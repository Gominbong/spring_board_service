package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.LoginFormDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Member login(LoginFormDto loginFormDto) {

        Member result = memberRepository.findEncodePassword(loginFormDto.getId());
        log.info("암호화 비밀번호 가져오기 확인  = {}", result);

        if (result == null) {
            return null;
        }
        boolean matches = passwordEncoder.matches(loginFormDto.getPw(), result.getPassword());
        if (matches) {
            log.info("loginId 값으로 db에 있는 pw 조회성공");
            return result;
        }
        return null;
    }

}








