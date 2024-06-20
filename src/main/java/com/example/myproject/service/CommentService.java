package com.example.myproject.service;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.CommentDeleteDto;
import com.example.myproject.dto.CommentFormDto;
import com.example.myproject.dto.CommentReplyFormDto;
import com.example.myproject.dto.CommentUpdateDto;
import com.example.myproject.repository.CommentRepository;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.repository.MusicListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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
    public Comment commentAdd(CommentFormDto commentFormDto, String loginId, int parent) {

        MusicList musicList = musicListRepository.findById(commentFormDto.getMusicListId()).orElseThrow();
        Member member = memberRepository.findByLoginIdQueryDsl(loginId);
        Comment comment = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        comment.setCreateTime(createTime);
        comment.setMember(member);
        comment.setParentMember(member);
        comment.setMusicList(musicList);
        comment.setDivWidthSize(0);
        comment.setParent(parent);
        comment.setChild(0);
        comment.setContent(commentFormDto.getCommentContent());
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> findCommentList(Long id) {
        return commentRepository.findCommentListQueryDsl(id);
    }

    public List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId) {
        return commentRepository.findByMusicListIdAndDivWidthSizeQueryDsl(musicListId, 0);
    }



    @Transactional
    public void replyAdd(CommentReplyFormDto commentReplyFormDto, String loginId) {
        Member member = memberRepository.findByLoginIdQueryDsl(loginId);
        log.info("디티오확인 = {}", commentReplyFormDto.getCommentId());

        Comment comment = commentRepository.findCommentIdQueryDsl(commentReplyFormDto.getCommentId());
        Member parentMember = comment.getMember();
        log.info("댓글부모 정보 = {} ", comment.getChild());

        Comment reply = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        reply.setMember(member);
        reply.setParentMember(parentMember);
        reply.setMusicList(comment.getMusicList());
        reply.setCreateTime(createTime);
        reply.setContent(commentReplyFormDto.getReplyContent());
        reply.setParent(comment.getParent());


        //대댓글 중간 삽입 할 경우 index 순서를 정함
        switch (comment.getDivWidthSize()){
            case 0 -> {
                List<Comment> parent = commentRepository.findByParentQueryDsl(comment.getMusicList(), comment.getParent());
                reply.setParent(comment.getParent());
                reply.setDivWidthSize(1);
                reply.setChild(parent.size());

            }
            case 1 -> {
                List<Comment> parent = commentRepository.
                        findByParentAndChildQueryDsl(comment.getMusicList(), comment.getParent(), comment.getChild());
                reply.setDivWidthSize(2);
                reply.setParent(comment.getParent());
                reply.setChild(comment.getChild());
                reply.setChild1(parent.size());

            }
            case 2 -> {
                List<Comment> parent = commentRepository.
                        findByParentAndChildAndChild1QueryDsl(comment.getMusicList(),
                                comment.getParent(), comment.getChild(), comment.getChild1());
                reply.setDivWidthSize(3);
                reply.setParent(comment.getParent());
                reply.setChild(comment.getChild());
                reply.setChild1(comment.getChild1());
                reply.setChild2(parent.size());
            }
            case 3 -> {
                List<Comment> parent = commentRepository.
                        findByParentAndChildAndChild1AndChild2QueryDsl(comment.getMusicList(),
                                comment.getParent(), comment.getChild(), comment.getChild1(), comment.getChild2());
                reply.setDivWidthSize(4);
                reply.setParent(comment.getParent());
                reply.setChild(comment.getChild());
                reply.setChild1(comment.getChild1());
                reply.setChild2(comment.getChild2());
                reply.setChild3(parent.size());
            }
            case 4 ->{
                reply.setDivWidthSize(4);
                reply.setParent(comment.getParent());
                reply.setChild(comment.getChild());
                reply.setChild1(comment.getChild1());
                reply.setChild2(comment.getChild2());
                reply.setChild3(comment.getChild3());
            }

        }



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
