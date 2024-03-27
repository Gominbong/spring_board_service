package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.AddItemFormDto;
import com.example.myproject.repository.AddItemRepository;
import com.example.myproject.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AddItemService {

    private final EntityManager em;

    private final AddItemRepository addItemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createAddItem(HttpServletRequest request, AddItemFormDto addItemFormDto){

        HttpSession session = request.getSession();
        Long id = (Long)session.getAttribute("id");
        Member member = em.find(Member.class, id);


        MusicList musicList = new MusicList();
        musicList.setMember(member); //Member id 외래키 설정
        musicList.setTitle(addItemFormDto.getTitle());
        musicList.setType(addItemFormDto.getType());
        musicList.setLevel(addItemFormDto.getLevel());
        musicList.setPrice(addItemFormDto.getPrice());
        musicList.setContent(addItemFormDto.getContent());
        musicList.setLocalDateTime(LocalDateTime.now());


        em.persist(musicList);
    }
}
