package com.flightontime.backend.service.genai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.HttpOptions;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Cambiado de javax a jakarta para compatibilidad con Spring Boot 3
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleGeminiService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleGeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    private Client client;

    @PostConstruct
    public void init() {
        try {
            // Inicialización con HttpOptions para timeouts
            HttpOptions httpOptions = HttpOptions.builder()

                    .timeout((int) Duration.ofSeconds(120).toMillis())
                    .build();

            this.client = Client.builder()
                    .apiKey(apiKey)
                    .httpOptions(httpOptions)
                    .build();
            logger.info("SimpleGeminiService initialized successfully.");
        } catch (Exception e) {
            logger.error("Error initializing SimpleGeminiService", e);
        }
    }

    public String testModel(String modelName, String instruction) {
        logger.info("Testing model: {} with instruction: {}", modelName, instruction);
        if (modelName == null || modelName.trim().isEmpty()) {
            return "Error: El nombre del modelo es requerido.";
        }

        try {
            // Generación de contenido simple sin configuraciones adicionales conflictivas
            GenerateContentResponse response = client.models.generateContent(modelName, instruction, null);
            if (response != null) {
                return response.text();
            } else {
                return "Error: Respuesta vacía del modelo.";
            }
        } catch (Exception e) {
            logger.error("Error testing model: {}", modelName, e);
            return "Error probando el modelo '" + modelName + "': " + e.getMessage();
        }
    }

    /**
     * Método simplificado para evitar errores con ListModelsConfig y Pagers
     * Proporciona una lista fija de modelos compatibles o un mensaje de estado.
     */
    public List<String> listModels() {
        List<String> modelNames = new ArrayList<>();
        modelNames.add("Gemini 1.5 Flash (Sugerido)");
        modelNames.add("Gemini 1.5 Pro");

        logger.info("Listing static model names to avoid library version conflicts.");
        return modelNames;
    }
}