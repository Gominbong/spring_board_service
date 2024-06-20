package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.MusicList;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentListQueryDsl(Long musicListId);

    List<Comment> findByMusicListIdAndDivWidthSizeQueryDsl(Long musicListId, int divWidthSize);

    Comment findCommentIdQueryDsl(Long commentId);

    List<Comment> findByParentQueryDsl(MusicList musicList, int parent);

    List<Comment> findByParentAndChildQueryDsl(MusicList musicList, int parent, int child);

    List<Comment> findByParentAndChildAndChild1QueryDsl(MusicList musicList, int parent, int child, int child1);

    List<Comment> findByParentAndChildAndChild1AndChild2QueryDsl(MusicList musicList, int parent, int child, int child1, int child2);
}
