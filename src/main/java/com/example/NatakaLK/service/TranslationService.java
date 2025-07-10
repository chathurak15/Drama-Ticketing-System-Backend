package com.example.NatakaLK.service;

import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class TranslationService {
    @Value("${google.api.key}")
    private String apiKey;

    public String translateText(String text, String targetLanguage) {
        try {
            String jsonBody = new JSONObject()
                    .put("q", text)
                    .put("target", targetLanguage)
                    .put("format", "text")
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://translation.googleapis.com/language/translate/v2?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            return json.getJSONObject("data")
                    .getJSONArray("translations")
                    .getJSONObject(0)
                    .getString("translatedText");

        } catch (Exception e) {
            e.printStackTrace();
            return text; // Fallback to original
        }
    }
}
