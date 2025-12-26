package com.flightontime.backend.repository; // Asegúrate de que termine en .repository

import com.flightontime.backend.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
}