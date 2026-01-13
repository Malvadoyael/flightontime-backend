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
     * Simula un modelo que estima la probabilidad de retraso para un vuelo.
     * Genera una probabilidad base y la ajusta según reglas simples (por
     * ejemplo, destino "MEX" aumenta la probabilidad) y un factor aleatorio.
     *
     * @param flight objeto `Flight` con los datos de entrada (origen,
     *               destino, etc.)
     * @return el mismo `Flight` con `delayProbability` actualizado
     */
    public Flight predictDelay(Flight flight) {
        logger.info("Calculando prediccion para el vuelo: {}", flight.getFlightNumber());
        Random random = new Random();
        double baseProbability = 0.10; // 10% base

        // Regla lógica: Si el vuelo es a "MEX" (CDMX), hay más tráfico, aumenta 15%
        // Asumimos que ID 1 es MEX
        if (flight.getDestination() != null && flight.getDestination() == 1) {
            baseProbability += 0.15;
        }

        // Simulamos una variación aleatoria del clima
        double weatherFactor = random.nextDouble() * 0.20;

        flight.setDelayProbability(baseProbability + weatherFactor);

        logger.info("Resultado de la prediccion: {}", flight);
        return flight;
    }

    /**
     * Devuelve una lista de vuelos de ejemplo utilizada por la API.
     * Los vuelos son instancias creadas en memoria para propósitos de demo.
     *
     * @return lista de vuelos de ejemplo
     */
    public List<Flight> originList() {
        List<Flight> flights = new ArrayList<>();
        // IDs arbitrarios para demo:
        // MEX=1, JFK=2, IAH=3, CUN=4, MAD=5
        // Aeromexico=1, United=2, Iberia=3
        flights.add(new Flight(1L, "AM123", 1, 1, 2, LocalDateTime.now().plusHours(4), 0.1));
        flights.add(new Flight(2L, "UA456", 2, 3, 4, LocalDateTime.now().plusHours(2), 0.2));
        flights.add(new Flight(3L, "IB789", 3, 5, 1, LocalDateTime.now().plusHours(10), 0.05));
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
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Load the resource using getInputStream for JAR compatibility
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(
                    "modelo_vuelos.json");

            com.flightontime.backend.model.xgboost.XGBoostModel model = mapper.readValue(resource.getInputStream(),
                    com.flightontime.backend.model.xgboost.XGBoostModel.class);

            if (model != null && model.getLearner() != null) {
                java.util.List<String> featureNames = model.getLearner().getFeatureNames();
                logger.info("Modelo cargado correctamente. Features encontradas: {}", featureNames);

                boolean hasOrigin = featureNames.contains("origin");
                boolean hasDest = featureNames.contains("dest");

                // Populate logic based on model features
                if (hasOrigin) {
                    flight.setOrigin(1); // Asignamos ID 1 (MEX)
                }
                if (hasDest) {
                    flight.setDestination(4); // Asignamos ID 4 (CUN)
                }

                // Example of inspecting tree structure (proving depth of integration)
                if (model.getLearner().getGradientBooster() != null &&
                        model.getLearner().getGradientBooster().getModel() != null &&
                        !model.getLearner().getGradientBooster().getModel().getTrees().isEmpty()) {
                    logger.info("El modelo contiene {} árboles de decisión.",
                            model.getLearner().getGradientBooster().getModel().getTrees().size());
                }

                // Explicit logic as requested before
                flight.setAirline(1); // Asignamos ID 1 (Aeromexico)
            }

        } catch (java.io.IOException e) {
            logger.error("Error al leer el archivo del modelo", e);
        }
        return flight;
    }

    /**
     * Valida si el modelo tiene las features necesarias para soportar la terna de
     * IDs:
     * Origen, Destino y Aerolinea.
     * 
     * @param originId  ID numérico del origen
     * @param destId    ID numérico del destino
     * @param airlineId ID numérico de la aerolínea (op_unique_carrier)
     * @return true si el modelo contiene las features correspondientes
     */
    public boolean validateTernaIds(int originId, int destId, int airlineId) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource(
                    "modelo_vuelos.json");

            com.flightontime.backend.model.xgboost.XGBoostModel model = mapper.readValue(resource.getInputStream(),
                    com.flightontime.backend.model.xgboost.XGBoostModel.class);

            if (model != null && model.getLearner() != null) {
                java.util.List<String> features = model.getLearner().getFeatureNames();
                boolean hasOrigin = features.contains("origin");
                boolean hasDest = features.contains("dest");
                boolean hasAirline = features.contains("op_unique_carrier");

                logger.info("Validando Terna con IDs: OrigenID={}, DestID={}, AerolineaID={}", originId, destId,
                        airlineId);
                logger.info("Resultados en modelo: Origin={}, Dest={}, Airline(op_unique_carrier)={}", hasOrigin,
                        hasDest, hasAirline);

                return hasOrigin && hasDest && hasAirline;
            }
        } catch (Exception e) {
            logger.error("Error validando terna IDs", e);
        }
        return false;
    }
}