package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import static com.example.myproject.domain.QMember.member;
import static com.example.myproject.domain.QMusicList.musicList;
import com.example.myproject.dto.HomeSortDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.List;

@RequiredArgsConstructor
public class MusicListRepositoryImpl implements MusicListRepositoryCustom {

    @Override
    public Page<MusicList> HomeSortFindBySoftDeleteIsNullQueryDsl(Pageable pageable, HomeSortDto homeSortDto) {
        List<Long> count = queryFactory
                .select(musicList.count())
                .from(musicList)
                .where(musicList.softDelete.isNull())
                .fetch();

        if (homeSortDto.getSortType().equals("sortSelect")) {
            List<MusicList> result = queryFactory
                    .selectFrom(musicList)
                    .innerJoin(musicList.member, member)
                    .fetchJoin()
                    .where(musicList.softDelete.isNull())
                    .orderBy(musicList.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new PageImpl<>(result, pageable, count.get(0));
        }
        if (homeSortDto.getSortType().equals("sortPrice")) {
            List<MusicList> result = queryFactory
                    .selectFrom(musicList)
                    .innerJoin(musicList.member, member)
                    .fetchJoin()
                    .where(musicList.softDelete.isNull())
                    .orderBy(musicList.price.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new PageImpl<>(result, pageable, count.get(0));
        }
        if (homeSortDto.getSortType().equals("sortLike")) {
            List<MusicList> result = queryFactory
                    .selectFrom(musicList)
                    .innerJoin(musicList.member, member)
                    .fetchJoin()
                    .where(musicList.softDelete.isNull())
                    .orderBy(musicList.likeCount.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new PageImpl<>(result, pageable, count.get(0));
        }
        if (homeSortDto.getSortType().equals("sortQuantity")) {
            List<MusicList> result = queryFactory
                    .selectFrom(musicList)
                    .innerJoin(musicList.member, member)
                    .fetchJoin()
                    .where(musicList.softDelete.isNull())
                    .orderBy(musicList.salesQuantity.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            return new PageImpl<>(result, pageable, count.get(0));
        }

        return null;
    }

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MusicList> findBySoftDeleteIsNullQueryDsl(Pageable pageable) {
        List<Long> count = queryFactory
                .select(musicList.count())
                .from(musicList)
                .where(musicList.softDelete.isNull())
                .fetch();

        Sort sort = pageable.getSort();

        List<MusicList> result = queryFactory
                .selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.softDelete.isNull())
                .orderBy(musicList.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, count.get(0));
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

        List<Long> count = queryFactory
                .select(musicList.count())
                .from(musicList)
                .where(musicList.softDelete.isNull(), musicList.title.like("%" + title + "%"))
                .fetch();

        List<MusicList> result = queryFactory
                .selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.softDelete.isNull(), musicList.title.like("%" + title + "%"))
                .orderBy(musicList.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, count.get(0));
    }

    @Override
    public Page<MusicList> findMusicListByNicknameContainsQueryDsl(Pageable pageable, String nickname) {

        List<Long> count = queryFactory
                .select(musicList.count())
                .from(musicList)
                .innerJoin(musicList.member, member)
                .where(musicList.softDelete.isNull(), member.nickname.like("%" + nickname + "%"))
                .fetch();

        List<MusicList> result = queryFactory
                .selectFrom(musicList)
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(musicList.softDelete.isNull(),  member.nickname.like("%" + nickname + "%"))
                .orderBy(musicList.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, count.get(0));
    }
}
