package com.example.myproject.repository;

import com.example.myproject.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;



import static com.example.myproject.domain.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member find(String loginId) {

        return queryFactory
                .selectFrom(member)
                .where(member.loginId.eq(loginId))
                .fetchOne();
    }


}
