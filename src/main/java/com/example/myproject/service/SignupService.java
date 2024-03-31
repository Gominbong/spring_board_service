package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.SignupFormDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Map<String, String> createMember(SignupFormDto signupFormDto) {

        Member result = memberRepository.findByLoginId(signupFormDto.getId());


        Member member = new Member();
        Map<String, String> errors = new HashMap<>();
        member.setLoginId(signupFormDto.getId());
        member.setPassword(passwordEncoder.encode(signupFormDto.getPw()));
        member.setNickname(signupFormDto.getNick());
        member.setCache(10000);
        member.setLocalDate(LocalDate.now());
        member.setId(member.getId());
        if (!StringUtils.hasText(signupFormDto.getId())) {
            errors.put("id", "아이디필수입니다");
        }
        if (!StringUtils.hasText(signupFormDto.getPw())) {
            errors.put("pw", "패스워드필수입니다");
        }
        if (!StringUtils.hasText(signupFormDto.getNick())) {
            errors.put("nick", "닉네임필수입니다");
        }
        if (result != null){
            errors.put("exist", "아이디중복입니다");
        }
        if (!errors.isEmpty()) {
            log.info("errors = {}", errors);
            return errors;
        }

        memberRepository.save(member);
        return null;
    }
}