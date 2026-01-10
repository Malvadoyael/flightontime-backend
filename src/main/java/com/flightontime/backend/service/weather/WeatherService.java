package com.flightontime.backend.service.weather;

import com.flightontime.backend.model.weather.WeatherResponse;
import com.flightontime.backend.model.weather.WeatherRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

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

    private static final String API_URL_TEMPLATE = "https://api.open-meteo.com/v1/forecast?" +
            "latitude=%s&longitude=%s" +
            "&hourly=temperature_2m,dew_point_2m,precipitation,weather_code,visibility," +
            "wind_speed_10m,wind_direction_10m,wind_gusts_10m,freezing_level_height," +
            "cloud_cover_low,snow_depth,cape" +
            "&wind_speed_unit=kn&timezone=auto&start_date=%s&end_date=%s";

    public WeatherResponse processWeather(WeatherRequest request) {
        String latitude = request.getLatitude();
        String longitude = request.getLongitude();
        java.time.LocalDate flightDate = request.getFechaVuelo().toLocalDate();

        // Mantenemos el rango de -1 y +1 día para contexto
        String startDate = flightDate.minusDays(1).toString();
        String endDate = flightDate.plusDays(1).toString();

        String url = String.format(API_URL_TEMPLATE, latitude, longitude, flightDate.toString(), endDate);

        // URL completa con los parámetros "Master Request" que definimos antes
        // String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
        // "&longitude=" + longitude
        // +
        // "&hourly=temperature_2m,dew_point_2m,precipitation,weather_code,visibility,wind_speed_10m,wind_direction_10m,wind_gusts_10m,freezing_level_height,cloud_cover_low,snow_depth,cape&wind_speed_unit=kn&timezone=auto&start_date="
        // + startDate + "&end_date=" + endDate;

        logger.info("Consultando Open-Meteo: {}", url);

        try {
            // String jsonString =
            // objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            // logger.info("Received Weather Request: {}", jsonString);

            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

            if (response == null || response.getHourly() == null) {
                return null;
            }

            // 4. Lógica de IA (Gemini)
            String cacheKey = latitude + "," + longitude + "," + flightDate.toString();

            if (analysisCache.containsKey(cacheKey)) {
                logger.info("Usando análisis de IA en caché para: {}", cacheKey);
                String cachedJsonString = analysisCache.get(cacheKey);
                JsonNode jsonNode = objectMapper.readTree(cachedJsonString);
                response.setAiAnalysis(jsonNode);
            } else {
                generateAiAnalysis(response, flightDate, cacheKey);
            }

            return response;
        } catch (Exception e) {
            logger.error("Error al procesar la solicitud de clima", e);
            return null;
        }
    }

    private void generateAiAnalysis(WeatherResponse response, LocalDate flightDate, String cacheKey) {
        try {

            String weatherDataForAI = objectMapper.writeValueAsString(response.getHourly());

            // --- PROMPT MODIFICADO: AGRUPACIÓN POR DÍA ---
            String systemPrompt = """
                    ROL:
                    Eres la API de inteligencia de "Flight On Time".

                    TAREA:
                    Analiza la data meteorológica completa adjunta.
                    Debes identificar CADA FECHA distinta presente en los datos y generar un reporte independiente para cada día.

                    REGLAS DE CÁLCULO (Por bloque horario):
                    1. NIEBLA (Vis < 800m): +60%% prob.
                    2. TORMENTA/CAPE > 1000: +50%% prob.
                    3. VIENTO > 30kn: +40%% prob.
                    4. HIELO: +30%% prob.

                    FORMATO DE SALIDA (ESTRICTO JSON):
                    Responde ÚNICAMENTE con este JSON estructurado por días:
                    {
                      "analisis_diario": [
                        {
                          "fecha": "YYYY-MM-DD",
                          "estado_general_dia": "EN_HORARIO" | "PROBABLE_RETRASO" | "CANCELADO",
                          "probabilidad_promedio_dia": (int 0-100, promedio de los bloques de este día),
                          "resumen_ejecutivo_dia": "Resumen específico de las condiciones de ESTE día.",
                          "bloques_horarios": [
                             {
                               "hora_inicio": "00:00",
                               "hora_fin": "04:00",
                               "estado": "VERDE" | "AMARILLO" | "ROJO",
                               "probabilidad": (int 0-100),
                               "factor_principal": "Causa breve"
                             },
                             ... (Generar los 6 bloques de 4 horas para este día)
                          ]
                        },
                        ... (Repetir estructura para el siguiente día encontrado en la data)
                      ]
                    }
                    """;

            // Quitamos el %s del prompt porque ahora la IA deduce las fechas de la data
            String finalPrompt = systemPrompt + "\n\nDATOS (HOURLY):\n" + weatherDataForAI;

            logger.info("Prompt enviado a IA: {}", finalPrompt);

            String aiResponseString = genAiService.generateContent(finalPrompt);

            // Limpieza básica por si Gemini devuelve Markdown (```json ... ```)
            aiResponseString = cleanMarkdown(aiResponseString);

            // Convertimos String -> JsonNode
            JsonNode jsonNode = objectMapper.readTree(aiResponseString);

            response.setAiAnalysis(jsonNode);

            // Guardamos en caché el String original
            analysisCache.put(cacheKey, aiResponseString);

        } catch (JsonProcessingException e) {
            logger.error("Error serializando datos para IA", e);
        }
    }

    // Método auxiliar para limpiar las comillas de markdown si la IA se equivoca
    private String cleanMarkdown(String response) {
        if (response.startsWith("```json")) {
            return response.replace("```json", "").replace("```", "").trim();
        }
        if (response.startsWith("```")) {
            return response.replace("```", "").trim();
        }
        return response;
    }
}