package com.flightontime.backend.controller;

import com.flightontime.backend.model.weather.WeatherResponse;
import com.flightontime.backend.model.weather.WeatherRequest;
import com.flightontime.backend.service.weather.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping("/print")
    public WeatherResponse printWeather(@RequestBody WeatherRequest weatherRequest) {
        return weatherService.processWeather(weatherRequest);
    }
}
