package com.example.myproject.repository;

import com.example.myproject.domain.QMember;
import com.example.myproject.domain.SellBuyList;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.myproject.domain.QMember.member;
import static com.example.myproject.domain.QMusicList.musicList;
import static com.example.myproject.domain.QSellBuyList.sellBuyList;

@RequiredArgsConstructor
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
                .fetch();
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public Page<SellBuyList> findMySellListQueryDsl(Pageable pageable, String loginId) {
        QMember buyMember = new QMember("buyMember");
        QMember sellMember = new QMember("sellMember");

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
                .fetch();
        return new PageImpl<>(result, pageable, result.size());
    }
}
