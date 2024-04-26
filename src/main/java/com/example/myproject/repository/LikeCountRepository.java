package com.example.myproject.repository;

import com.example.myproject.domain.LikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {
    LikeCount findByMusicListIdAndLoginId(String musicListId, String loginId);
}
