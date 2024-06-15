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
        Member member = memberRepository.findByLoginId(loginId);
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
        return commentRepository.findCommentList(id);
    }

    public List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId) {
        return commentRepository.findByMusicListIdAndDivWidthSize(musicListId, 0);
    }

    public void replyAdd(CommentReplyFormDto commentReplyFormDto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId);

        // divWidthSize 0이면 댓글이고 1,2,3,4면 대댓글 이다
        // 댓글 추가시 parent 0부터 시작 해 +1 씩 증가
        // parent 0에 대댓글 추가시 부모 parent 0을 넣고 child1에 +1로 순서를 셋팅한다
        // divWidthSize 1이면 parent child1 넣고
        // divWidthSize 2이면 parent child1 child2 넣는식으로 셋팅한다.
        // 대댓글 깊이 5단계인데 원하는 깊이만큼 추가로 만들 수 있다.
        Comment comment = commentRepository.findCommentId(commentReplyFormDto.getCommentId());
        Member parentMember = comment.getMember();

        Long musicListId = comment.getMusicList().getId();
        int parentId = comment.getParent();
        int child1 = comment.getChild1();
        int child2 = comment.getChild2();
        int child3 = comment.getChild3();

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
                List<Comment> parent = commentRepository.findParent(musicListId, parentId);
                reply.setParentMember(parentMember);
                reply.setDivWidthSize(1);
                reply.setMember(member);
                reply.setParent(comment.getParent());
                reply.setChild1(parent.get(0).getChild1() +1);
                reply.setChild2(0);
                reply.setChild3(0);
                reply.setChild4(0);
            }
            case 1 -> {
                List<Comment> parent = commentRepository.findParentChild1(musicListId, parentId, child1);
                reply.setDivWidthSize(2);
                reply.setParentMember(parentMember);
                reply.setParent(comment.getParent());
                reply.setChild1(comment.getChild1());
                reply.setChild2(parent.get(0).getChild2() +1);
                reply.setChild3(0);
                reply.setChild4(0);
            }
            case 2 -> {
                List<Comment> parent = commentRepository.
                        findParentChild1Child2(musicListId, parentId, child1, child2);
                reply.setDivWidthSize(3);
                reply.setParentMember(parentMember);
                reply.setParent(comment.getParent());
                reply.setChild1(comment.getChild1());
                reply.setChild2(comment.getChild2());
                reply.setChild3(parent.get(0).getChild3() +1);
                reply.setChild4(0);
            }
            case 3, 4 -> {
                List<Comment> parent = commentRepository.
                        findParentChild1Child2Child3(musicListId, parentId, child1, child2, child3);
                reply.setDivWidthSize(4);
                reply.setParentMember(parentMember);
                reply.setParent(comment.getParent());
                reply.setChild1(comment.getChild1());
                reply.setChild2(comment.getChild2());
                reply.setChild3(comment.getChild3());
                reply.setChild4(parent.get(0).getChild4() +1);
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
