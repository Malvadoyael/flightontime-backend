package com.flightontime.backend.service.genai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class GenAiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public String generateContent(String prompt) {
        try {
            Client client = Client.builder().apiKey(apiKey).build();
            GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash-exp", prompt, null);
            return response.text();
        } catch (Exception e) {
            return "Error calling Gemini: " + e.getMessage();
        }
    }
}
