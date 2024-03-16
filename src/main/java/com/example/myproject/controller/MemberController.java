package com.example.myproject.controller;

import com.example.myproject.dto.MemberDto;
import com.example.myproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signForm(Model model) {

        model.addAttribute("memberDto", new MemberDto());
        return "/login/signupForm";
    }

    @GetMapping("/signupComplete")
    public String signForm(){

        return "completeForm";
    }

    @PostMapping("/signup")
    public String signForm(MemberDto memberDto, Model model ) {

        Map<String, String> errors = memberService.createMember(memberDto);

        if (errors != null) {
            model.addAttribute("errors", errors);
            return "/login/signupForm";
        }
        log.info("aaaaaaaaaaaaaaaaa");
        return "redirect:/signupComplete";
    }

    @GetMapping("/login")
    public String login(){
        return "login/loginForm";
    }

    @GetMapping("/myInfo")
    public String myInfo(){
        return "/login/myInfoForm";
    }

    @GetMapping("/cart")
    public String cart(){
        return "/login/cartForm";
    }

}
