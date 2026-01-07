package com.flightontime.backend.controller;

import com.flightontime.backend.model.Flight;
import com.flightontime.backend.service.FlightService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StatusController {

    /**
     * Controlador REST para endpoints de predicción y lista de orígenes.
     */

    @Autowired
    private FlightService flightService;

    // Cambiamos a POST para que TÚ le envíes los datos del vuelo
    @PostMapping("/predict")
    /**
     * Recibe un `Flight` en el cuerpo de la petición y devuelve el mismo objeto
     * con la predicción de probabilidad de retraso calculada por
     * {@link com.flightontime.backend.service.FlightService#predictDelay(Flight)}.
     *
     * @param flight objeto `Flight` con datos de entrada
     * @return objeto `Flight` con la propiedad `delayProbability` calculada
     */
    public Flight getPrediction(@RequestBody Flight flight) {
        return flightService.predictDelay(flight);
    }

    @PostMapping("/originList")
    /**
     * Devuelve una lista de vuelos de ejemplo que representan orígenes
     * disponibles.
     *
     * @return lista de `Flight` de ejemplo
     */
    public List<Flight> originList() {
        return flightService.originList();
    }
}