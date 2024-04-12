package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellBuyListRepository extends JpaRepository<SellBuyList, Long> {

    SellBuyList findByMusicListAndBuyMemberLoginId(MusicList id, String loginId);
}
