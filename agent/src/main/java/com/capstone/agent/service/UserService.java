package com.capstone.agent.service;

import org.springframework.stereotype.Service;

import com.capstone.agent.entity.User;
import com.capstone.agent.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public void signup(User user) {
        this.userRepository.save(user);
    }

    public int login(User user) {
        return this.userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword()).getId();
    }

    public User getUserInfo(String email) {
        return this.userRepository.findByEmail(email);
    }
}
