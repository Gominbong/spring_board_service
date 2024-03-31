package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
            return null;
        }
        boolean matches = passwordEncoder.matches(loginFormDto.getPw(), String.valueOf(a.get(0)));
        if (matches) {
            log.info("loginId 값으로 db에 있는 pw 조회성공");
            return a;
        }

        return null;
    }
    public void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public List<Member> findMemberId(String id) {
        return memberRepository.findById(id);
    }
}








