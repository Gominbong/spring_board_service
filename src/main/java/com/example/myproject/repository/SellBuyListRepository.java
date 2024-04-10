package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellBuyListRepository extends JpaRepository<SellBuyList, Long> {

    @Query("select s from SellBuyList s where s.musicList = :id and s.buyMemberLoginId = :loginId")
    SellBuyList findMyBuyList(MusicList id, String loginId);
}
