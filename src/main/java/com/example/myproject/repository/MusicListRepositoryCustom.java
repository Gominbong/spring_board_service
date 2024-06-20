package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MusicListRepositoryCustom {


    Page<MusicList> findBySoftDeleteIsNullQueryDsl(Pageable pageable);

    MusicList findByMusicListQueryDsl(Long musicListId);

    Page<MusicList> findMusicListByTitleContainsQueryDsl(Pageable pageable, String title);

    Page<MusicList> findMusicListByNicknameContainsQueryDsl(Pageable pageable, String nickname);
}
