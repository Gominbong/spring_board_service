package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long> {


    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member where musicList.id = :musicListId ")
    List<Comment> findCommentList1(Long musicListId);

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member where musicList.id = :musicListId order by" +
            " c.parent, c.child1, c.child2, c. child3, c.child4")
    List<Comment> findCommentList(Long musicListId);

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member where musicList.id = :musicListId order by" +
            " c.parent, c.child1, c.child2, c. child3, c.child4")
    Page<Comment> findFirstCommentList(Pageable pageable, Long musicListId);

    List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId, int divWidthSize);

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member where c.id= :commentId")
    Comment findCommentId(Long commentId);

    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.parent = :parentId" +
            " order by c.parent desc, c.child1 desc, c.child2 desc, c.child3 desc, c.child4 desc")
    List<Comment> findParent(Long musicListId, int parentId);

    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.parent = :parentId and c.child1 = :child1" +
            " order by c.parent desc, c.child1 desc, c.child2 desc, c.child3 desc, c.child4 desc")
    List<Comment> findParentChild1(Long musicListId, int parentId, int child1);

    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.parent = :parentId and c.child1 = :child1" +
            " and c.child2 = :child2" +
            " order by c.parent desc, c.child1 desc, c.child2 desc, c.child3 desc, c.child4 desc")
    List<Comment> findParentChild1Child2(Long musicListId, int parentId, int child1, int child2);


    @Query("select c from Comment c inner join fetch c.musicList musicList" +
            " where c.musicList.id = :musicListId and c.parent = :parentId and c.child1 = :child1" +
            " and c.child2 = :child2 and c.child3 = :child3" +
            " order by c.parent desc, c.child1 desc, c.child2 desc, c.child3 desc, c.child4 desc")
    List<Comment> findParentChild1Child2Child3(Long musicListId, int parentId, int child1, int child2, int child3);

}
