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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        comment.setContent(commentFormDto.getCommentContent());
        comment.setChild1(0);
        comment.setChild2(0);
        comment.setChild3(0);
        comment.setChild4(0);
        commentRepository.save(comment);
        return comment;
    }

    public List<Comment> findCommentList(Long id) {
        return commentRepository.findCommentListQueryDsl(id);
    }

    public List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId) {
        //부모만 찾아야 하므로 div 깊이가 0인것은 전부 부모다.
        return commentRepository.findByMusicListIdAndDivWidthSizeQueryDsl(musicListId, 0);
    }

    public void replyAdd(CommentReplyFormDto commentReplyFormDto, String loginId) {
        Member member = memberRepository.findByLoginIdQueryDsl(loginId);

        //대댓글 구조 변경중;; 원하는 결과값이 안나옴
        Comment comment = commentRepository.findCommentIdQueryDsl(commentReplyFormDto.getCommentId());
        Member parentMember = comment.getMember();

        Long musicListId = comment.getMusicList().getId();

        int parent = comment.getParent();
        int child1 = comment.getChild1();
        int child2 = comment.getChild2();
        int child3 = comment.getChild3();
        int child4 = comment.getChild4();
        Comment reply = new Comment();
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        reply.setMember(member);
        reply.setCreateTime(createTime);
        reply.setContent(commentReplyFormDto.getReplyContent());
        reply.setMusicList(comment.getMusicList());
        reply.setMusicList(comment.getMusicList());

        switch (comment.getDivWidthSize()) {
            case 0 -> {
                List<Comment> parentId = commentRepository.findParentQueryDsl(musicListId, parent);
                reply.setParentMember(parentMember);
                reply.setDivWidthSize(1);
                reply.setParent(parent);
                reply.setChild1(parentId.get(0).getChild1() +1);
                reply.setChild2(parentId.get(0).getChild1() +1);
                reply.setChild3(parentId.get(0).getChild1() +1);
                reply.setChild4(parentId.get(0).getChild1() +1);
            }
            case 1 -> {
                List<Comment> child1Id = commentRepository.findChild1QueryDsl(musicListId, child1);
                reply.setDivWidthSize(2);
                reply.setParentMember(parentMember);
                reply.setParent(parent);
                reply.setChild1(child1Id.get(0).getChild1() +1);
                reply.setChild2(child1Id.get(0).getChild1() +1);
                reply.setChild3(child1Id.get(0).getChild1() +1);
                reply.setChild4(child1Id.get(0).getChild1() +1);
            }
            case 2 -> {
                List<Comment> child2Id  = commentRepository.findChild2QueryDsl(musicListId, child2);
                reply.setDivWidthSize(3);
                reply.setParentMember(parentMember);
                reply.setParent(parent);
                reply.setChild1(child2Id.get(0).getChild1() +1);
                reply.setChild2(child2Id.get(0).getChild1() +1);
                reply.setChild3(child2Id.get(0).getChild1() +1);
                reply.setChild4(0);
            }
            case 3,4 -> {
                List<Comment> child3Id = commentRepository.findChild3QueryDsl(musicListId, child3);
                reply.setDivWidthSize(4);
                reply.setParentMember(parentMember);
                reply.setParent(parent);
                reply.setChild1(child3Id.get(0).getChild1() +1);
                reply.setChild2(child3Id.get(0).getChild1() +1);
                reply.setChild3(child3Id.get(0).getChild1() +1);
                reply.setChild4(child3Id.get(0).getChild1() +1);
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
