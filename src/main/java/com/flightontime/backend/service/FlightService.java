package com.flightontime.backend.service;

import com.flightontime.backend.model.Flight;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FlightService {

    private static final Logger logger = LoggerFactory.getLogger(FlightService.class);

    /**
     * Este método simula lo que hará tu modelo de Data Science.
     * Por ahora, genera una probabilidad basada en reglas lógicas simples.
     */
    public Flight predictDelay(Flight flight) {
        logger.info("Calculando prediccion para el vuelo: {}", flight.getFlightNumber());
        Random random = new Random();
        double baseProbability = 0.10; // 10% base

        // Regla lógica: Si el vuelo es a "MEX" (CDMX), hay más tráfico, aumenta 15%
        if ("MEX".equalsIgnoreCase(flight.getDestination())) {
            baseProbability += 0.15;
        }

        // Simulamos una variación aleatoria del clima
        double weatherFactor = random.nextDouble() * 0.20;

        flight.setDelayProbability(baseProbability + weatherFactor);

        logger.info("Resultado de la prediccion: {}", flight);
        return flight;
    }

    public List<Flight> originList() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(1L, "AM123", "Aeroméxico", "MEX", "JFK", LocalDateTime.now().plusHours(4), 0.1));
        flights.add(new Flight(2L, "UA456", "United", "IAH", "CUN", LocalDateTime.now().plusHours(2), 0.2));
        flights.add(new Flight(3L, "IB789", "Iberia", "MAD", "MEX", LocalDateTime.now().plusHours(10), 0.05));
        return flights;
    }

    /**
     * Lee el archivo de modelo 'modelo_vuelos.json' para verificar las
     * características disponibles.
     * Rellena el origen, destino y aerolínea del vuelo basándose en la lógica
     * del modelo y valores por defecto.
     *
     * @param flight El objeto vuelo a procesar.
     * @return El objeto vuelo con los datos actualizados.
     */
    public Flight testingModelEdu(Flight flight) {
        try {

            logger.info("Flight: {}", flight);
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            // Load the resource from classpath
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(
                    "modelo_vuelos.json");
            com.fasterxml.jackson.databind.JsonNode rootNode = mapper.readTree(resource.getFile());

            // Extract feature_names
            com.fasterxml.jackson.databind.JsonNode learner = rootNode.path("learner");
            com.fasterxml.jackson.databind.JsonNode featureNames = learner.path("feature_names");
            logger.info("Feature names: {}", featureNames);
            if (featureNames.isArray()) {
                boolean hasOrigin = false;
                boolean hasDest = false;
                boolean hasAirline = false; // Usually not in this specific model based on checks

                for (com.fasterxml.jackson.databind.JsonNode node : featureNames) {
                    String feature = node.asText();
                    if ("origin".equals(feature)) {
                        hasOrigin = true;
                    } else if ("dest".equals(feature)) {
                        hasDest = true;
                    } else if ("airline".equals(feature)) {
                        hasAirline = true;
                    }
                }

                // Populate logic based on model features
                if (hasOrigin) {
                    flight.setOrigin("MEX"); // Default as mapping is missing
                }
                if (hasDest) {
                    flight.setDestination("CUN"); // Default as mapping is missing
                }

                // Explicitly requested to populate airline even if missing in features (which
                // it is)
                // or if it were present. User request said "rellena ... airline ... en la
                // logica para el modelo"
                // Since it's NOT in the model, we can't use model logic, but we must fill it.
                flight.setAirline("Aeromexico");
            }

        } catch (java.io.IOException e) {
            logger.error("Error reading model file", e);
            // Handle error appropriately, maybe log it
        }
        return flight;
    }
}