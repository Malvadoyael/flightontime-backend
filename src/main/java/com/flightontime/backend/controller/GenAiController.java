package com.flightontime.backend.controller;

import com.flightontime.backend.service.genai.GenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/genai")
public class GenAiController {

    @Autowired
    private GenAiService genAiService;

    @PostMapping("/generate")
    public String generate(@RequestBody Map<String, String> payload) {
        String prompt = payload.get("prompt");
        if (prompt == null || prompt.isEmpty()) {
            return "Prompt is required";
        }
        return genAiService.generateContent(prompt);
    }
}
