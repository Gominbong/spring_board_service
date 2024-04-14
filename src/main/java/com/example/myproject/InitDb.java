package com.example.myproject;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;
    static int a = 0;
    @PostConstruct
    public void init() {
/*        for(int i=0; i<20; i++){
            initService.dbInit1();
        }*/
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;
        public void dbInit1(){
            Member member = createMember("rhalsqhd123", passwordEncoder.encode("123"),
                    "고민봉");

            MusicList musicList = MusicList.builder()
                    .title("테스트용 더미 글 입니다.")
                    .memberNickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .member(member)
                    .localDateTime(LocalDateTime.now().withNano(0))
                    .content("체르니 40정도면 칠수 있어요")
                    .type("피아노")
                    .level("어려움")
                    .price(4000)
                    .build();

            MusicList musicList1 = MusicList.builder()
                    .title("테스트용 더미 글 입니다.")
                    .memberNickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .member(member)
                    .localDateTime(LocalDateTime.now().withNano(0))
                    .content("체르니 100정도면 칠수있어요 ")
                    .type("피아노")
                    .level("보통")
                    .price(3000)
                    .build();
            em.persist(member);
            em.persist(musicList);
            em.persist(musicList1);
        }

        private Member createMember(String loginId, String password, String nickname) {
            Member member = new Member();
            a=a+1;
            String b = String.valueOf(a);
            member.setLoginId(loginId+b);
            member.setPassword(password);
            member.setNickname(nickname+b);
            member.setCash(20000);
            member.setLocalDate(LocalDate.now());
            return member;
        }
    }


}
