package com.example.myproject.controller;

import com.example.myproject.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final LoginService loginService;

}
