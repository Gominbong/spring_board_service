package com.example.myproject.controller;

import com.example.myproject.dto.SignupFormDto;
import com.example.myproject.service.SignupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @GetMapping("/signup")
    public String signForm(Model model) {

        model.addAttribute("signupFormDto", new SignupFormDto());
        return "/login/signupForm";
    }

    @GetMapping("/signupComplete")
    public String signForm(){

        return "/login/completeForm";
    }

    @PostMapping("/signup")
    public String signForm(SignupFormDto memberDto, Model model ) {

        Map<String, String> errors = signupService.createMember(memberDto);

        if (errors != null) {
            model.addAttribute("errors", errors);
            return "/login/signupForm";
        }
        log.info("aaaaaaaaaaaaaaaaa");
        return "redirect:/signupComplete";
    }
}
