package com.capstone.agent.api.agent.service;

import java.util.HashMap;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.capstone.agent.api.agent.dto.QuestionDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService {
    private final WebClient webClient;
    public HashMap<String, String> getAgentQuestionHash(QuestionDTO question) {
        HashMap<String, String> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(5001)
                        .path("/agent/question")
                        .queryParam("query", question.getQuery())
                        .queryParam("loc", question.getLocation())
                        .queryParam("gender", question.getGender())
                        .queryParam("age", question.getAge())
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {})
                .block();
        return response;
    }
}
