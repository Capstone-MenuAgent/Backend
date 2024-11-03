package com.capstone.agent.api.agent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.agent.api.agent.dto.WeatherDTO;
import com.capstone.agent.api.agent.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/external")
public class ExternalAPIController {
    private final WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherDTO getWeather() {
        return weatherService.getWeatherForecast();
    }
}
