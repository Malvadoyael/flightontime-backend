package com.flightontime.backend;

import com.flightontime.backend.model.Flight;
import com.flightontime.backend.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StatusController {

    @Autowired
    private FlightService flightService;

    // Cambiamos a POST para que TÚ le envíes los datos del vuelo
    @PostMapping("/predict")
    public Flight getPrediction(@RequestBody Flight flight) {
        return flightService.predictDelay(flight);
    }
}