package com.example.myproject.repository;

import com.example.myproject.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    Member find(String loginId);


}
