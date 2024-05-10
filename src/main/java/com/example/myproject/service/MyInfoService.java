package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.MyInfoEditDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyInfoService {

    private final MemberRepository memberRepository;

    @Transactional
    public Map<String, String> myInfoEdit(MyInfoEditDto myInfoEditDto, String loginId) {

        Map<String, String> errors = new HashMap<>();
        if (!StringUtils.hasText(myInfoEditDto.getNickname())){
            errors.put("nickname", "닉네임입력필수입니다");
            return errors;
        }

        Member result = memberRepository.findByNickname(myInfoEditDto.getNickname());
        if (result == null){
            Member member = memberRepository.findByLoginId(loginId);
            member.setNickname(myInfoEditDto.getNickname());
        }else{
            log.info("닉네임중복입니다.");
            errors.put("nickname", "닉네임중복입니다");
        }
        if (!errors.isEmpty()) {
            log.info("errors = '{}'", errors);
            return errors;
        }
        return null;
    }
}
