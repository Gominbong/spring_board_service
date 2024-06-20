package com.example.myproject.repository;

import com.example.myproject.domain.QMember;
import com.example.myproject.domain.SellBuyList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.myproject.domain.QMember.member;
import static com.example.myproject.domain.QMusicList.musicList;
import static com.example.myproject.domain.QSellBuyList.sellBuyList;

@RequiredArgsConstructor
@Slf4j
public class SellBuyListRepositoryImpl implements SellBuyListRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public SellBuyList findByMusicListIdAndBuyMemberLoginIdQueryDsl(Long musicListId, String loginId) {
        return queryFactory.selectFrom(sellBuyList)
                .innerJoin(sellBuyList.musicList, musicList)
                .fetchJoin()
                .innerJoin(sellBuyList.buyMember, member)
                .fetchJoin()
                .where(musicList.id.eq(musicListId), member.loginId.eq(loginId))
                .fetchOne();
    }

    @Override
    public Page<SellBuyList> findMyBuyListQueryDsl(Pageable pageable, String loginId) {
        QMember buyMember = new QMember("buyMember");
        QMember sellMember = new QMember("sellMember");

        List<Long> count = queryFactory
                .select(sellBuyList.count())
                .from(sellBuyList)
                .innerJoin(sellBuyList.musicList, musicList)
                .innerJoin(sellBuyList.buyMember, buyMember)
                .innerJoin(sellBuyList.sellMember, sellMember)
                .where(sellBuyList.buyMember.loginId.eq(loginId))
                .fetch();

        List<SellBuyList> result = queryFactory
                .selectFrom(sellBuyList)
                .innerJoin(sellBuyList.musicList, musicList)
                .fetchJoin()
                .innerJoin(sellBuyList.buyMember, buyMember)
                .fetchJoin()
                .innerJoin(sellBuyList.sellMember, sellMember)
                .fetchJoin()
                .where(sellBuyList.buyMember.loginId.eq(loginId))
                .orderBy(sellBuyList.buyMember.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(result, pageable, count.get(0));
    }

    @Override
    public Page<SellBuyList> findMySellListQueryDsl(Pageable pageable, String loginId) {
        QMember buyMember = new QMember("buyMember");
        QMember sellMember = new QMember("sellMember");

        List<Long> count = queryFactory
                .select(sellBuyList.count())
                .from(sellBuyList)
                .innerJoin(sellBuyList.musicList, musicList)
                .innerJoin(sellBuyList.buyMember, buyMember)
                .innerJoin(sellBuyList.sellMember, sellMember)
                .where(sellBuyList.sellMember.loginId.eq(loginId))
                .fetch();
        log.info("판매횟수 = {}", count.get(0));

        List<SellBuyList> result = queryFactory
                .selectFrom(sellBuyList)
                .innerJoin(sellBuyList.musicList, musicList)
                .fetchJoin()
                .innerJoin(sellBuyList.buyMember, buyMember)
                .fetchJoin()
                .innerJoin(sellBuyList.sellMember, sellMember)
                .fetchJoin()
                .where(sellBuyList.sellMember.loginId.eq(loginId))
                .orderBy(sellBuyList.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(result, pageable, count.get(0));
    }
}
