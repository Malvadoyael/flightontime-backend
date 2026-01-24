package com.flightontime.backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightontime.backend.dto.FlightDelayDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar los datos de retrasos de vuelos.
 * Carga datos desde un archivo JSON y proporciona consultas por ruta.
 */
@Service
public class FlightDelayService {

    private final ObjectMapper objectMapper;
    private final Map<String, List<FlightDelayDTO>> delayMap = new HashMap<>();

    /**
     * Constructor que inyecta el ObjectMapper para el manejo de JSON.
     *
     * @param objectMapper Instancia de ObjectMapper para deserializar JSON.
     */
    public FlightDelayService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Método de inicialización que carga los datos de retrasos desde el archivo JSON.
     * Se ejecuta después de la construcción del bean.
     *
     * @throws RuntimeException Si falla la carga del archivo JSON.
     */
    @PostConstruct
    public void loadData() {
        try {
            ClassPathResource resource = new ClassPathResource("retrasos_lista_plana.json");
            InputStream inputStream = resource.getInputStream();
            List<FlightDelayDTO> delays = objectMapper.readValue(inputStream,
                    new TypeReference<List<FlightDelayDTO>>() {
                    });

            for (FlightDelayDTO delay : delays) {
                delayMap.computeIfAbsent(delay.getOrigenDestino(), k -> new ArrayList<>()).add(delay);
            }

            System.out.println("Loaded " + delays.size() + " delay records into memory.");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load delay data from JSON file", e);
        }
    }

    /**
     * Obtiene la lista de retrasos para una ruta específica.
     *
     * @param origin Código del aeropuerto de origen.
     * @param destination Código del aeropuerto de destino.
     * @return Lista de DTOs de retrasos para la ruta, o lista vacía si no se encuentra.
     */
    public List<FlightDelayDTO> getDelaysByRoute(String origin, String destination) {
        String key = origin + "-" + destination;
        return delayMap.getOrDefault(key, new ArrayList<>());
    }
}
