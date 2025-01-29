package com.example.myproject.service;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.*;
import com.example.myproject.repository.CommentRepository;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.repository.MusicListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MusicListRepository musicListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void commentDelete(CommentDeleteDto commentId) {
        Comment comment = commentRepository.findById(commentId.getCommentId()).orElseThrow();
        comment.setSoftDelete("yes");
    }

    @Transactional
    public Comment commentAdd(CommentFormDto commentFormDto, String loginId) {

        MusicList musicList = musicListRepository.findById(commentFormDto.getMusicListId()).orElseThrow();
        Member member = memberRepository.findByLoginIdQueryDsl(loginId);
        Comment comment = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        comment.setCreateTime(createTime);
        comment.setMember(member);
        comment.setMusicList(musicList);
        comment.setDepth(0);
        comment.setContent(commentFormDto.getCommentContent());
        commentRepository.save(comment);
        return comment;
    }



    @Transactional
    public List<CommentDTO> findCommentList(Long id) {
        List<Comment> comments = commentRepository.findCommentsByMusicListId(id);
        return comments.stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void replyAdd(CommentReplyFormDto commentReplyFormDto, String loginId) {
        MusicList musicList = musicListRepository.findById(commentReplyFormDto.getMusicListId()).orElseThrow();
        Comment comment = commentRepository.findCommentIdQueryDsl(commentReplyFormDto.getParentId());
        Member member = memberRepository.findByLoginIdQueryDsl(loginId);

        log.info("댓글 로그보기"+comment.getMember().getId());
        log.info("멤버 로그보기"+member.getId());

        Comment reply = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        reply.setMember(member);
        reply.setMusicList(musicList);
        reply.setParentMember(comment.getMember());
        reply.setCreateTime(createTime);
        reply.setDepth(comment.getDepth() + 1);
        reply.setContent(commentReplyFormDto.getReplyContent());
        reply.setParent(comment);

        commentRepository.save(reply);

    }


    @Transactional
    public void commentEdit(CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentUpdateDto.getCommentId()).orElseThrow();
        comment.setContent(commentUpdateDto.getCommentEditContent());
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        comment.setCreateTime(createTime);
    }
}
