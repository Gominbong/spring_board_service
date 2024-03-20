package com.example.myproject.service;

import com.example.myproject.domain.member.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Member> login(LoginFormDto loginFormDto) {

        List<Member> a = memberRepository.findEncodePassword(loginFormDto.getId());

        if (a.isEmpty()) {
            log.info("로그인Id와 비밀번호가 같지 않다");
            return null;
        }
        boolean matches = passwordEncoder.matches(loginFormDto.getPw(), String.valueOf(a.get(0)));
        if (matches) {
            log.info("로그인 성공");
            return a;
        }

        return null;
    }
    public void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}


