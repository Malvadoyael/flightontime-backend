package com.flightontime.backend.service;

import com.flightontime.backend.model.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FlightServiceTest {

    @Autowired
    private FlightService flightService;

    @Test
    public void testModelIntegrationWithVariables() {
        // 1. Crear un vuelo con valores iniciales
        Flight inputFlight = new Flight();
        inputFlight.setFlightNumber("TEST-100");
        // No seteamos origin/dest aún para ver si el servicio los rellena basado en la
        // lógica del modelo

        // 2. Ejecutar el servicio que carga el modelo
        System.out.println("Iniciando prueba de integración con variables...");
        Flight result = flightService.testingModelEdu(inputFlight);

        // 3. Verificaciones de "Revisión con variables"

        // Verificamos que el servicio haya detectado la feature "origin" en el modelo y
        // aplicado la lógica "MEX"
        // Si el modelo NO se hubiera cargado, esto seguiría siendo null.
        assertNotNull(result.getOrigin(),
                "El origen debería haber sido rellenado si el modelo se cargó correctamente y contiene 'origin'");
        assertEquals("MEX", result.getOrigin());

        // Verificamos "dest" -> "CUN"
        assertEquals("CUN", result.getDestination());

        // Verificamos "airline" -> "Aeromexico" (lógica explícita)
        assertEquals("Aeromexico", result.getAirline());

        System.out.println(
                "PRUEBA EXITOSA: El modelo se cargó y las variables del vuelo fueron procesadas según las features del modelo.");
        System.out.println("Vuelo resultante: " + result);
    }

    @Test
    public void testValidateTernaIds() {
        System.out.println("Iniciando validación de terna con IDs...");
        // Usamos IDs arbitrarios (ej. 1, 2, 3) solo para verificar que el método
        // procesa la solicitud
        // y chequea las features en el modelo.
        boolean isValid = flightService.validateTernaIds(101, 202, 303);

        // Debe ser true porque el modelo contiene 'origin', 'dest' y
        // 'op_unique_carrier'
        if (isValid) {
            System.out.println("EXITO: El modelo valida correctamente la existencia de features para la terna de IDs.");
        } else {
            System.out.println("FALLO: El modelo reportó falta de features para la terna.");
        }
        assertEquals(true, isValid, "El modelo debería soportar la terna de IDs (feature names).");
    }
}
