package com.example.myproject.controller;

import com.example.myproject.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    //@GetMapping("/")
    public String login(@CookieValue(name = "loginId", required = false) String id, Model model){
        model.addAttribute("cookieLoginId", id);
        log.info("cookieLoginId = {}", id);
        return "home";
    }

    @GetMapping("/")
    public String loginV3(HttpServletRequest request, Model model){

        HttpSession session = request.getSession(false);
        if (session != null){
            String memberId = (String)session.getAttribute("memberId");
            model.addAttribute("loginId", memberId);
            log.info("세션로그인한아이디 재확인  = {} ", memberId);
        }
        return "home";

    }
}
