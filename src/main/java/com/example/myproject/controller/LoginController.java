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

    @GetMapping("/login")
    public String loginForm(Model model) {

        model.addAttribute("loginFormDto", new LoginFormDto());
        return "login/loginForm";
    }

    @GetMapping("/")
    public String loginSession(@RequestParam(value = "page", defaultValue = "0")  int page,
                               HttpServletRequest request, Model model) {
        model.addAttribute("menu", "home");
        HttpSession session = request.getSession(false);
        log.info("페이지 정보 확인 = '{}' ", page);

        if (session == null) {
            Page<MusicList> paging = musicListService.findAllItemList(page);
            model.addAttribute("paging", paging);
            model.addAttribute("page", page);
            int temp=page/7;
            int start = temp* 7;
            log.info("스타트 페이지 위치 확인 해보기 = '{}' ", start);

            model.addAttribute("start", start);
            model.addAttribute("end", start+6);
            return "home";
        }
        model.addAttribute("loginId", session.getAttribute("loginId"));
        Object id = session.getAttribute("id");
        Page<MusicList> paging = musicListService.findAllItemList(page);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        int temp=page/7;
        int start = temp* 7;
        log.info("스타트 페이지 확인 해보기 = '{}' ", start);

        model.addAttribute("start", start);
        model.addAttribute("end", start+6);
        return "home";
    }

    @PostMapping("/login")
    public String loginSession(@Valid LoginFormDto loginFormDto,
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