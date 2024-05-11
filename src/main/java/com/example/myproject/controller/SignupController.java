package com.example.myproject.controller;

import com.example.myproject.dto.SignupFormDto;
import com.example.myproject.service.SignupService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @GetMapping("/signup")
    public String sign(Model model) {

        model.addAttribute("signupFormDto", new SignupFormDto());
        return "login/signupForm";
    }

    @PostMapping("/signup")
    public String sign(SignupFormDto signupFormDto, Model model ) {

        Map<String, String> errors = signupService.createMember(signupFormDto);
        if (errors != null) {
            model.addAttribute("errors", errors);
            return "login/signupForm";
        }
        log.info("가입성공");
        return "redirect:/";
    }
}