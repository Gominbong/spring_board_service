package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicListRepository extends JpaRepository<MusicList, Long> {



    @Query("select m from MusicList m where m.content is not null")
    Page<MusicList> findContentNotNull(Pageable pageable);

    @Query("select m from MusicList m where m.content is null")
    MusicList findContentIsNull();
}
