package com.capstone.agent.api.agent.controller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.agent.api.agent.dto.ChatLogDTO;
import com.capstone.agent.api.agent.dto.QuestionDTO;
import com.capstone.agent.api.agent.entity.Role;
import com.capstone.agent.api.agent.service.AgentService;
import com.capstone.agent.api.agent.service.HistoryService;
import com.capstone.agent.api.member.dto.MemberInfoDTO;
import com.capstone.agent.api.member.jwt.service.JwtService;
import com.capstone.agent.api.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
@CrossOrigin(origins = "*", methods = RequestMethod.GET)
public class AgentController {

    private final MemberService memberService;
    private final HistoryService historyService;
    private final JwtService jwtService;
    private final AgentService agentService;

    @GetMapping("/question")
    @ResponseBody
    public HashMap<String, String> userQeustionTest(@RequestParam String query, HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Access Token"));
        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("이메일 추출 실패"));

        MemberInfoDTO memberInfo = memberService.memberInfo(email);

        log.info("User ID: {}, Role: {}, Query: {}", memberInfo.getId(), memberInfo.getRole(), query);

        // question log 저장
        ChatLogDTO userChatLogDTO = ChatLogDTO.builder()
                .memberId(memberInfo.getId())
                .log(query)
                .role(Role.HUMAN)
                .build();
        historyService.saveLog(userChatLogDTO);

        try {
            QuestionDTO question = QuestionDTO.builder()
                    .query(query)
                    .location(memberInfo.getAddr())
                    .gender(memberInfo.getGender().toString())
                    .age(memberInfo.getAge())
                    .build();
            HashMap<String, String> answer = agentService.getAgentQuestionHash(question);

        // question log 저장
        ChatLogDTO agentChatLogDTO = ChatLogDTO.builder()
                .memberId(memberInfo.getId())
                .log(answer.get("answer"))
                .role(Role.COMPUTER)
                .build();
        historyService.saveLog(agentChatLogDTO);
            
            return answer;
        } catch (Exception e) {
            log.error(email, e);
            return new HashMap<>();
        }
    }
}