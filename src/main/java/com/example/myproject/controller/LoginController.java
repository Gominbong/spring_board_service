package com.example.myproject.controller;

import com.example.myproject.domain.member.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/myInfo")
    public String myInfo() {
        return "/login/myInfoForm";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginFormDto") LoginFormDto loginFormDto) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String loginSession(@Valid @ModelAttribute LoginFormDto loginFormDto,
                          BindingResult bindingResult,
                          HttpServletRequest request) {
        List<Member> result = loginService.login(loginFormDto);

        log.info("암호화된비밀번호가져오기= {}", result);

        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }
        if (result == null) {
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "/login/loginForm";
        }

        String id = request.getParameter("id");
        log.info("loginId= = {} ", id);
        HttpSession session = request.getSession();
        session.setAttribute("memberId", loginFormDto.getId());
        String memberId = (String)session.getAttribute("memberId");
        log.info("세션로그인한아이디 = {} ", memberId);
        log.info("세션로그인 완료   =   {}", session);
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        loginService.expireCookie(response, "loginId");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if ( session != null) {
            session.invalidate();
            log.info("세션 로그아웃 되었습니다");
        }
        return "redirect:/";
    }
}
