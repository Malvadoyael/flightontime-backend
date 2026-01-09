package com.flightontime.backend.service.genai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
<<<<<<< HEAD
=======
import com.google.genai.types.ListModelsConfig;
>>>>>>> ffef73aef39bc50a21c824ba641d9aa1eea81435
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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
            this.client = Client.builder().apiKey(apiKey).build();
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
<<<<<<< HEAD
            // Pasamos null en config porque no lo necesitamos para una prueba simple
=======
>>>>>>> ffef73aef39bc50a21c824ba641d9aa1eea81435
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

<<<<<<< HEAD
    // HEMOS SIMPLIFICADO ESTE MÉTODO PARA EVITAR ERRORES DE COMPILACIÓN
    public List<String> listModels() {
        List<String> modelNames = new ArrayList<>();
        modelNames.add("La lista de modelos está deshabilitada temporalmente para evitar errores.");
        // Aquí borramos el código que causaba conflicto con ListModelsConfig
        return modelNames;
    }
}
=======
    public List<String> listModels() {
        List<String> modelNames = new ArrayList<>();
        modelNames.add("Attempting to list models...");
        try {
            // Using ListModelsConfig builder
            ListModelsConfig config = ListModelsConfig.builder().build();

            // Iterate over models. Note: client.models.list returns a Pager or Iterable
            for (var model : client.models.list(config)) {
                // Handling potential Optional return type or just String
                Object nameObj = model.name();
                String name = nameObj != null ? nameObj.toString() : "null";
                modelNames.add(name);
                logger.debug("Found model: {}", name);
            }

            if (modelNames.size() == 1) {
                modelNames.add("No models found (list is empty).");
            }
        } catch (Exception e) {
            logger.error("Error listing models", e);
            modelNames.add("Error listing models: " + e.getMessage());
        }
        return modelNames;
    }
}
>>>>>>> ffef73aef39bc50a21c824ba641d9aa1eea81435
