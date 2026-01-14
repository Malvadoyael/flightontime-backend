package com.flightontime.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "flight_match")
public class FlightMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer aerolineaId;
    private Integer origenId;
    private Integer destinoId;

    // Constructor vacío (obligatorio para JPA)
    public FlightMatch() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getAerolineaId() { return aerolineaId; }
    public void setAerolineaId(Integer aerolineaId) { this.aerolineaId = aerolineaId; }

    public Integer getOrigenId() { return origenId; }
    public void setOrigenId(Integer origenId) { this.origenId = origenId; }

    public Integer getDestinoId() { return destinoId; }
    public void setDestinoId(Integer destinoId) { this.destinoId = destinoId; }
}