package com.capstone.agent.legacy.service;
import com.capstone.agent.legacy.entity.Chatlog;
import com.capstone.agent.legacy.repository.ChatlogRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatlogService {
    private final ChatlogRepository chatlogRepository;

    public void saveLog(int userId, String role, String chat) {
        Chatlog log = Chatlog.builder()
                       .userId(userId)
                       .role(role)
                       .chat(chat)
                       .build();
        chatlogRepository.save(log);
    }

    public List<Chatlog> loadLog(int userId) {
        return chatlogRepository.findByUserId(userId);
    }
}
