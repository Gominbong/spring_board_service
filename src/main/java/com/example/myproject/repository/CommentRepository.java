package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository <Comment, Long> {



    @Query("select c from Comment c inner join fetch" +
            " c.musicList m where m.id = :musicListId order by c.parent, c.child1, c.child2, c. child3, c.child4")
    List<Comment> findMusicListId(Long musicListId);

    List<Comment> findByMusicListIdAndDivWidthSize(Long musicListId, int divWidthSize);

}
