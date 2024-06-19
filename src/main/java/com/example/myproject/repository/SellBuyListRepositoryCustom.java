package com.example.myproject.repository;


import com.example.myproject.domain.SellBuyList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SellBuyListRepositoryCustom {

    SellBuyList findByMusicListIdAndBuyMemberLoginIdQueryDsl(Long musicListId, String loginId);

    Page<SellBuyList> findMyBuyListQueryDsl(Pageable pageable, String loginId);

    Page<SellBuyList> findMySellListQueryDsl(Pageable pageable, String loginId);
}

