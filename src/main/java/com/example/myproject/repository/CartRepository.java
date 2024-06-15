package com.example.myproject.repository;

import com.example.myproject.domain.Cart;
import com.example.myproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart inner join fetch cart.musicList musicList inner join fetch" +
            " musicList.member member where cart.loginId = :loginId")
    List<Cart> findCartList(String loginId);

    Cart findByLoginIdAndMusicListId(String loginId, Long musicListId);
}
