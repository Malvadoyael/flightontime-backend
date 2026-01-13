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

    @PostMapping("/test-model")
    /**
     * Endpoint para probar la integración con el modelo XGBoost.
     * Rellena datos del vuelo basado en features del modelo.
     */
    public Flight testModel(@RequestBody Flight flight) {
        return flightService.testingModelEdu(flight);
    }

    @PostMapping("/validate-terna")
    /**
     * Endpoint para validar si el modelo soporta la terna de IDs (Origen, Destino,
     * Aerolinea).
     * Se espera un objeto Flight con ids dummy para mapear a los argumentos, o un
     * objeto custom.
     * Para simplificar, usaremos parametros de request o un objeto wrapper.
     * Aquí usaremos un objeto wrapper interno o map para recibir los IDs.
     */
    public boolean validateTerna(@RequestBody TernaRequest request) {
        return flightService.validateTernaIds(request.originId, request.destId, request.airlineId);
    }

    // Clase auxiliar para el request bodt
    public static class TernaRequest {
        public int originId;
        public int destId;
        public int airlineId;
    }
}