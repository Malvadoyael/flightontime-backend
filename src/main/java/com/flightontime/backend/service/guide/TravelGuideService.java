package com.flightontime.backend.service.guide;

import com.flightontime.backend.dto.TravelGuideRequest;
import com.flightontime.backend.service.genai.GenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TravelGuideService {

    private static final Logger logger = LoggerFactory.getLogger(TravelGuideService.class);
    private final GenAiService genAiService;

    public TravelGuideService(GenAiService genAiService) {
        this.genAiService = genAiService;
    }

    public String generateGuide(TravelGuideRequest request) {
        logger.info("Generating travel guide for Coords: [{}, {}], Date: {}",
                request.getLatitude(), request.getLongitude(), request.getTravelDate());

        String prompt = constructPrompt(request.getLatitude(), request.getLongitude(), request.getTravelDate());
        String response = genAiService.generateContent(prompt);
        return cleanResponse(response);
    }

    private String cleanResponse(String response) {
        if (response == null) {
            return "";
        }
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    private String constructPrompt(String lat, String lon, String date) {
        return "Actúa como un experto en Inteligencia Estratégica de Viajes y Arquitecto de Datos. Tu objetivo es generar un objeto JSON estricto, extenso y procesable basado únicamente en un par de coordenadas geográficas (aeropuerto) y una fecha específica.\n\n"
                +
                "**REGLAS DE SALIDA:**\n" +
                "- Responde ÚNICAMENTE con el objeto JSON. No incluyas texto introductorio ni conclusiones.\n" +
                "- Colores Leaflet (Estándar HEX): Aeropuerto (#000000), Transporte (#0000FF), Hotel Centro (#008000), Hotel Aeropuerto (#FFA500), Turismo (#FF0000), Comida (#8B4513).\n"
                +
                "- Navegación: Genera Deep Links funcionales para Google Maps (modo navegación) y Waze.\n" +
                "- Búsqueda: Genera URLs de Google Search específicas para cada prenda de ropa y para la compra de eSIM local.\n\n"
                +
                "**ESQUEMA JSON REQUERIDO:**\n" +
                "{\n" +
                "  \"destino\": {\n" +
                "    \"aeropuerto\": \"string\",\n" +
                "    \"ciudad\": \"string\",\n" +
                "    \"pais\": \"string\",\n" +
                "    \"info_pais\": {\n" +
                "      \"idioma\": \"string\",\n" +
                "      \"moneda_codigo\": \"string\",\n" +
                "      \"tasa_propina_sugerida\": \"string\",\n" +
                "      \"e_sim_recomendada_url\": \"string\"\n" +
                "    },\n" +
                "    \"tecnico\": {\n" +
                "      \"enchufes\": [\"string\"],\n" +
                "      \"voltaje\": \"string\",\n" +
                "      \"frecuencia\": \"string\"\n" +
                "    },\n" +
                "    \"emergencias\": { \"numero_unico\": \"string\", \"policia\": \"string\", \"ambulancia\": \"string\" }\n"
                +
                "  },\n" +
                "  \"analisis_climatico_historico\": {\n" +
                "    \"resumen\": \"string\",\n" +
                "    \"temp_rango\": \"string\",\n" +
                "    \"riesgos_meteorologicos\": \"string\",\n" +
                "    \"maleta_inteligente\": [\n" +
                "      { \"prenda\": \"string\", \"prioridad\": \"Alta|Media\", \"link_google_search\": \"string\" }\n"
                +
                "    ]\n" +
                "  },\n" +
                "  \"logistica_transporte_aeropuerto\": [\n" +
                "    {\n" +
                "      \"medio\": \"string\",\n" +
                "      \"costo_estimado_usd\": number,\n" +
                "      \"tiempo_minutos\": number,\n" +
                "      \"horario_recomendado\": \"string\",\n" +
                "      \"metodo_pago\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"puntos_interes_georreferenciados\": [\n" +
                "    {\n" +
                "      \"nombre\": \"string\",\n" +
                "      \"tipo\": \"aeropuerto|transporte|hotel_centro|hotel_aeropuerto|turismo|comida\",\n" +
                "      \"coordenadas\": { \"lat\": 0.0, \"lng\": 0.0 },\n" +
                "      \"color_hex\": \"string\",\n" +
                "      \"navegacion\": { \n" +
                "        \"gmaps_nav\": \"string\", \n" +
                "        \"waze_nav\": \"string\" \n" +
                "      },\n" +
                "      \"comentario_experto\": \"string\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"gastronomia_estacional\": {\n" +
                "    \"platos_sugeridos_fecha\": [\"string\"],\n" +
                "    \"bebida_tipica\": \"string\",\n" +
                "    \"precio_medio_menu_usd\": number\n" +
                "  },\n" +
                "  \"inteligencia_seguridad\": {\n" +
                "    \"nivel_riesgo\": \"string\",\n" +
                "    \"zonas_no_go\": [\"string\"],\n" +
                "    \"estafas_comunes_activas\": [\"string\"],\n" +
                "    \"frase_auxilio_local\": \"string\"\n" +
                "  }\n" +
                "}\n\n" +
                "---\n" +
                "**DATOS DE ENTRADA:**\n" +
                "- Coordenadas: " + lat + ", " + lon + "\n" +
                "- Fecha: " + date + "\n";
    }
}
