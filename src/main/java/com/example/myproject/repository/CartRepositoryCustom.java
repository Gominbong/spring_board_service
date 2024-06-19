package com.example.myproject.repository;

import com.example.myproject.domain.Cart;

import java.util.List;

public interface CartRepositoryCustom {

    List<Cart> findCartListQueryDsl(String loginId);

    Cart findByLoginIdAndMusicListIdQueryDsl(String loginId, Long musicListId);
}
