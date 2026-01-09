package com.flightontime.backend.service.genai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest

public class GenAiServiceTest {

     
    @Autowired
    private GenAiService genAiService;

    @Test
    public void testGenAiServiceLoad() {
        // This test ensures the service and its dependencies (GenAI Client) load
        // correctly
        assertNotNull(genAiService);
    }
}
