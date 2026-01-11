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
        when(genAiService.generateContent(anyString())).thenReturn("{\"analisis_diario\": []}");

        // First Call
        weatherService.processWeather(request);

        // Second Call - Should hit cache
        weatherService.processWeather(request);

        // Verify GenAI was called ONLY ONCE
        verify(genAiService, times(1)).generateContent(anyString());
    }

    @Test
    public void testProcessWeatherReturnsFilteredResponse() {
        // Setup
        WeatherRequest request = new WeatherRequest();
        String lat = "30.0";
        String lon = "40.0";
        Date date = Date.valueOf("2026-01-11");
        request.setLatitude(lat);
        request.setLongitude(lon);
        request.setFechaVuelo(date);

        WeatherResponse mockOpenMeteoResponse = new WeatherResponse();
        mockOpenMeteoResponse.setHourly(new WeatherResponse.Hourly());
        mockOpenMeteoResponse.getHourly().setWindSpeed10m(new ArrayList<>());

        // Mock RestTemplate to return the full OpenMeteo response
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(WeatherResponse.class)))
                .thenReturn(mockOpenMeteoResponse);

        // Mock GenAI
        String aiJson = "{\"analisis_diario\": []}";
        when(genAiService.generateContent(anyString())).thenReturn(aiJson);

        // Execute
        WeatherResponse finalResponse = weatherService.processWeather(request);

        // Verify
        // Check Request Fields are present
        org.junit.jupiter.api.Assertions.assertEquals(lat, finalResponse.getRequestLatitude());
        org.junit.jupiter.api.Assertions.assertEquals(lon, finalResponse.getRequestLongitude());
        org.junit.jupiter.api.Assertions.assertEquals(date, finalResponse.getRequestFechaVuelo());

        // Check AI Analysis is present
        org.junit.jupiter.api.Assertions.assertNotNull(finalResponse.getAiAnalysis());

        // Check OpenMeteo fields are NULL (because we created a new object)
        org.junit.jupiter.api.Assertions.assertNull(finalResponse.getHourly());
        org.junit.jupiter.api.Assertions.assertNull(finalResponse.getLatitude()); // Should be null in the new object
    }
}
