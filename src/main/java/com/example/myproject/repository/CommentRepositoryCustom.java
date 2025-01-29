package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.MusicList;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentListQueryDsl(Long musicListId);

    List<Comment> findByMusicListIdAndDepthQueryDsl(Long musicListId, int depth);

    Comment findCommentIdQueryDsl(Long commentId);


}
