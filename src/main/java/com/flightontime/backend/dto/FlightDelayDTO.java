package com.flightontime.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO (Data Transfer Object) que representa los datos de retraso de un vuelo.
 * Utilizado para transferir información de retrasos entre capas de la aplicación.
 */
@Data
public class FlightDelayDTO {
    /**
     * Código de origen y destino en formato "ORIGEN-DESTINO".
     */
    @JsonProperty("origen_destino")
    private String origenDestino;

    /**
     * Mes del retraso (1-12).
     */
    @JsonProperty("mes")
    private Integer mes;

    /**
     * Nombre de la aerolínea.
     */
    @JsonProperty("aerolinea")
    private String aerolinea;

    /**
     * Tiempo de retraso en minutos.
     */
    @JsonProperty("tiempo")
    private Double tiempo;
}
