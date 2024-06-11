package com.example.myproject;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
/*        initService.createMember("3", "3", "Rhalsqhd123");
        initService.createMember("2", "2", "Rhalsqhd456");
        for(int i=0; i<170; i++){
            initService.dbInit1();
            initService.dbInit2();
        }*/

    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final MemberService memberService;
        private final PasswordEncoder passwordEncoder;

        public void dbInit1(){
            Member member = memberService.findByLoginId("3");
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            MusicList musicList = MusicList.builder()
                    .title("페이징 확인용 테스트 글")
                    .member(member)
                    .createTime(createTime)
                    .content("체르니 40정도면 칠수 있어요")
                    .type("피아노")
                    .level("어려움")
                    .salesQuantity(0)
                    .price(4000)
                    .likeCount(0)
                    .salesQuantity(0)
                    .build();
            em.persist(musicList);
        }

        public void dbInit2(){
            Member member = memberService.findByLoginId("2");
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            MusicList musicList = MusicList.builder()
                    .title("페이징 확인용 테스트 글")
                    .member(member)
                    .createTime(createTime)
                    .content("체르니 40정도면 칠수 있어요")
                    .type("피아노")
                    .level("어려움")
                    .salesQuantity(0)
                    .price(3000)
                    .likeCount(0)
                    .salesQuantity(0)
                    .build();
            em.persist(musicList);
        }

        public void createMember(String loginId, String password, String nickname) {
            Member member = new Member();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            member.setLoginId(loginId);
            member.setPassword(passwordEncoder.encode(password));
            member.setNickname(nickname);
            member.setCash(20000);
            member.setRevenue(0);
            member.setCreateTime(createTime);
            em.persist(member);
        }
    }


}
