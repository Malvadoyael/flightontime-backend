package com.flightontime.backend.service.weather;

import com.flightontime.backend.model.weather.WeatherResponse;
import com.flightontime.backend.model.weather.WeatherRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.web.client.RestTemplate restTemplate;

    public WeatherResponse processWeather(WeatherRequest request) {
        String latitude = request.getLatitude();
        String longitude = request.getLongitude();

        // Default or usage of hardcoded parameters as requested
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
                + "&hourly=temperature_2m,visibility,wind_speed_10m,wind_direction_10m,wind_gusts_10m,cloud_cover,weather_code&wind_speed_unit=kn&timezone=auto&start_date=2026-01-07&end_date=2026-01-13";

        try {
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            logger.info("Received Weather Request: {}", jsonString);

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            return response;
        } catch (JsonProcessingException e) {
            logger.error("Error processing weather request json", e);
            return null;
        } catch (Exception e) {
            logger.error("Error calling weather API", e);
            return null;
        }
    }
}
