package com.flightontime.backend.controller;

import com.flightontime.backend.dto.TravelGuideRequest;
import com.flightontime.backend.service.guide.TravelGuideService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/travel-guide")
@CrossOrigin(origins = "http://localhost:3000")
public class TravelGuideController {

    private final TravelGuideService travelGuideService;

    public TravelGuideController(TravelGuideService travelGuideService) {
        this.travelGuideService = travelGuideService;
    }

    @PostMapping
    public String getTravelGuide(@RequestBody TravelGuideRequest request) {
        return travelGuideService.generateGuide(request);
    }
}
