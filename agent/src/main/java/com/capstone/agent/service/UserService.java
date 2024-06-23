package com.capstone.agent.service;
import org.springframework.stereotype.Service;

import com.capstone.agent.dto.InfoDTO;
import com.capstone.agent.dto.LoginDTO;
import com.capstone.agent.dto.SignupDTO;
import com.capstone.agent.entity.User;
import com.capstone.agent.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignupDTO entity) {
        User newUser = User.builder()
                            .email(entity.getEmail())
                            .password(entity.getPassword())
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

    public InfoDTO getUserInfo(int id) {
        User user = userRepository.findById(id);
        
        InfoDTO info = InfoDTO.builder()
                               .email(user.getEmail())
                               .password(user.getPassword())
                               .name(user.getName())
                               .age(user.getAge())
                               .gender(user.getGender())
                               .addr(user.getAddr())
                               .build();
        return info;
    }
}
