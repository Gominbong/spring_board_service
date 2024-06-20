package com.example.myproject;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.repository.CommentRepository;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.service.MemberService;
import com.example.myproject.service.MusicListService;
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
    private static int parent = 0;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {

        /*
        Member member = memberRepository.findByLoginId("3");
        for (int a=0; a<3095; a++){
            initService.for0(102L ,a, member);
        }*/


/*

        for (int a=0; a<5; a++){
            initService.for0(102L ,a, member);
            for (int b=1; b<6; b++){
                initService.for1(102L, a, b, member);
                for (int c=2; c<7; c++){
                    initService.for2(102L, a, b, c, member);
                    for (int d=3; d<8; d++){
                        initService.for3(102L ,a, b, c, d, member);
                        for (int e=4; e<9; e++){
                            initService.for4(102L ,a, b, c, d, e, member);
                        }
                    }
                }
            }
        }
*/


    /*    parent = 0;
        for (int i=0; i<2000; i++){
            initService.dbInit3(402L);
        }
        parent = 0;
        for (int i=0; i<3000; i++){
            initService.dbInit3(403L);
        }
        parent = 0;
        for (int i=0; i<4000; i++){
            initService.dbInit3(404L);
        }
        parent = 0;
        for (int i=0; i<5000; i++){
            initService.dbInit3(405L);
        }*/

        /*
        initService.createMember("3", "3", "Rhalsqhd123");
        initService.createMember("2", "2", "Rhalsqhd456");
        */

/*        for(int i=0; i<170; i++){
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
        private final MusicListService musicListService;
        private final MusicListRepository musicListRepository;
        private final MemberRepository memberRepository;
        private final CommentRepository commentRepository;

        public void for0(Long id, int a, Member member){
            MusicList musicList = musicListRepository.findById(id).orElseThrow();

            Comment comment = new Comment();
            comment.setMember(member);
            comment.setParentMember(member);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(0);
            comment.setContent("테스트내용");
            comment.setParent(a);

            commentRepository.save(comment);

        }

        public void for1(Long id, int a, int b, Member member){
            MusicList musicList = musicListRepository.findById(id).orElseThrow();

            Comment comment = new Comment();
            comment.setMember(member);
            comment.setParentMember(member);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(1);
            comment.setContent("테스트내용");
            comment.setParent(a);

            commentRepository.save(comment);

        }

        public void for2(Long id, int a, int b, int c, Member member){
            MusicList musicList = musicListRepository.findById(id).orElseThrow();

            Comment comment = new Comment();
            comment.setMember(member);
            comment.setParentMember(member);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(2);
            comment.setContent("테스트내용");
            comment.setParent(a);

            commentRepository.save(comment);

        }

        public void for3(Long id, int a, int b, int c, int d, Member member){
            MusicList musicList = musicListRepository.findById(id).orElseThrow();

            Comment comment = new Comment();
            comment.setMember(member);
            comment.setParentMember(member);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(3);
            comment.setContent("테스트내용");
            comment.setParent(a);

            commentRepository.save(comment);

        }

        public void for4(Long id, int a, int b, int c, int d, int e, Member member){
            MusicList musicList = musicListRepository.findById(id).orElseThrow();

            Comment comment = new Comment();
            comment.setMember(member);
            comment.setParentMember(member);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(4);
            comment.setContent("테스트내용");
            comment.setParent(a);

            commentRepository.save(comment);

        }


        public void dbInit3(Long id){
            MusicList musicList = musicListRepository.findById(id).orElseThrow();
            Member member = memberRepository.findByLoginId("3");

            Comment comment = new Comment();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            comment.setCreateTime(createTime);
            comment.setMusicList(musicList);
            comment.setDivWidthSize(0);
            comment.setParent(parent);
            parent += 1;
            comment.setContent("테스트내용");

            commentRepository.save(comment);

        }

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
