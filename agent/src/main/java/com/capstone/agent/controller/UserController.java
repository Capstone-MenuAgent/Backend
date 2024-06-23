package com.capstone.agent.controller;
import com.capstone.agent.dto.LoginDTO;
import com.capstone.agent.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    @ResponseBody
    public String userLogin(@RequestBody LoginDTO entity) {
        return String.format("로그인 정보 확인 %d", userService.login(entity.getUserEmail(), entity.getUserPassword()));
    }

    @PostMapping("/signup")
    @ResponseBody
    public String userSignup(@RequestBody String entity) {
        return entity;
    }

    @GetMapping("/information")
    @ResponseBody
    public String getUserInformation(@RequestParam String userId) {
        return String.format("회원 정보 확인 %s", userId);
    }

    @GetMapping("/chatlog")
    @ResponseBody
    public String getUserHistory(@RequestParam String userId) {
        return String.format("유저 사용 기록 %s", userId);
    }
}
