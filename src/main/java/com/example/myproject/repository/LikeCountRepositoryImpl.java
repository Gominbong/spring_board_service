package com.example.myproject.repository;

import com.example.myproject.domain.LikeCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.example.myproject.domain.QLikeCount.likeCount;

@RequiredArgsConstructor
public class LikeCountRepositoryImpl implements LikeCountRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public LikeCount findByMusicListIdAndLoginIdQueryDsl(Long musicListId, String loginId) {
        return queryFactory
                .selectFrom(likeCount)
                .where(likeCount.musicListId.eq(musicListId), likeCount.loginId.eq(loginId))
                .fetchOne();
    }
}
