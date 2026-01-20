package com.flightontime.backend.service.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightontime.backend.dto.MonthlyDelaySummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class DashboardServiceTest {

    @Test
    public void testGetMonthlyDelaySummaries() throws IOException {
        DashboardService service = new DashboardService(new ObjectMapper());
        List<MonthlyDelaySummary> summaries = service.getMonthlyDelaySummaries(null);

        Assertions.assertNotNull(summaries);
        Assertions.assertFalse(summaries.isEmpty());

        for (MonthlyDelaySummary summary : summaries) {
            System.out.println("Period: " + summary.getPeriodo() +
                    ", Flights: " + summary.getTotalVuelos() +
                    ", Delays: " + summary.getTotalRetrasos());
            Assertions.assertNotNull(summary.getPeriodo());
            Assertions.assertTrue(summary.getTotalVuelos() > 0);
            Assertions.assertTrue(summary.getTotalRetrasos() >= 0);
        }

        // Assert sorting
        // 2024-01 comes before 2024-02
        if (summaries.size() > 1) {
            String p1 = summaries.get(0).getPeriodo();
            String p2 = summaries.get(1).getPeriodo();
            Assertions.assertTrue(p1.compareTo(p2) < 0);
        }
    }
}
