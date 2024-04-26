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

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final MusicListService musicListService;

    @GetMapping("/")
    public String home(@RequestParam(value = "page", defaultValue = "0") int page,
                       HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("menu", "home");
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        Cookie cookie = new Cookie("url", requestURI);
        response.addCookie(cookie);
        log.info("페이지 정보 확인 = '{}' ", page);

        if (session != null) {
            String loginId = (String) session.getAttribute("loginId");
            model.addAttribute("loginId", loginId);
        }

        Page<MusicList> paging = musicListService.findMusicList(page);

        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());
        int temp = page / 7;
        int start = temp * 7;

        if (paging.getTotalPages() == 0) {
                log.info("여기11111 = {}", paging.getTotalPages());
                model.addAttribute("start", start);
                model.addAttribute("end", paging.getTotalPages());
        } else if (paging.getTotalPages() - start > 7) {
            log.info("여기22222 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", start + 6);
        } else if (paging.getTotalPages() < 6){
            log.info("여기33333 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        }

        log.info("스타트 페이지 확인 해보기 = '{}' ", start);

        return "home";
    }

    @GetMapping("/loginInterceptor")
    public String loginInterceptor(Model model) {
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "/login/loginInterceptorForm";
    }

    @PostMapping("/loginInterceptor")
    public String loginInterceptor(@CookieValue("url") String url, @Valid LoginFormDto loginFormDto,
                               BindingResult bindingResult, HttpServletRequest request) {
        Member result = loginService.login(loginFormDto);
        log.info("로그인후 돌아갈 경로 확인 = '{}'", url);
        log.info("암호화된비밀번호가져오기= '{}'", result);
        if (bindingResult.hasErrors()) {
            return "/login/loginInterceptorForm";
        }
        if (result == null) {
            log.info("로그인 실패");
            bindingResult.reject("loginFail", "아이디 또는 비밀번호 맞지 않습니다");
            return "/login/loginInterceptorForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginId", loginFormDto.getId());

        log.info("세션 로그인 아이디 = '{}'", loginFormDto.getId());
        return "redirect:" + url;
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request, HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        Cookie cookie = new Cookie("url", referer);
        log.info("이전 위치 url = {}", referer);
        response.addCookie(cookie);
        model.addAttribute("loginFormDto", new LoginFormDto());
        return "/login/loginForm";
    }

    @PostMapping("/login")
    public String login(@CookieValue("url") String url, @Valid LoginFormDto loginFormDto,
                        BindingResult bindingResult, HttpServletRequest request) {
        Member result = loginService.login(loginFormDto);

        log.info("암호화된비밀번호가져오기= '{}'", result);
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

        log.info("세션 로그인 아이디 = '{}'", loginFormDto.getId());
        return "redirect:" + url;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("세션 로그아웃 되었습니다");
        }

        return "redirect:/";
    }
}