package com.example.myproject.service;

import com.example.myproject.domain.member.Member;
import com.example.myproject.dto.MemberDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, String> createMember(MemberDto memberDto) {

        List<Member> result = memberRepository.findLoginId(memberDto.getId());
        Member member = new Member();
        Map<String, String> errors = new HashMap<>();

        member.setLoginId(memberDto.getId());
        member.setPassword(passwordEncoder.encode(memberDto.getPwd()));
        member.setNickname(memberDto.getNickname());

        if (!StringUtils.hasText(memberDto.getId())) {
            errors.put("id", "아이디필수입니다");
        }
        if (!StringUtils.hasText(memberDto.getPwd())) {
            errors.put("pwd", "패스워드필수");
        }
        if (!StringUtils.hasText(memberDto.getNickname())) {
            errors.put("nick", "닉네임필수");
        }
        if (!result.isEmpty()){
            errors.put("exist", "이미존재하는회원입니다");
        }
        if (!errors.isEmpty()) {
            log.info("errors = {}", errors);
            return errors;
        }

        memberRepository.save(member);
        return null;

    }
}
