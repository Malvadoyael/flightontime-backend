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

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

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
