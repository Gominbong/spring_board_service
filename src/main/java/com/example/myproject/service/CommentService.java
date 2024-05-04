package com.example.myproject.service;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.CommentFormDto;
import com.example.myproject.repository.CommentRepository;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.repository.MusicListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MusicListRepository musicListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Comment commentAdd(CommentFormDto commentFormDto, String loginId, int parent) {

        MusicList musicList = musicListRepository.findById(commentFormDto.getMusicListId()).orElseThrow();
        Member member = memberRepository.findByLoginId(loginId);
        Comment comment = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        comment.setCreateTime(createTime);
        comment.setMusicList(musicList);
        comment.setMember(member);
        comment.setDivWidthSize(0);
        comment.setParent(parent);
        comment.setContent(commentFormDto.getContent());
        comment.setChild1(0);
        comment.setChild2(0);
        comment.setChild3(0);
        comment.setChild4(0);
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> findMusicListId(Long id) {
        return commentRepository.findMusicListId(id);
    }

    public List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId) {
        return commentRepository.findByMusicListIdAndDivWidthSize(musicListId, 0);
    }
}
