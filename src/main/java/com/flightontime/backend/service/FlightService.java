package com.flightontime.backend.service;

import com.flightontime.backend.model.Flight;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

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

    public List<Flight> originList() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight(1L, "AM123", "Aeroméxico", "MEX", "JFK", LocalDateTime.now().plusHours(4), 0.1));
        flights.add(new Flight(2L, "UA456", "United", "IAH", "CUN", LocalDateTime.now().plusHours(2), 0.2));
        flights.add(new Flight(3L, "IB789", "Iberia", "MAD", "MEX", LocalDateTime.now().plusHours(10), 0.05));
        return flights;
    }
}