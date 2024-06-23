package com.capstone.agent.controller;
import com.capstone.agent.dto.LoginDTO;
import com.capstone.agent.dto.SignupDTO;
import com.capstone.agent.entity.Chatlog;
import com.capstone.agent.dto.InfoDTO;
import com.capstone.agent.dto.LogDTO;
import com.capstone.agent.service.ChatlogService;
import com.capstone.agent.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

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
    private final ChatlogService chatlogService;

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
    public List<LogDTO> getUserHistory(@RequestParam int userId) {
        List<Chatlog> chatlogs = chatlogService.loadLog(userId);
        List<LogDTO> logs = chatlogs.stream()
                                    .map(log -> LogDTO.builder()
                                                      .role(log.getRole())
                                                      .chat(log.getChat())
                                                      .created_at(log.getCreated_at())
                                                      .build())
                                    .collect(Collectors.toList());
        return logs;
    }
}
