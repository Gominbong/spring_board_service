package com.example.myproject.repository;

import com.example.myproject.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c,m from Cart c inner join c.musicList m where c.loginId = :loginId")
    List<Cart> findCartList(String loginId);

    Cart findByLoginIdAndMusicListId(String loginId, Long musicListId);
}
