package com.flightontime.backend.service;

import com.flightontime.backend.model.Flight;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class FlightService {

    /**
     * Este método simula lo que hará tu modelo de Data Science.
     * Por ahora, genera una probabilidad basada en reglas lógicas simples.
     */
    public Flight predictDelay(Flight flight) {
        Random random = new Random();
        double baseProbability = 0.10; // 10% base

        // Regla lógica: Si el vuelo es a "MEX" (CDMX), hay más tráfico, aumenta 15%
        if ("MEX".equalsIgnoreCase(flight.getDestination())) {
            baseProbability += 0.15;
        }

        // Simulamos una variación aleatoria del clima
        double weatherFactor = random.nextDouble() * 0.20;
        
        flight.setDelayProbability(baseProbability + weatherFactor);
        
        return flight;
    }
}