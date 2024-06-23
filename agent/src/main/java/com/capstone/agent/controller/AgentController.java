package com.capstone.agent.controller;
import com.capstone.agent.service.ChatlogService;
import com.capstone.agent.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import com.capstone.agent.dto.InfoDTO;

import java.net.URI;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AgentController {
    private final UserService userService;
    private final ChatlogService chatlogService;
    
    @GetMapping("agent/question")
    @ResponseBody
    public ResponseEntity<?> userQuestion(@RequestParam String query, HttpServletRequest request) {
        int userId = 1;
        InfoDTO info = userService.getUserInfo(userId);
        this.chatlogService.saveLog(userId, "human", query);
        try {
            HttpHeaders headers = new HttpHeaders();
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            int age = info.getAge();
            String encodedGender = URLEncoder.encode(info.getGender(), "UTF-8");
            String encodedAddr = URLEncoder.encode(info.getAddr(), "UTF-8");
            String encodedParams = String.format("?query=%s&age=%d&gender=%s&loc=%s", encodedQuery, age, encodedGender, encodedAddr);
            String agentPort = "5000";
            String newUrl = String.format("%s://%s:%s/question/%s",
                                           request.getScheme(),
                                           request.getServerName(),
                                           agentPort,
                                           encodedParams );
            headers.setLocation(URI.create(newUrl));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("agent/answer")
    public String agentAnswer(@RequestParam String ans) {
        int userId = 1;
        this.chatlogService.saveLog(userId, "agent", ans);
        return ans;
    }
}
