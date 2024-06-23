package com.capstone.agent.service;

import org.springframework.stereotype.Service;

import com.capstone.agent.entity.User;
import com.capstone.agent.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(User user) {
        userRepository.save(user);
    }

    public int login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).getId();
    }

    public User getUserInfo(String email) {
        return userRepository.findByEmail(email);
    }
}
