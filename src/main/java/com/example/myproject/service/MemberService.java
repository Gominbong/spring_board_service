package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.AddCashDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }

    @Transactional
    public Map<String, String> addCash(String loginId, AddCashDto addCashDto) {

        Map<String, String> errors = new HashMap<>();

        if (addCashDto.getCash() == null){
            errors.put("cash", "충전할금액입력하세요");
        }
        if (addCashDto.getCash() > 1000000){
            errors.put("cash", "충전금액초과");
        }

        if (!errors.isEmpty()){
            return errors;
        }


        Member member = memberRepository.findByLoginId(loginId);
        member.setCash(member.getCash() + addCashDto.getCash());

        return null;
    }

}
