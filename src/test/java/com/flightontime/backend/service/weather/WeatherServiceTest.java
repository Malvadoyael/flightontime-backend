package com.flightontime.backend.service.weather;

import com.flightontime.backend.model.weather.WeatherRequest;
import com.flightontime.backend.model.weather.WeatherResponse;
import com.flightontime.backend.service.genai.GenAiService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Date;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private GenAiService genAiService;

    @MockBean
    private org.springframework.web.client.RestTemplate restTemplate;

    @Test
    public void testCaching() {
        // Setup
        WeatherRequest request = new WeatherRequest();
        request.setLatitude("10.0");
        request.setLongitude("20.0");
        request.setFechaVuelo(Date.valueOf("2024-01-01"));

        WeatherResponse mockResponse = new WeatherResponse();
        mockResponse.setHourly(new WeatherResponse.Hourly());
        // Initialize lists to avoid null pointer in summarize
        mockResponse.getHourly().setWindSpeed10m(new ArrayList<>());
        mockResponse.getHourly().setWindGusts10m(new ArrayList<>());
        mockResponse.getHourly().setVisibility(new ArrayList<>());
        mockResponse.getHourly().setPrecipitation(new ArrayList<>());

        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(WeatherResponse.class)))
                .thenReturn(mockResponse);
        when(genAiService.generateContent(anyString())).thenReturn("AI Analysis Result");

        // First Call
        weatherService.processWeather(request);

        // Second Call - Should hit cache
        weatherService.processWeather(request);

        // Verify GenAI was called ONLY ONCE
        verify(genAiService, times(1)).generateContent(anyString());
    }
}
