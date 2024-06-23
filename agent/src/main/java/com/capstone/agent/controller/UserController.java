package com.capstone.agent.controller;

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
    @PostMapping("/login")
    @ResponseBody
    public String userLogin(@RequestBody String entity) {
        return String.format("로그인 정보 확인 %s", entity);
    }

    @PostMapping("/signup")
    @ResponseBody
    public String userSignup(@RequestBody String entity) {
        return entity;
    }

    @GetMapping("/information")
    @ResponseBody
    public String getUserInformation(@RequestParam String userID) {
        return String.format("회원 정보 확인 %s", userID);
    }

    @GetMapping("/history")
    @ResponseBody
    public String getUserHistory(@RequestParam(name="st") String start, @RequestParam(name = "ed") String end) {
        return String.format("유저 사용 기록 시작 : %s 종료 : %s", start, end);
    }
}
