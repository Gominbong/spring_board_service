package com.example.myproject.service;

import com.example.myproject.domain.Cart;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.repository.CartRepository;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.SellBuyListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SellBuyListService {
    private final SellBuyListRepository sellBuyListRepository;
    private final MusicListRepository musicListRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    @Transactional
    public Member buyMusicList(Long id, String loginId) {


        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        Member buyMember = memberRepository.findByLoginId(loginId);
        Member sellMember = memberRepository.findById(musicList.getMember().getId()).orElseThrow();

        Cart cart = cartRepository.findByLoginIdAndMusicListId(loginId, id);
        if (cart != null){
            cartRepository.delete(cart);
        }
        if (buyMember.getCash() >= musicList.getPrice()){
            SellBuyList sellBuyList = new SellBuyList();
            sellBuyList.setMusicList(musicList);
            sellBuyList.setBuyMember(buyMember);
            sellBuyList.setSellMember(sellMember);
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            String temp = String.valueOf(localDateTime);
            String createTime = temp.replace("T", " ");
            sellBuyList.setCreateTime(createTime);
            sellBuyListRepository.save(sellBuyList);
            musicList.setSalesQuantity(musicList.getSalesQuantity()+1);
            buyMember.setCash(buyMember.getCash() - musicList.getPrice());
            sellMember.setRevenue(sellMember.getRevenue() + musicList.getPrice());
        }else{
           return null;
        }

        return  buyMember;
    }


    public SellBuyList myBuyInfo(Long id, String loginId) {
        return sellBuyListRepository.findByMusicListIdAndBuyMemberLoginId(id, loginId);
    }

    public Page<SellBuyList> findBuyList(int page, String loginId) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
        return sellBuyListRepository.findMyBuyList(pageable, loginId);
    }

    public Page<SellBuyList> findSellList(int page, String loginId) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
        return sellBuyListRepository.findMySellList(pageable, loginId);
    }

}
