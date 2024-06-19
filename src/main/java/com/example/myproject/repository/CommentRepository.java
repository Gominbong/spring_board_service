package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long>, CommentRepositoryCustom {

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member inner join fetch c.parentMember parentMember where musicList.id = :musicListId order by" +
            " c.parent, c.child1, c.child2, c. child3, c.child4")
    List<Comment> findCommentList(Long musicListId);

    List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId, int divWidthSize);

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member where c.id= :commentId")
    Comment findCommentId(Long commentId);

    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.parent = :parentId")
    List<Comment> findParent(Long musicListId, int parentId);

    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.child1 = :child1")
    List<Comment> findChild1(Long musicListId, int child1);

    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.child2 = :child2")
    List<Comment> findChild2(Long musicListId, int child2);


    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.child3 = :child3")
    List<Comment> findChild3(Long musicListId, int child3);

}
