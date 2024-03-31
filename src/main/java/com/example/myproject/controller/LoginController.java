package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.service.MusicListService;
import com.example.myproject.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private final MusicListService addItemService;
    @GetMapping("/myInfo")
    public String myInfo(HttpServletRequest request, Model model) {


        HttpSession session = request.getSession(false);
        if (session != null){
            model.addAttribute("loginId", session.getAttribute("loginId"));
        }

        return "/login/myInfoForm";
    }

    @GetMapping("/login")
    public String loginForm( Model model) {

        model.addAttribute("loginFormDto",new LoginFormDto());
        return "login/loginForm";
    }

    @GetMapping("/")
    public String loginSession(HttpServletRequest request, Model model){

        model.addAttribute("menu","list");
        HttpSession session = request.getSession(false);
        if (session == null) {
            List<MusicList> itemList = addItemService.findItemList();
            model.addAttribute("itemList", itemList);
            return "home";
        }
        model.addAttribute("loginId", session.getAttribute("loginId"));
        Object id = session.getAttribute("id");
        List<MusicList> itemList = addItemService.findItemList();

        model.addAttribute("itemList", itemList);

        return "home";
    }

    @PostMapping("/login")
    public String loginSession(@Valid LoginFormDto loginFormDto,
                               BindingResult bindingResult, HttpServletRequest request) {
        Member result = loginService.login(loginFormDto);

        log.info("암호화된비밀번호가져오기= {}", result);
        if (bindingResult.hasErrors()) {
            return "/login/loginForm";
        }
        if (result == null) {
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "/login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginId", loginFormDto.getId());


        log.info("세션 로그인 아이디 = {}", loginFormDto.getId());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutSession(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("세션 로그아웃 되었습니다");
        }

        return "redirect:/";
    }
}