package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.service.MusicListService;
import com.example.myproject.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static com.example.myproject.controller.LoginMember.loginMember;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final MusicListService musicListService;

    @GetMapping("/")
    public String home(@RequestParam(value = "page", defaultValue = "0") int page,
                       HttpServletRequest request, Model model) {
        model.addAttribute("menu", "home");
        HttpSession session = request.getSession(false);

        if (session != null) {
            String loginId = (String) session.getAttribute("loginId");
            model.addAttribute("loginId", loginId);
        }

        for (String s : loginMember.keySet()) {
            log.info("해시맵 확인 = {}", s);
        }
        Page<MusicList> paging = musicListService.findMusicList(page);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        musicListService.pageStartEndNumber(page, paging, model);

        return "home";
    }

    @GetMapping("/loginInterceptor")
    public String loginInterceptor(Model model) {

        model.addAttribute("loginFormDto", new LoginFormDto());
        return "login/loginForm";
    }

    @PostMapping("/loginInterceptor")
    public String loginInterceptor(@CookieValue("url") String url, @Valid LoginFormDto loginFormDto,
                               BindingResult bindingResult, HttpServletRequest request) {
        Member result = loginService.login(loginFormDto);
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        if (result == null) {
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginId", loginFormDto.getId());

        boolean signup = url.contains("signup");
        if (signup){
            return "redirect:/";
        }

        return "redirect:" + url;
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {

        String referer = request.getHeader("Referer");
        Cookie cookie = new Cookie("url", referer);
        response.addCookie(cookie);
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@CookieValue("url") String url, @Valid LoginFormDto loginFormDto,
                               BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        Member result = loginService.login(loginFormDto);
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        if (result == null) {
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "login/loginForm";
        }

        loginMember.put(loginFormDto.getId(), loginFormDto.getId());


        HttpSession session = request.getSession();
        session.setAttribute("loginId", loginFormDto.getId());

        String jwt = loginService.createJwt(loginFormDto, request, response);

        if (url.contains("signup")){
            return "redirect:/";
        }
        if (url.contains("login")){
            return "redirect:/";
        }

        return "redirect:" + url;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession(false);
        if (session != null) {
            String loginId = (String)session.getAttribute("loginId");
            session.invalidate();
            loginMember.remove(loginId);
            log.info("세션 로그아웃 되었습니다");
        }

        return "redirect:" + referer ;
    }

}