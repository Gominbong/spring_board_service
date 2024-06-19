package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import static com.example.myproject.domain.QMember.member;
import static com.example.myproject.domain.QMusicList.musicList;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class MusicListRepositoryImpl implements MusicListRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MusicList> findBySoftDeleteIsNullQueryDsl(Pageable pageable) {
        List<MusicList> result = queryFactory
                .selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.softDelete.isNull())
                .orderBy(musicList.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public MusicList findByMusicListQueryDsl(Long musicListId) {
        return queryFactory.selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.id.eq(musicListId))
                .fetchOne();
    }

    @Override
    public Page<MusicList> findMusicListByTitleContainsQueryDsl(Pageable pageable, String title) {
        List<MusicList> result = queryFactory
                .selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.softDelete.isNull(), musicList.title.like("%" + title + "%"))
                .fetch();
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Page<MusicList> findMusicListByNicknameContainsQueryDsl(Pageable pageable, String nickname) {
        List<MusicList> result = queryFactory
                .selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.softDelete.isNull(), member.nickname.like("%" + nickname + "%"))
                .fetch();
        return new PageImpl<>(result, pageable, result.size());
    }
}
