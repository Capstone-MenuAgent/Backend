package com.capstone.agent.controller;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    @GetMapping("/question")
    @ResponseBody
    public String userQuestion(@RequestParam String query) {
        return "유저의 질문입니다 : " + query;
    }
    
}
