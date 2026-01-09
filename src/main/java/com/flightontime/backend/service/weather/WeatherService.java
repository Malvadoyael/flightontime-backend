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

    @org.springframework.beans.factory.annotation.Autowired
    private com.flightontime.backend.service.genai.GenAiService genAiService;

    // Simple in-memory cache to avoid repeated API calls
    private java.util.Map<String, String> analysisCache = new java.util.concurrent.ConcurrentHashMap<>();

    public WeatherResponse processWeather(WeatherRequest request) {
        String latitude = request.getLatitude();
        String longitude = request.getLongitude();
        java.time.LocalDate flightDate = request.getFechaVuelo().toLocalDate();
        String startDate = flightDate.minusDays(1).toString();
        String endDate = flightDate.plusDays(1).toString();

        // Default or usage of hardcoded parameters as requested
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude
                + "&hourly=temperature_2m,dew_point_2m,precipitation,weather_code,visibility,wind_speed_10m,wind_direction_10m,wind_gusts_10m,freezing_level_height,cloud_cover_low,snow_depth,cape&wind_speed_unit=kn&timezone=auto&start_date="
                + startDate + "&end_date=" + endDate;

        try {
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            logger.info("Received Weather Request: {}", jsonString);

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response != null) {
                // Check cache
                String cacheKey = latitude + "," + longitude + "," + flightDate.toString();
                if (analysisCache.containsKey(cacheKey)) {
                    logger.info("Using cached AI analysis for key: {}", cacheKey);
                    response.setAiAnalysis(analysisCache.get(cacheKey));
                } else {
                    try {
                        // Describe weather data in a summarized way to save tokens
                        String weatherSummary = summarizeWeather(response.getHourly());
                        String prompt = "Analiza los siguientes datos resumidos del clima para un vuelo el "
                                + flightDate
                                + " y determina si hay riesgo de retraso o cancelacion. Resume las condiciones en un párrafo corto.\n"
                                + weatherSummary;
                        // String prompt = "Di Hola soy un modelo de IA, dime tu nombre";
                        String analysis = genAiService.generateContent(prompt);
                        response.setAiAnalysis(analysis);

                        // Store in cache
                        analysisCache.put(cacheKey, analysis);
                    } catch (Exception e) {
                        logger.error("Error generating AI analysis", e);
                        response.setAiAnalysis("No se pudo generar el análisis de IA.");
                    }
                }
            }

            return response;
        } catch (JsonProcessingException e) {
            logger.error("Error processing weather request json", e);
            return null;
        } catch (Exception e) {
            logger.error("Error calling weather API", e);
            return null;
        }
    }

    private String summarizeWeather(WeatherResponse.Hourly hourly) {
        if (hourly == null)
            return "No hay datos detallados.";

        // Calculate max values for risks
        double maxWind = hourly.getWindSpeed10m().stream().mapToDouble(d -> d).max().orElse(0.0);
        double maxGusts = hourly.getWindGusts10m().stream().mapToDouble(d -> d).max().orElse(0.0);
        double minVisibility = hourly.getVisibility().stream().mapToDouble(d -> d).min().orElse(99999.0);
        double maxPrecip = hourly.getPrecipitation().stream().mapToDouble(d -> d).max().orElse(0.0);

        return String.format(
                "Viento Máx: %.1f kn, Ráfagas Máx: %.1f kn, Visibilidad Mín: %.0f m, Precipitación Máx: %.1f mm",
                maxWind, maxGusts, minVisibility, maxPrecip);
    }
}
