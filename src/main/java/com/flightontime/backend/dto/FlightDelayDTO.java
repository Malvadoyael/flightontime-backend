package com.flightontime.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlightDelayDTO {
    @JsonProperty("origen_destino")
    private String origenDestino;

    @JsonProperty("mes")
    private Integer mes;

    @JsonProperty("aerolinea")
    private String aerolinea;

    @JsonProperty("tiempo")
    private Double tiempo;
}
