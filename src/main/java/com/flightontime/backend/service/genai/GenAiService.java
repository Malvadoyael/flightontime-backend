package com.flightontime.backend.service.genai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct; // Asegúrate de tener esta dependencia o usar jakarta.annotation


@Service
public class GenAiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    // Inyectamos el modelo desde la configuración
    // En tu application.properties añade: gemini.model.name
    @Value("${gemini.model.name}")
    private String modelName;

    private Client client;

    @PostConstruct
    public void init() {
        // Inicialización del cliente
        this.client = Client.builder().apiKey(apiKey).build();
    }

    public String generateContent(String prompt) {
        try {
            // Usamos la variable modelName en lugar del string "hardcodeado"
            GenerateContentResponse response = client.models.generateContent(modelName, prompt, null);

            // Verificamos que la respuesta no sea nula antes de llamar a text()
            if (response != null) {
                return response.text();
            } else {
                return "Error: La respuesta de Gemini fue vacía.";
            }

        } catch (Exception e) {
            // Imprimir el stack trace en consola ayuda mucho a depurar errores 404 vs 403
            e.printStackTrace();
            return "Error calling Gemini: " + e.getMessage();
        }
    }
}