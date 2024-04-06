package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.MyInfoDto;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public Member findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }


    public void addCash(String loginId, MyInfoDto myInfoDto) {
        Member member = memberRepository.findByLoginId(loginId);
        member.setCash(member.getCash() + myInfoDto.getCash());
        memberRepository.save(member);
    }

}
