package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.SignupFormDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignupService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Map<String, String> createMember(SignupFormDto signupFormDto) {

        Member loginId = memberRepository.findByLoginIdQueryDsl(signupFormDto.getId());
        Member nickname = memberRepository.findByNicknameQueryDsl(signupFormDto.getNick());
        Map<String, String> errors = new HashMap<>();
        Member member = new Member();
        if (signupFormDto.getPw().equals(signupFormDto.getPwCheck())){
            member.setPassword(passwordEncoder.encode(signupFormDto.getPw()));
        }else{
            errors.put("pwCheck","패스워드불일치");
        }
        if (!StringUtils.hasText(signupFormDto.getId())) {
            errors.put("id", "아이디필수입니다");
        }
        if (!StringUtils.hasText(signupFormDto.getPw())) {
            errors.put("pw", "패스워드필수입니다");
        }
        if (!StringUtils.hasText(signupFormDto.getPwCheck())) {
            errors.put("pwCheck", "패스워드필수입니다");
        }
        if (!StringUtils.hasText(signupFormDto.getNick())) {
            errors.put("nick", "닉네임필수입니다");
        }
        if (loginId != null){
            errors.put("exist", "아이디중복입니다");
        }
        if (nickname != null){
            errors.put("nick", "닉네임 중복입니다");
        }
        if (!errors.isEmpty()) {
            log.info("errors = '{}'", errors);
            return errors;
        }

        member.setLoginId(signupFormDto.getId());
        member.setNickname(signupFormDto.getNick());
        member.setCash(20000);
        member.setRevenue(0);
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        member.setCreateTime(createTime);
        member.setId(member.getId());

        memberRepository.save(member);
        return null;
    }
}