package com.example.myproject.repository;

import com.example.myproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId = :id")
    Member findEncodePassword(@Param("id") String id);

    Member findByLoginId(String loginId);


}
