package com.capstone.agent.service;
import com.capstone.agent.dto.LoginDTO;
import com.capstone.agent.dto.SignupDTO;

import org.springframework.stereotype.Service;

import com.capstone.agent.entity.User;
import com.capstone.agent.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignupDTO entity) {
        User newUser = User.builder()
                            .email(entity.getUserEmail())
                            .password(entity.getUserPassword())
                            .name(entity.getName())
                            .age(entity.getAge())
                            .gender(entity.getGender())
                            .addr(entity.getAddr())
                            .build();

        userRepository.save(newUser);
    }

    public int login(LoginDTO entity) {
        User user = userRepository.findByEmailAndPassword(entity.getUserEmail(), entity.getUserPassword());
        if (user == null) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다");
        }
        return user.getId();
    }

    public User getUserInfo(String email) {
        return userRepository.findByEmail(email);
    }
}
