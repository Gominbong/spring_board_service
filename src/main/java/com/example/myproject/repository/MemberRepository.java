package com.example.myproject.repository;

import com.example.myproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId = :id")
    Member findEncodePassword(String id);

    Member findByLoginId(String loginId);
    Member findByNickname(String nickname);

}
