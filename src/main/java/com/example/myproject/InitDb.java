package com.example.myproject;

import com.example.myproject.domain.member.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;
        public void dbInit1(){
            Member member = createMember("sjy2017z", passwordEncoder.encode("123"), "고민봉");
            em.persist(member);
        }

        private Member createMember(String loginId, String password, String nickname) {
            Member member = new Member();
            member.setLoginId(loginId);
            member.setPassword(password);
            member.setNickname(nickname);
            return member;
        }
    }


}
