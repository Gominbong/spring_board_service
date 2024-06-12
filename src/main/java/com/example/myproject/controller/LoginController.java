package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.service.MusicListService;
import com.example.myproject.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final MusicListService musicListService;


    @GetMapping("/")
    public String home(@RequestParam(value = "page", defaultValue = "0") int page,
                       HttpServletRequest request, Model model, HttpServletResponse response) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        log.info("아이피 주소 = {}", ip);
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
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
                               BindingResult bindingResult, HttpServletRequest request,
                                   HttpServletResponse response) {
        Member result = loginService.login(loginFormDto);
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        if (result == null) {
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "login/loginForm";
        }

        loginService.createJwt(loginFormDto.getId(), request, response);

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

        loginService.createJwt(loginFormDto.getId(), request, response);

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

        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");
        if (jwtCookie != null){
            jwtCookie.setMaxAge(0);
            response.addCookie(jwtCookie);
            log.info("jwt 로그아웃 되었습니다");
        }

        return "redirect:" + referer ;
    }

}