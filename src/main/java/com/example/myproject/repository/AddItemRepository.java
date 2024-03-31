package com.example.myproject.repository;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Repository
@Transactional
public class AddItemRepository {

    private final EntityManager em;

    public void save(MusicList musicList) {
        em.persist(musicList);
    }

    public void fileSave(FileList fileList) {
        em.persist(fileList);
    }
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<MusicList> findAll() {
        return em.createQuery("select musicList from MusicList musicList", MusicList.class)
                .getResultList();
    }



}
