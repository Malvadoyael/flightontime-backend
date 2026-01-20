package com.flightontime.backend.service.dashboard;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightontime.backend.dto.MonthlyDelaySummary;
import com.flightontime.backend.model.DelayStat;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

        private final ObjectMapper objectMapper;

        public DashboardService(ObjectMapper objectMapper) {
                this.objectMapper = objectMapper;
        }

        public List<MonthlyDelaySummary> getMonthlyDelaySummaries(Integer opUniqueCarrier) throws IOException {
                ClassPathResource resource = new ClassPathResource("retrasos_aerolineas_3.json");
                InputStream inputStream = resource.getInputStream();

                List<DelayStat> stats = objectMapper.readValue(inputStream, new TypeReference<List<DelayStat>>() {
                });

                // If a carrier ID is provided, filter the stats first
                if (opUniqueCarrier != null) {
                        return stats.stream()
                                        .filter(stat -> opUniqueCarrier.equals(stat.getOpUniqueCarrier()))
                                        .map(stat -> new MonthlyDelaySummary(stat.getPeriodo(), stat.getTotalVuelos(),
                                                        stat.getTotalRetrasos()))
                                        .sorted(Comparator.comparing(MonthlyDelaySummary::getPeriodo))
                                        .collect(Collectors.toList());
                }

                // Otherwise, aggregate global stats by month
                Map<String, MonthlyDelaySummary> aggregatedMap = stats.stream()
                                .collect(Collectors.groupingBy(
                                                DelayStat::getPeriodo,
                                                Collectors.reducing(
                                                                new MonthlyDelaySummary("", 0, 0),
                                                                stat -> new MonthlyDelaySummary(stat.getPeriodo(),
                                                                                stat.getTotalVuelos(),
                                                                                stat.getTotalRetrasos()),
                                                                (s1, s2) -> {
                                                                        // Use the period from either one (assuming they
                                                                        // match per grouping)
                                                                        String periodo = s1.getPeriodo().isEmpty()
                                                                                        ? s2.getPeriodo()
                                                                                        : s1.getPeriodo();
                                                                        return new MonthlyDelaySummary(periodo,
                                                                                        s1.getTotalVuelos() + s2
                                                                                                        .getTotalVuelos(),
                                                                                        s1.getTotalRetrasos() + s2
                                                                                                        .getTotalRetrasos());
                                                                })));

                return aggregatedMap.values().stream()
                                .sorted(Comparator.comparing(MonthlyDelaySummary::getPeriodo))
                                .collect(Collectors.toList());
        }
}
