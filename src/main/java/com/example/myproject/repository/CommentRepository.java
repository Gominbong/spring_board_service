package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import com.example.myproject.domain.MusicList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long>, CommentRepositoryCustom {

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member inner join fetch c.parentMember parentMember where musicList.id = :musicListId" +
            " order by c.parent asc, c.child asc, c.child1 asc, c.child2 asc, c.child3 asc")

    List<Comment> findCommentList(Long musicListId);

    List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId, int divWidthSize);

    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch" +
            " c.member member where c.id= :commentId")
    Comment findCommentId(Long commentId);


    @Query("select c from Comment c inner join fetch c.musicList musicList inner join fetch " +
            " c.member member where c.musicList = :musicList and c.parent = :parent")
    List<Comment> findByParent(MusicList musicList, int parent);

    @Query("select c from Comment c inner join fetch c.musicList musicList inner  join fetch " +
            " c.member member where c.musicList = :musicList and c.parent = :parent " +
            " and c.child = :child ")
    List<Comment> findByParentAndChild(MusicList musicList, int parent, int child);


    @Query("select c from Comment c inner join fetch c.musicList musicList inner  join fetch " +
            " c.member member where c.musicList = :musicList and c.parent = :parent " +
            " and c.child = :child and c.child1 = :child1 ")
    List<Comment> findByParentAndChildAndChild1(MusicList musicList, int parent, int child, int child1);


    @Query("select c from Comment c inner join fetch c.musicList musicList inner  join fetch " +
            " c.member member where c.musicList = :musicList and c.parent = :parent " +
            " and c.child = :child and c.child1 = :child1 and c.child2 = :child2")
    List<Comment> findByParentAndChildAndChild1AndChild2(MusicList musicList, int parent, int child, int child1, int child2);

}


