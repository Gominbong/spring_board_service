package com.example.myproject.repository;

import com.example.myproject.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> findLoginId(String id) {
        return em.createQuery("select a from Member a where a.loginId = :id", Member.class)
                .setParameter("id", id)
                .getResultList();
    }

}

