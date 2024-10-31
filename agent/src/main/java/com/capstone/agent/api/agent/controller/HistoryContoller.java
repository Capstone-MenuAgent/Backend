package com.capstone.agent.api.agent.controller;
import com.capstone.agent.api.agent.service.HistoryService;
import com.capstone.agent.api.agent.dto.HistoryRequestDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class HistoryContoller {
    private final HistoryService historyService;

    @GetMapping("/info")
    public HistoryRequestDTO loadHistory(@RequestParam Long userId) {

        HistoryRequestDTO historyRequest = HistoryRequestDTO.builder()
                .memberId(userId)
                .historyData(historyService.loadHistory(userId))
                .build();
        
        return historyRequest;
    }
    
}
