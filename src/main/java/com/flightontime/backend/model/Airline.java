package com.flightontime.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AIRLINE")
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shortName; // Para el "Nombre Corto" (ej. AA, DL)
    private String fullName;  // Para el "Nombre de Fantasia" (ej. American Airlines)
    private Boolean active;   // Para la columna "active"
}