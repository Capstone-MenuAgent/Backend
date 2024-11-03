package com.capstone.agent.api.agent.dto;

import java.util.HashMap;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class WeatherDTO {
    private HashMap<String, HashMap<String, String>> weathers;
}
