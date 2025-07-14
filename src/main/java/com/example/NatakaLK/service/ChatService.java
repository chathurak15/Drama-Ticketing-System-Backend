//package com.example.NatakaLK.service;
//
//import com.example.NatakaLK.dto.requestDTO.ChatRequestDTO;
//import com.example.NatakaLK.dto.responseDTO.ChatResponseDTO;
//import com.example.NatakaLK.model.Show;
//import com.example.NatakaLK.repo.ShowRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ChatService {
////    @Autowired
////    private OpenAiService openAiService;
//
//    @Autowired
//    private ShowRepo showRepository;
//
//    public ChatResponseDTO getSmartReply(ChatRequestDTO requestDTO) {
//        String message = requestDTO.getMessage().toLowerCase();
//
//        // Search upcoming shows
//        if (message.contains("upcoming shows")) {
//            List<Show> shows = showRepository.findUpcomingShows();
//            return new ChatResponseDTO(formatShows(shows));
//        }
//
//        // Search by city name
//        if (message.contains("colombo") || message.contains("gampaha")) {
//            String cityName = message.contains("colombo") ? "Colombo" : "Gampaha";
//            List<Show> shows = showRepository.findUpcomingShowsByCity(cityName);
//            return new ChatResponseDTO("Upcoming shows in " + cityName + ":\n" + formatShows(shows));
//        }
//
//        // Search by drama title
//        if (message.contains("suba saha") || message.contains("oluhaluwo") || message.contains("lowrance")) {
//            String dramaTitle = extractDramaKeyword(message); // build helper method
//            List<Show> shows = showRepository.findShowsByDramaTitle(dramaTitle);
//            return new ChatResponseDTO("Shows for drama \"" + dramaTitle + "\":\n" + formatShows(shows));
//        }
//
//        // Fallback to GPT
//        CompletionRequest completionRequest = CompletionRequest.builder()
//                .prompt("User: " + message + "\nAI:")
//                .model("text-davinci-003")
//                .maxTokens(100)
//                .temperature(0.7)
//                .build();
//
//        String gptReply = openAiService.createCompletion(completionRequest)
//                .getChoices().get(0).getText();
//
//        return new ChatResponseDTO(gptReply.trim());
//    }
//
//    private String formatShows(List<Show> shows) {
//        if (shows == null || shows.isEmpty()) return "❌ No shows found.";
//
//        StringBuilder sb = new StringBuilder();
//        for (Show s : shows) {
//            sb.append("🆔 Show: ").append(s.getTitle()).append("\n")
//                    .append("📅 Date: ").append(s.getShowDate()).append("\n")
//                    .append("🕕 Time: ").append(s.getShowTime()).append("\n")
//                    .append("📍 Venue: ").append(s.getLocation()).append("\n")
//                    .append("―――――――――――――――――――――――――\n");
//        }
//        return sb.toString();
//    }
//
//    private String extractDramaKeyword(String message) {
//        if (message.contains("suba saha")) return "Suba Saha Yasa";
//        if (message.contains("oluhaluwo")) return "Oluhaluwo";
//        if (message.contains("lowrance")) return "Lowrancege Manamali";
//        if (message.contains("guru tharu")) return "Garu Tharu";
//        return "Suba Saha Yasa"; // fallback
//    }
//}
