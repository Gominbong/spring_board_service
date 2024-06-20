package com.example.myproject.repository;

import com.example.myproject.domain.Member;
public interface MemberRepositoryCustom {
    Member findByLoginIdQueryDsl(String loginId);

    Member findByNicknameQueryDsl(String nickname);

    Member findEncodePasswordQueryDsl(String id);


}
