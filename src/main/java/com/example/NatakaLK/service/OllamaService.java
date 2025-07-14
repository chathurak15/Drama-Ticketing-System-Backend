package com.example.NatakaLK.service;

import com.example.NatakaLK.model.Drama;
import com.example.NatakaLK.model.Show;
import com.example.NatakaLK.repo.ActorRepo;
import com.example.NatakaLK.repo.DramaRepo;
import com.example.NatakaLK.repo.ShowRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OllamaService {

    private final String OLLAMA_URL = "http://localhost:11434/api/chat";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ShowRepo showRepo;

    @Autowired
    private DramaRepo dramaRepo;

    @Autowired
    private ActorRepo actorRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getReply(String userMessage) {
        try {
            String aiResponse = callOllamaIntentExtraction(userMessage);

            aiResponse = aiResponse.strip();
            int jsonStart = aiResponse.indexOf('{');
            if (jsonStart >= 0) {
                aiResponse = aiResponse.substring(jsonStart);
            } else {
                return "❌ Sorry, no JSON found in AI response.";
            }
            JsonNode root = objectMapper.readTree(aiResponse);
            String intent = root.path("intent").asText(null);
            JsonNode params = root.path("parameters");

            if (intent == null) {
                return "❌ Sorry, intent not found in AI response.";
            }
            switch (intent) {
                case "get_upcoming_shows":
                    return getUpcomingShowsMessage();

                case "get_show_details":
                    return getShowDetails(params);

                case "get_drama_details":
                    return getDramaDetails(params);

//                case "get_actor_details":
//                    return getActorDetails(params);

                default:
                    return "Sorry, I didn't understand your question. Please try asking about dramas, shows, or actors.";
            }

        } catch (Exception e) {
            // If AI call or JSON parsing fails, fallback to normal chat call or error message
            return "❌ Sorry, I couldn't process your request: " + e.getMessage();
        }
    }

    private String callOllamaIntentExtraction(String userMessage) {
        // Prepare prompt messages for intent extraction
        List<Object> messages = List.of(
                // System prompt explains the role of AI to parse intents
                new Message("system", "You are an intent extraction assistant for a stage drama website. Parse user questions into JSON with intent and parameters. " +
                        "Available intents: get_upcoming_shows, get_show_details, get_drama_details, get_actor_details. " +
                        "Example output: {\"intent\":\"get_show_details\", \"parameters\":{\"show_title\":\"Lowrancege Manamali\"}}"),

                new Message("user", userMessage)
        );

        var requestBody = Map.of(
                "model", "llama3.2:1b",
                "messages", messages,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(OLLAMA_URL, HttpMethod.POST, entity, Map.class);
        Map<String, Object> body = response.getBody();

        if (body != null && body.get("message") instanceof Map) {
            Map<String, Object> message = (Map<String, Object>) body.get("message");
            Object content = message.get("content");
            if (content != null) {
                return content.toString();
            }
        }

        throw new RuntimeException("No content received from Ollama");
    }

    private String getUpcomingShowsMessage() {
        List<Show> shows = showRepo.findUpcomingShows();

        if (shows.isEmpty()) {
            return "📭 No upcoming shows found at the moment.";
        }

        StringBuilder response = new StringBuilder("🎭 Here are some upcoming shows:\n\n");

        for (Show show : shows) {
            response.append("🎫 *").append(show.getTitle()).append("*\n")
                    .append("📍 Location: ").append(show.getLocation()).append(", ").append(show.getCity().getCityName()).append("\n")
                    .append("📅 Date: ").append(show.getShowDate().toString()).append(" ⏰ ").append(show.getShowTime()).append("\n")
                    .append("📝 Drama: ").append(show.getDrama().getTitle()).append("\n\n");
        }

        return response.toString();
    }

    private String getShowDetails(JsonNode params) {
        String title = params.path("show_title").asText(null);
        if (title == null || title.isEmpty()) {
            return "Please specify the show title.";
        }
        // Search shows by title (exact or partial)
        List<Show> shows = showRepo.findShowsByDramaTitle(title);
        if (shows.isEmpty()) {
            return "No shows found matching title: " + title;
        }
        StringBuilder response = new StringBuilder("Details for shows matching: " + title + "\n\n");
        for (Show show : shows) {
            response.append("🎫 *").append(show.getTitle()).append("*\n")
                    .append("📍 Location: ").append(show.getLocation()).append(", ").append(show.getCity().getCityName()).append("\n")
                    .append("📅 Date: ").append(show.getShowDate().toString()).append(" ⏰ ").append(show.getShowTime()).append("\n")
                    .append("📝 Drama: ").append(show.getDrama().getTitle()).append("\n\n");
        }
        return response.toString();
    }

    private String getDramaDetails(JsonNode params) {
        String title = params.path("drama_title").asText(null);
        if (title == null || title.isEmpty()) {
            return "Please specify the drama title.";
        }

        List<Drama> dramas = dramaRepo.findByTitleContainingIgnoreCase(title, null).getContent();
        if (dramas.isEmpty()) {
            return "No dramas found matching title: " + title;
        }

        StringBuilder response = new StringBuilder("Details for dramas matching: " + title + "\n\n");
        for (Drama drama : dramas) {
            response.append("🎭 *").append(drama.getTitle()).append("*\n")
                    .append("📝 Description: ").append(drama.getDescription() != null ? drama.getDescription() : "N/A").append("\n");

        }
        return response.toString();
    }

//    private String getActorDetails(JsonNode params) {
//        String actorName = params.path("actor_name").asText(null);
//        if (actorName == null || actorName.isEmpty()) {
//            return "Please specify the actor's name.";
//        }
//
//        // Assuming you have a method in actorRepo to find actors by name ignoring case and partial match
//        List<Actor> actors = actorRepo.findByNameContainingIgnoreCase(actorName);
//        if (actors.isEmpty()) {
//            return "No actors found matching name: " + actorName;
//        }
//
//        StringBuilder response = new StringBuilder("Details for actors matching: " + actorName + "\n\n");
//        for (Actor actor : actors) {
//            response.append("🎭 *").append(actor.getName()).append("*\n")
//                    .append("🎂 DOB: ").append(actor.getBirthday() != null ? actor.getBirthday().toString() : "N/A").append("\n")
//                    .append("📝 Gender: ").append(actor.getGender() != null ? actor.getGender() : "N/A").append("\n\n");
//        }
//        return response.toString();
//    }
//
    // Simple class for messages in prompt
    static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
