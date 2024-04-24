package com.example.myproject.service;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.repository.MemberRepository;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.SellBuyListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellBuyListService {
    private final SellBuyListRepository sellBuyListRepository;
    private final MusicListRepository musicListRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Member buyMusicList(Long id, String loginId) {

        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        Member BuyMember = memberRepository.findByLoginId(loginId);
        Member sellMember = memberRepository.findByLoginId(musicList.getLoginId());
        if (BuyMember.getCash() >= musicList.getPrice()){
            SellBuyList sellBuyList = new SellBuyList();
            sellBuyList.setMusicList(musicList);
            sellBuyList.setBuyMemberLoginId(loginId);
            sellBuyList.setSellMemberLoginId(musicList.getLoginId());
            sellBuyList.setLocalDate(LocalDate.now());
            sellBuyListRepository.save(sellBuyList);
            musicList.setSalesQuantity(musicList.getSalesQuantity()+1);
            BuyMember.setCash(BuyMember.getCash() - musicList.getPrice());
            sellMember.setRevenue(sellMember.getRevenue() + musicList.getPrice());
        }else{
           return null;
        }

        return  BuyMember;
    }


    public SellBuyList myBuyInfo(MusicList musicList, String loginId) {
        return sellBuyListRepository.findByMusicListAndBuyMemberLoginId(musicList, loginId);
    }

    public Page<SellBuyList> findBuyList(int page, String loginId) {
        Pageable pageable = PageRequest.of(page, 15);
        return sellBuyListRepository.findMyBuyList(pageable, loginId);
    }

    public Page<SellBuyList> findSellList(int page, String loginId) {
        Pageable pageable = PageRequest.of(page, 15);
        return sellBuyListRepository.findMySellList(pageable, loginId);
    }

}
