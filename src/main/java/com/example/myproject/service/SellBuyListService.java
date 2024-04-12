package com.example.myproject.service;

import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.SellBuyListRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SellBuyListService {
    private final SellBuyListRepository sellBuyListRepository;
    private final MusicListRepository musicListRepository;

    public void buyMusicList(HttpServletRequest request, Long id) {

        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        MusicList musicList = musicListRepository.findById(id).orElseThrow();

        SellBuyList sellBuyList = new SellBuyList();
        sellBuyList.setMusicList(musicList);
        sellBuyList.setBuyMemberLoginId(loginId);
        sellBuyList.setSellMemberLoginId(musicList.getLoginId());
        sellBuyList.setLocalDateTime(LocalDateTime.now().withNano(0));

        sellBuyListRepository.save(sellBuyList);
    }

    public SellBuyList myBuyInfo(MusicList musicList, String loginId) {
        return sellBuyListRepository.findByMusicListAndBuyMemberLoginId(musicList, loginId);
    }
}
