package com.capstone.agent.controller;
import com.capstone.agent.dto.LoginDTO;
import com.capstone.agent.dto.SignupDTO;
import com.capstone.agent.dto.InfoDTO;
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
    public int userLogin(@RequestBody LoginDTO entity) {
        userService.login(entity);
        return 200;
    }

    @PostMapping("/signup")
    @ResponseBody
    public int userSignup(@RequestBody SignupDTO entity) {
        userService.signup(entity);
        return 200;
    }

    @GetMapping("/information")
    @ResponseBody
    public InfoDTO getUserInformation(@RequestParam int userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/chatlog")
    @ResponseBody
    public String getUserHistory(@RequestParam String userId) {
        return String.format("유저 사용 기록 %s", userId);
    }
}
