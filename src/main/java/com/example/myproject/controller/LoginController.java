package com.example.myproject.controller;

import com.example.myproject.domain.member.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    @GetMapping("/myInfo")
    public String myInfo(){
        return "/login/myInfoForm";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginFormDto") LoginFormDto loginFormDto){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginFormDto loginFormDto,
                        BindingResult bindingResult,
                        HttpServletResponse response){

        List<Member> result = loginService.login(loginFormDto);

        if(bindingResult.hasErrors()){
            return "/login/loginForm";
        }
        if (result == null){
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "/login/loginForm";
        }

        Cookie idCookie = new Cookie("loginId", loginFormDto.getId());
        response.addCookie(idCookie);

        log.info("로그인 완료");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        loginService.expireCookie(response, "loginId");
        return "redirect:/";
    }

}
