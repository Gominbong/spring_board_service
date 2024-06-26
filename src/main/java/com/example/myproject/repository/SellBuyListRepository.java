package com.example.myproject.repository;

import com.example.myproject.domain.SellBuyList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellBuyListRepository extends JpaRepository<SellBuyList, Long>, SellBuyListRepositoryCustom {


    SellBuyList findByMusicListIdAndBuyMemberLoginId(Long musicListId, String loginId);

    @Query("select s from SellBuyList s inner join fetch s.musicList musicList inner join fetch" +
            " s.buyMember buyMember inner join fetch s.sellMember sellMember" +
            " where s.buyMember.loginId = :loginId")
    Page<SellBuyList> findMyBuyList(Pageable pageable, String loginId);

    @Query("select s from SellBuyList s inner join fetch s.musicList musicList inner join fetch" +
            " s.buyMember buyMember inner join fetch s.sellMember sellMember" +
            " where s.sellMember.loginId = :loginId")
    Page<SellBuyList> findMySellList(Pageable pageable, String loginId);
}
