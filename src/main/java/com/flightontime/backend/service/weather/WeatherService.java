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

        // Mantenemos el rango de -1 y +1 día para contexto
        String startDate = flightDate.minusDays(1).toString();
        String endDate = flightDate.plusDays(1).toString();

        // URL completa con los parámetros "Master Request" que definimos antes
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
                        // PASO CRÍTICO: Convertimos los datos horarios a String JSON para que la IA los
                        // lea.
                        // Si tienes un método 'summarizeWeather', asegúrate de que NO borre los
                        // números,
                        // la IA necesita ver "visibility: 500" para aplicar la regla.
                        // Aquí asumo que usas Jackson para pasarle el objeto 'hourly' crudo o resumido.
                        String weatherDataForAI = objectMapper.writeValueAsString(response.getHourly());

                        // --- AQUÍ INSERTAMOS EL PROMPT DE INGENIERÍA AVANZADA ---
                        String systemPrompt = """
                                ROL:
                                Actúa como un Experto en Meteorología Aeronáutica y Despachador de Vuelos.

                                TAREA:
                                Analiza los datos meteorológicos adjuntos para un vuelo programado el %s.
                                Evalúa condiciones peligrosas según estándares ICAO/FAA.

                                REGLAS DE NEGOCIO Y UMBRALES DE RIESGO:
                                1. Visibilidad (LVP): CRÍTICO si visibility < 800m. ADVERTENCIA si < 3000m.
                                2. Viento (Ráfagas): CRÍTICO si wind_gusts_10m > 40 kn. ADVERTENCIA si > 25 kn.
                                3. Tormentas (CAPE): CRÍTICO si weather_code es 95-99 O cape > 1000 J/kg.
                                4. Hielo (De-icing): ADVERTENCIA si freezing_level_height < 1500m Y precipitation > 0.
                                5. Pista: CRÍTICO si snow_depth > 0.01m.

                                FORMATO DE SALIDA (Genera solo este resumen):
                                ### 🛫 Informe de Operacionalidad
                                **Estado:** [VERDE / AMARILLO / ROJO]
                                **Riesgos Detectados:**
                                * [Variable]: [Valor] -> [Impacto Operativo]
                                **Justificación:** [Breve explicación técnica]
                                **Ventana Segura:** [Mejor hora para volar]
                                """;

                        // Inyectamos la fecha y los datos
                        String prompt = String.format(systemPrompt, flightDate) + "\n\nDATOS DEL CLIMA:\n"
                                + weatherDataForAI;

                        // Llamada a Gemini
                        String analysis = genAiService.generateContent(prompt);

                        response.setAiAnalysis(analysis);

                        // Store in cache
                        analysisCache.put(cacheKey, analysis);
                    } catch (Exception e) {
                        logger.error("Error generating AI analysis", e);
                        response.setAiAnalysis("No se pudo generar el análisis de IA. Consulte METAR oficial.");
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

}
