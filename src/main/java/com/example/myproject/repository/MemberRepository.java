package com.example.myproject.repository;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.LoginFormDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public List<Member> checkLoginId(String loginId) {
        return em.createQuery("select m.loginId from Member m where m.loginId = :id", Member.class)
                .setParameter("id", loginId)
                .getResultList();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findEncodePassword(String loginId) {
        return em.createQuery("select m.password from Member m where m.loginId = :id", Member.class)
                .setParameter("id", loginId)
                .getResultList();
    }

    public List<Member> findById(String loginId) {
        return em.createQuery("select m.id from Member m where m.loginId = :id", Member.class)
                .setParameter("id", loginId)
                .getResultList();
    }
}