package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SellBuyListRepository extends JpaRepository<SellBuyList, Long> {

    SellBuyList findByMusicListAndBuyMemberLoginId(MusicList id, String loginId);

    @Query("select s from SellBuyList s inner join fetch s.musicList m where s.buyMemberLoginId = :loginId")
    Page<SellBuyList> findMyBuyList(Pageable pageable, String loginId);

}
