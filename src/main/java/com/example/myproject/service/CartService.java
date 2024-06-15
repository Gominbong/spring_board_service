package com.example.myproject.service;

import com.example.myproject.domain.Cart;
import com.example.myproject.domain.MusicList;
import com.example.myproject.repository.CartRepository;
import com.example.myproject.repository.MusicListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final MusicListRepository musicListRepository;

    public Cart createCart(Long id, String loginId) {
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        Cart result = cartRepository.findByLoginIdAndMusicListId(loginId, id);

        if (result == null){
            Cart cart = new Cart();
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            cart.setCreateTime(createTime);
            cart.setLoginId(loginId);
            cart.setMusicList(musicList);
            cartRepository.save(cart);
            log.info("장바구니 추가 완료");
            return cart;
        }else{
            log.info("장바구니에 이미 들어가있음");
            return null;
        }
    }


    public List<Cart> findCartList(String loginId) {
        return cartRepository.findCartList(loginId);

    }

    public void deleteCartList(Long cartListId) {
        log.info("장바구니 삭제완료");
        cartRepository.deleteById(cartListId);
    }
}
