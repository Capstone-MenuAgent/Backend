package com.capstone.agent.api.agent.controller;

import com.capstone.agent.api.member.dto.MemberInfoResponseDTO;
import com.capstone.agent.api.member.jwt.service.JwtService;
import com.capstone.agent.api.member.service.MemberService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class AgentController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @GetMapping("/question")
    @ResponseBody
    public ResponseEntity<?> userQeustion(@RequestParam String query, HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Access Token"));
        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("이메일 추출 실패"));

        MemberInfoResponseDTO memberInfo = memberService.memberInfo(email);
        log.info("User ID: {}, Role: {}, Query: {}", memberInfo.getId(), memberInfo.getRole(), query);
        try {
            HttpHeaders headers = new HttpHeaders();
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String encodedAddr = URLEncoder.encode(memberInfo.getAddr(), "UTF-8");
            String encodedGender = URLEncoder.encode(memberInfo.getGender().toString(), "UTF-8");
            int age = memberInfo.getAge();
            String encodedParams = String.format("?query=%s&loc=%s&gender=%s&age=%d", encodedQuery, encodedAddr, encodedGender, age);
            String agentPort = "5000";
            String newURL = String.format(
                    "%s://%s:%s/question/%s",
                    request.getScheme(),
                    request.getServerName(),
                    agentPort,
                    encodedParams );
            headers.setLocation(URI.create(newURL));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        } catch (UnsupportedEncodingException e) {
            log.error(email, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/answer")
    public Map<String, String> agnetAnswer(@RequestBody Map<String, String> json, HttpServletRequest request) {
        log.info("User ID: {}, Role: Agent, Query: {}", json.get("id"), json.get("answer"));

        return json;
    }
}