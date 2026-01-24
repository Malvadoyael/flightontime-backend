package com.flightontime.backend.controller;

import com.flightontime.backend.dto.MonthlyDelaySummary;
import com.flightontime.backend.service.dashboard.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Controlador REST para el dashboard de la aplicación FlightOnTime.
 * Proporciona endpoints para obtener resúmenes de retrasos mensuales.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Constructor que inyecta el servicio de dashboard.
     *
     * @param dashboardService Servicio para manejar la lógica del dashboard.
     */
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Obtiene los resúmenes de retrasos por mes.
     * Opcionalmente filtra por código único de aerolínea.
     *
     * @param opUniqueCarrier Código único de la aerolínea (opcional).
     * @return Lista de resúmenes mensuales de retrasos.
     */
    @GetMapping("/delays-by-month")
    public ResponseEntity<List<MonthlyDelaySummary>> getDelaysByMonth(
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer opUniqueCarrier) {
        try {
            List<MonthlyDelaySummary> summaries = dashboardService.getMonthlyDelaySummaries(opUniqueCarrier);
            return ResponseEntity.ok(summaries);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
