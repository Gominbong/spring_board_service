package com.example.myproject.repository;

import com.example.myproject.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static com.example.myproject.domain.QMember.member;

@RequiredArgsConstructor
@Slf4j
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Member findEncodePasswordQueryDsl(String id) {

        return queryFactory
                .selectFrom(member)
                .where(member.loginId.eq(id))
                .fetchOne();
    }

    @Override
    public Member findByLoginIdQueryDsl(String loginId) {

        if (loginId != null) {
            return queryFactory
                    .selectFrom(member)
                    .where(member.loginId.eq(loginId))
                    .fetchOne();
        }else{
            return null;
        }
    }

    @Override
    public Member findByNicknameQueryDsl(String nickname) {

        if (nickname != null){
            return queryFactory
                    .selectFrom(member)
                    .where(member.nickname.eq(nickname))
                    .fetchOne();
        }else{
            return null;
        }
    }
}
