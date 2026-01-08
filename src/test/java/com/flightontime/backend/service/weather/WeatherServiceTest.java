package com.flightontime.backend.service.weather;

import com.flightontime.backend.model.weather.WeatherRequest;
import com.flightontime.backend.model.weather.WeatherResponse;
import com.flightontime.backend.service.genai.GenAiService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GenAiService genAiService;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    public void testProcessWeatherWithAiAnalysis() {
        // Mock Request
        WeatherRequest request = new WeatherRequest();
        request.setLatitude("-33.45");
        request.setLongitude("-70.66");
        request.setFechaVuelo(Date.valueOf("2026-01-10"));

        // Mock Weather API Response
        WeatherResponse mockWeatherResponse = new WeatherResponse();
        WeatherResponse.Hourly mockHourly = new WeatherResponse.Hourly();
        mockWeatherResponse.setHourly(mockHourly);

        when(restTemplate.getForObject(anyString(), eq(WeatherResponse.class)))
                .thenReturn(mockWeatherResponse);

        // Mock AI Service Response
        String mockAiAnalysis = "Predicted delay due to high winds.";
        when(genAiService.generateContent(anyString())).thenReturn(mockAiAnalysis);

        // Execute
        WeatherResponse response = weatherService.processWeather(request);

        // Verify
        assertNotNull(response);
        assertEquals(mockAiAnalysis, response.getAiAnalysis());
    }
}
