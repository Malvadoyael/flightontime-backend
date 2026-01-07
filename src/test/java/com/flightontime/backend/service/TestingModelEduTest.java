package com.flightontime.backend.service;

import com.flightontime.backend.model.Flight;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestingModelEduTest {

    @Test
    /**
     * Prueba que `testingModelEdu` rellena `origin`, `destination` y
     * `airline` cuando el archivo de modelo contiene los features esperados.
     */
    public void testTestingModelEdu() {
        FlightService service = new FlightService();
        Flight flight = new Flight();

        // Act
        service.testingModelEdu(flight);

        // Assert
        // Based on our logic: origin="MEX", destination="CUN", airline="Aeromexico"
        // if file is read correctly and features exist.
        // We print values to see what happened if test fails or for log.
        System.out.println("Origin: " + flight.getOrigin());
        System.out.println("Dest: " + flight.getDestination());
        System.out.println("Airline: " + flight.getAirline());

        assertEquals("MEX", flight.getOrigin(), "Origin should be MEX");
        assertEquals("CUN", flight.getDestination(), "Destination should be CUN");
        assertEquals("Aeromexico", flight.getAirline(), "Airline should be Aeromexico");
    }
}
