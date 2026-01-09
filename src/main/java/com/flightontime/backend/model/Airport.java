package com.flightontime.backend.model; // Ajusta el paquete si es necesario

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AIRPORT")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iata;      // Columna B: IATA (ej: ABE)
    private String name;      // Columna C: Nombre
    private String city;      // Columna D: Ciudad
    private String state;     // Columna E: Estado
    private Double latitude;  // Columna F: Latitud
    private Double longitude; // Columna G: Longitud
}