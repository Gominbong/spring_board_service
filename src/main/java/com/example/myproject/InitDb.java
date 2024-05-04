package com.example.myproject;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.service.MemberService;
import com.example.myproject.service.MusicListService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    static int k = 0;

    static int e = 0;
    static int f = 0;
    static int g = 0;
    static int h = 0;
    @PostConstruct
    public void init() {


/*
        for(int i=0; i<5; i++){
            initService.dbInit3();
        }
*/



/*
        for(int i=0; i<10; i++){
            initService.dbInit2();
        }
*/




/*        for(int i=0; i<200; i++){
            initService.dbInit1();
        }*/
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final PasswordEncoder passwordEncoder;
        private final MusicListService musicListService;
        private final MemberService memberService;

        public void dbInit2(){
            Comment comment = createComment();
            em.persist(comment);

        }
        public void dbInit3(){
            Comment child = createCommentChild();
            em.persist(child);
        }

        private Comment createCommentChild() {
            MusicList musicList = musicListService.findById(1L);
            Member member = memberService.findByLoginId("3");
            Comment comment = new Comment();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setParent(5);
            comment.setMember(member);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(4);
            comment.setContent("안녕하세요ㅋㅋ"+ d++);
            comment.setChild1(3);
            comment.setChild2(3);
            comment.setChild3(3);
            comment.setChild4(0);
            return comment;
        }

        private Comment createComment() {
            MusicList musicList = musicListService.findById(1L);
            Member member = memberService.findByLoginId("3");
            Comment comment = new Comment();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setParent(c++);
            comment.setMember(member);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(0);
            comment.setContent("안녕하세요"+ d++);
            comment.setChild1(0);
            comment.setChild2(0);
            comment.setChild3(0);
            comment.setChild4(0);
            return comment;
        }

        public void dbInit1(){
            Member member = createMember("rhalsqhd123", passwordEncoder.encode("123"),
                    "고민봉");

            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            MusicList musicList = MusicList.builder()
                    .title("테스트용 더미 글 입니다.")
                    .member(member)
                    .createTime(createTime)
                    .content("체르니 40정도면 칠수 있어요")
                    .type("피아노")
                    .level("어려움")
                    .salesQuantity(0)
                    .price(4000)
                    .views(0)
                    .likeCount(0)
                    .salesQuantity(0)
                    .build();

            MusicList musicList1 = MusicList.builder()
                    .title("테스트용 더미 글 입니다.")
                    .member(member)
                    .createTime(createTime)
                    .content("체르니 100정도면 칠수있어요 ")
                    .type("피아노")
                    .level("보통")
                    .salesQuantity(0)
                    .views(0)
                    .likeCount(0)
                    .price(3000)
                    .salesQuantity(0)
                    .build();
            em.persist(member);
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
