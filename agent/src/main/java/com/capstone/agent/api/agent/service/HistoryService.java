package com.capstone.agent.api.agent.service;
import com.capstone.agent.api.agent.dto.ChatLogDTO;
import com.capstone.agent.api.agent.entity.History;
import com.capstone.agent.api.agent.repository.HistoryRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    @Transactional
    public void saveLog(ChatLogDTO chatLogDTO) {
        History history = History.builder()
                .memberId(chatLogDTO.getMemberId())
                .chatLog(chatLogDTO.getLog())
                .role(chatLogDTO.getRole())
                .build();
        historyRepository.save(history);
    }

    public List<History> loadHistory(Long memberId) {
        historyRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
        return historyRepository.findAllByMemberId(memberId);
    }
}
