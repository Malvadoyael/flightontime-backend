package com.flightontime.backend.controller;

import com.flightontime.backend.service.genai.SimpleGeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/simple-gemini")
public class SimpleGeminiController {

    @Autowired
    private SimpleGeminiService simpleGeminiService;

    @PostMapping("/test")
    public String test(@RequestBody Map<String, String> payload) {
        String model = payload.get("model");
        String instruction = payload.get("instruction");

        if (model == null) {
            // Fallback default for easier testing of just "instruction"
            model = "gemini-1.5-flash";
        }
        if (instruction == null) {
            return "Error: Instrucción requerida";
        }

        return simpleGeminiService.testModel(model, instruction);
    }

    @GetMapping("/models")
    public List<String> listModels() {
        return simpleGeminiService.listModels();
    }
}
