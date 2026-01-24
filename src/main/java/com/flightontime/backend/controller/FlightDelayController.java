package com.flightontime.backend.controller;

import com.flightontime.backend.dto.FlightDelayDTO;
import com.flightontime.backend.service.FlightDelayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para gestionar consultas de retrasos de vuelos.
 * Proporciona endpoints para obtener retrasos por ruta específica.
 */
@RestController
@RequestMapping("/api/v1/delays")
public class FlightDelayController {

    private final FlightDelayService flightDelayService;

    /**
     * Constructor que inyecta el servicio de retrasos de vuelos.
     *
     * @param flightDelayService Servicio para manejar la lógica de retrasos.
     */
    public FlightDelayController(FlightDelayService flightDelayService) {
        this.flightDelayService = flightDelayService;
    }

    /**
     * Obtiene los retrasos de vuelos para una ruta específica definida por origen y destino.
     *
     * @param origin Código del aeropuerto de origen.
     * @param destination Código del aeropuerto de destino.
     * @return Lista de DTOs de retrasos para la ruta especificada.
     */
    @GetMapping("/route")
    public ResponseEntity<List<FlightDelayDTO>> getDelaysByRoute(
            @RequestParam String origin,
            @RequestParam String destination) {
        List<FlightDelayDTO> delays = flightDelayService.getDelaysByRoute(origin, destination);
        return ResponseEntity.ok(delays);
    }
}
