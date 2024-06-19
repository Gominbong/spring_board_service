package com.example.myproject.repository;

import com.example.myproject.domain.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentListQueryDsl(Long musicListId);

    List<Comment> findByMusicListIdAndDivWidthSizeQueryDsl(Long musicListId, int divWidthSize);

    Comment findCommentIdQueryDsl(Long commentId);

    List<Comment> findParentQueryDsl(Long musicListId, int parentId);

    List<Comment> findChild1QueryDsl(Long musicListId, int child1);

    List<Comment> findChild2QueryDsl(Long musicListId, int child2);

    List<Comment> findChild3QueryDsl(Long musicListId, int child3);

    List<Comment> findChild4QueryDsl(Long musicListId, int child4);
}
