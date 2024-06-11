package com.capstone.agent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AgentController {
    @GetMapping("/main")
    @ResponseBody
    public String mainPage() {
        return "대화 내용 반환 예정";
    }
}
