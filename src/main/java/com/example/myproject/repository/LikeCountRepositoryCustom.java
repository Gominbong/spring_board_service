package com.example.myproject.repository;

import com.example.myproject.domain.LikeCount;

public interface LikeCountRepositoryCustom {
    LikeCount findByMusicListIdAndLoginIdQueryDsl(Long musicListId, String loginId);
}
