package com.example.myproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {


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