package com.flightontime.backend.controller;

import com.flightontime.backend.dto.MonthlyDelaySummary;
import com.flightontime.backend.service.dashboard.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @Test
    public void testGetDelaysByMonth() throws Exception {
        // Arrange
        MonthlyDelaySummary summary1 = new MonthlyDelaySummary("2024-01", 100, 10);
        MonthlyDelaySummary summary2 = new MonthlyDelaySummary("2024-02", 150, 20);
        List<MonthlyDelaySummary> mockSummaries = Arrays.asList(summary1, summary2);

        given(dashboardService.getMonthlyDelaySummaries(null)).willReturn(mockSummaries);

        // Act & Assert
        mockMvc.perform(get("/api/v1/dashboard/delays-by-month")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].periodo").value("2024-01"))
                .andExpect(jsonPath("$[0].totalVuelos").value(100))
                .andExpect(jsonPath("$[0].totalRetrasos").value(10))
                .andExpect(jsonPath("$[0].porcentaje").value(10.0))
                .andExpect(jsonPath("$[1].periodo").value("2024-02"))
                .andExpect(jsonPath("$[1].totalVuelos").value(150))
                .andExpect(jsonPath("$[1].totalRetrasos").value(20))
                .andExpect(jsonPath("$[1].porcentaje").value(13.333333333333334));
    }
}
