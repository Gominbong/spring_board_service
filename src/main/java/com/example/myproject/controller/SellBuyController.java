package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.service.SellBuyListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SellBuyController {

    private final SellBuyListService sellBuyListService;

    @GetMapping("/buyMusicList")
    public String buyComplete(@RequestParam("musicListId") Long id,
                              HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        if(loginId == null){
            log.info("구매하시려면 로그인해주세요");
            String referer = request.getHeader("Referer");
            log.info("이전 경로 확인 = {} ", referer);
            Cookie cookie = new Cookie("url",referer);
            response.addCookie(cookie);
            return"redirect:/loginInterceptor";
        }
        Member result = sellBuyListService.buyMusicList(id, loginId);

        if (result == null){
            log.info("잔액부족");
        }else{
            log.info("구매성공");
        }

        return "redirect:/content?musicListId="+id;
    }
    @GetMapping("/sellList")
    public String cart(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);

        return "/musicList/sellMusicListForm";
    }

    @GetMapping("/buyList")
    public String buyList(@RequestParam(value = "page", defaultValue = "0") int page,
                          HttpServletRequest request, Model model) {
        model.addAttribute("menu", "buyList");
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);

        log.info("페이지 정보 확인 = '{}' ", page);

        Page<SellBuyList> paging = sellBuyListService.findBuyList(page, loginId);
        for (SellBuyList sellBuyList : paging) {
            log.info("SellBuyList 테이블 기본키Id = {}", sellBuyList.getId());
            log.info("SellBuyList 테이블 구매자아이디 = {}", sellBuyList.getBuyMemberLoginId());
            log.info("SellBuyList 테이블 판매자아이디 = {}", sellBuyList.getSellMemberLoginId());
            log.info("SellBuyList 테이블 구매판매시간 = {}", sellBuyList.getLocalDateTime());
            log.info("SellBuyList 테이블 구매판매시간 = {}", sellBuyList.getMusicList());

            log.info("MusicList 테이블 기본키Id = {}", sellBuyList.getMusicList().getId());
            log.info("MusicList 테이블 제목 = {}", sellBuyList.getMusicList().getTitle());
            log.info("MusicList 테이블 글쓴이닉네임 = {}", sellBuyList.getMusicList().getMemberNickname());
        }

        model.addAttribute("page", page);
        model.addAttribute("paging", paging);


        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());

        int temp=page/7;
        int start = temp* 7;

        if (paging.getTotalPages() - start > 7 ){
            model.addAttribute("start", start);
            model.addAttribute("end", start+6);
        }else{
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        }

        log.info("스타트 페이지 확인 해보기 = '{}' ", start);

        return "/musicList/buyMusicListForm";
    }

}
