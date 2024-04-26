package com.example.myproject.service;

import com.example.myproject.domain.LikeCount;
import com.example.myproject.domain.MusicList;
import com.example.myproject.repository.LikeCountRepository;
import com.example.myproject.repository.MusicListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LikeCountService {
    private final LikeCountRepository likeCountRepository;
    private final MusicListRepository musicListRepository;

    @Transactional
    public LikeCount like(Long id, String loginId){
        String musicListId = String.valueOf(id);
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        LikeCount result = likeCountRepository.findByMusicListIdAndLoginId(musicListId, loginId);

        if (result == null) {
            LikeCount likeCount = new LikeCount();
            likeCount.setLoginId(loginId);
            likeCount.setMusicListId(musicListId);
            musicList.setLikeCount(musicList.getLikeCount() + 1);
            likeCountRepository.save(likeCount);

            return likeCount;
        }else {
            return null;
        }
    }

    public LikeCount findMyLike(Long id, String loginId) {
        String musicListId = String.valueOf(id);
        return likeCountRepository.findByMusicListIdAndLoginId(musicListId, loginId);

    }
}
