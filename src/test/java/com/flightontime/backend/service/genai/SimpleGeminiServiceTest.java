package com.flightontime.backend.service.genai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "gemini.api.key=dummy-key",
        "gemini.model.name=gemini-1.5-flash"
})
public class SimpleGeminiServiceTest {

    @Autowired
    private SimpleGeminiService simpleGeminiService;

    @Test
    public void testInitialization() {
        // The service should be initialized by Spring.
        // We can verify it's not null, or call a method that doesn't require a valid
        // API key if we mock the client?
        // But the init() method runs @PostConstruct, so if it fails, the context load
        // fails.
        // This test serves to check if context loads successfully with
        // SimpleGeminiService.
        System.out.println("SimpleGeminiService initialized: " + (simpleGeminiService != null));
    }
}
