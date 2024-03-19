package com.example.myproject.repository;

import com.example.myproject.domain.member.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<Member> checkLoginId1(String loginId){
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
 public List<Member> findEncodePassword(String loginId) {
     return em.createQuery("select m.password from Member m where m.loginId = :id", Member.class)
             .setParameter("id", loginId)
             .getResultList();
 }


    public List<Member> checkLogin(String loginId, String password){
        return em.createQuery("select m from Member m where m.loginId = :id and m.password = : pw")
                .setParameter("id", loginId)
                .setParameter("pw", password)
                .getResultList();
    }

}