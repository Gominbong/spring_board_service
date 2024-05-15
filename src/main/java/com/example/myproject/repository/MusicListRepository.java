package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicListRepository extends JpaRepository<MusicList, Long> {

    @Query("select musicList from MusicList musicList inner join fetch" +
            " musicList.member member where musicList.softDelete is null")
    Page<MusicList> findBySoftDeleteIsNull(Pageable pageable);

    @Query("select musicList from MusicList musicList inner join fetch" +
            " musicList.member member where musicList.id = :musicListId")
    MusicList findByMusicList(Long musicListId);

    @Query("select musicList from MusicList musicList inner join fetch" +
            " musicList.member member where musicList.softDelete is null and" +
            " musicList.title like concat('%', :title, '%')")
    Page<MusicList> findMusicListByTitleContains(Pageable pageable, String title);

    @Query("select musicList from MusicList musicList inner join fetch" +
            " musicList.member member where musicList.softDelete is null and" +
            " member.nickname like concat('%', :nickname, '%')")
    Page<MusicList> findMusicListByNicknameContains(Pageable pageable, String nickname);


}
