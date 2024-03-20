package com.example.myproject.controller;

import com.example.myproject.domain.member.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final LoginService loginService;

    @GetMapping("/")
    public String login(@CookieValue(name = "loginId", required = false) String id, Model model){
        model.addAttribute("cookieLoginId", id);
        log.info("cookieLoginId = {}", id);
        return "home";
    }

}
