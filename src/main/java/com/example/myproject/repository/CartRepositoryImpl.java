package com.example.myproject.repository;

import com.example.myproject.domain.Cart;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;

import static com.example.myproject.domain.QCart.cart;
import static com.example.myproject.domain.QMember.member;
import static com.example.myproject.domain.QMusicList.musicList;

@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Cart> findCartListQueryDsl(String loginId) {
        return queryFactory
                .selectFrom(cart)
                .innerJoin(cart.musicList, musicList)
                .fetchJoin()
                .innerJoin(musicList.member, member)
                .fetchJoin()
                .where(cart.loginId.eq(loginId))
                .fetch();
    }

    @Override
    public Cart findByLoginIdAndMusicListIdQueryDsl(String loginId, Long musicListId) {
        return queryFactory
                .selectFrom(cart)
                .innerJoin(cart.musicList, musicList)
                .where(cart.loginId.eq(loginId), cart.musicList.id.eq(musicListId))
                .fetchOne();
    }
}
