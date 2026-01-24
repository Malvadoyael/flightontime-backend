package com.flightontime.backend.controller;

import com.flightontime.backend.service.genai.GenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para la integración con servicios de IA generativa.
 * Permite generar contenido basado en prompts proporcionados.
 */
@RestController
@RequestMapping("/api/v1/genai")
public class GenAiController {

    @Autowired
    private GenAiService genAiService;

    /**
     * Genera contenido utilizando IA generativa basado en un prompt.
     *
     * @param payload Mapa que contiene el prompt en la clave "prompt".
     * @return Contenido generado como cadena de texto.
     */
    @PostMapping("/generate")
    public String generate(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        if (prompt == null || prompt.isEmpty()) {
            return "Prompt is required";
        }
        return genAiService.generateContent(prompt);
    }
}
