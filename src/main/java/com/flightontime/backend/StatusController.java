package com.flightontime.backend;

import com.flightontime.backend.model.Flight;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class StatusController {

    @GetMapping("/status")
    public String checkStatus() {
        return "✈️ FlightOnTime Backend: ¡Sistemas listos y despegando!";
    }

    @GetMapping("/test-flight")
    public Flight getTestFlight() {
        return new Flight(
            "MX123", 
            "Aeromexico", 
            "MEX", 
            "CUN", 
            LocalDateTime.now(), 
            0.15
        );
    }
}