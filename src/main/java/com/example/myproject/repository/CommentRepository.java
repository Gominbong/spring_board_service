package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.MusicList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long>, CommentRepositoryCustom {

    @Query(value = "WITH RECURSIVE CommentHierarchy AS (" +
            "SELECT c.comment_id, c.content, c.member_id, c.parent_id, c.parent_member_id, c.music_list_id, c.create_time, c.depth, c.soft_delete, CAST(c.comment_id AS CHAR(255)) AS path " +
            "FROM comment c WHERE c.parent_id IS NULL AND c.music_list_id = :musicListId " +
            "UNION ALL " +
            "SELECT c.comment_id, c.content, c.member_id, c.parent_id, c.parent_member_id, c.music_list_id, c.create_time, c.depth, c.soft_delete, CONCAT(ch.path, ',', c.comment_id) AS path " +
            "FROM comment c JOIN CommentHierarchy ch ON c.parent_id = ch.comment_id " +
            ") " +
            "SELECT ch.*, ml.title AS musicListTitle " +
            "FROM CommentHierarchy ch " +
            "JOIN music_list ml ON ch.music_list_id = ml.music_list_id " +
            "ORDER BY ch.path", nativeQuery = true)
    List<Comment> findCommentsByMusicListId(@Param("musicListId") Long musicListId);




}


