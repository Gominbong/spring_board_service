package com.example.myproject.controller;

import com.example.myproject.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final LoginService loginService;

    @GetMapping("/home")
    public String home(){
        return "/home";
    }

}
