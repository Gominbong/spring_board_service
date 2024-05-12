package com.example.myproject;


import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    static int a = 0;
    static int b = 0;
    static int c = 0;
    static int d = 100;

    @PostConstruct
    public void init() {

/*        for(int i=0; i<200; i++){
            initService.dbInit1();
        }*/
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final MemberService memberService;



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

            MusicList musicList1 = MusicList.builder()
                    .title("페이징 확인용 테스트 글")
                    .member(member)
                    .createTime(createTime)
                    .content("체르니 100정도면 칠수있어요 ")
                    .type("피아노")
                    .level("보통")
                    .salesQuantity(0)
                    .likeCount(0)
                    .price(3000)
                    .salesQuantity(0)
                    .build();
            em.persist(musicList);
            em.persist(musicList1);
        }

        private Member createMember(String loginId, String password, String nickname) {
            Member member = new Member();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            a=a+1;
            String b = String.valueOf(a);
            member.setLoginId(loginId+b);
            member.setPassword(password);
            member.setNickname(nickname+b);
            member.setCash(20000);
            member.setRevenue(0);
            member.setCreateTime(createTime);

            return member;
        }
    }


}
