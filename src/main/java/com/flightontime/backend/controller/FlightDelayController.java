package com.flightontime.backend.controller;

import com.flightontime.backend.dto.FlightDelayDTO;
import com.flightontime.backend.service.FlightDelayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delays")
public class FlightDelayController {

    private final FlightDelayService flightDelayService;

    public FlightDelayController(FlightDelayService flightDelayService) {
        this.flightDelayService = flightDelayService;
    }

    @GetMapping("/route")
    public ResponseEntity<List<FlightDelayDTO>> getDelaysByRoute(
            @RequestParam String origin,
            @RequestParam String destination) {
        List<FlightDelayDTO> delays = flightDelayService.getDelaysByRoute(origin, destination);
        return ResponseEntity.ok(delays);
    }
}
