package com.flightontime.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un vuelo con datos básicos y la probabilidad de
 * retraso.
 */
@Data
@Entity // Esto le dice a JPA que cree una tabla llamada "Flight"
@AllArgsConstructor
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Agregamos un ID único autoincremental

    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Double delayProbability;

    @Override
    /**
     * Representación en texto del objeto `Flight`.
     *
     * @return cadena con los campos del vuelo
     */
    public String toString() {
        return "Flight [id=" + id + ", flightNumber=" + flightNumber + ", airline=" + airline + ", origin=" + origin
                + ", destination=" + destination + ", departureTime=" + departureTime + ", delayProbability="
                + delayProbability + "]";
    }

}