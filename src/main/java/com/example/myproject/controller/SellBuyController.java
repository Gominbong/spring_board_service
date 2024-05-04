package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.dto.BuyMusicListDto;
import com.example.myproject.service.MemberService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SellBuyController {

    private final SellBuyListService sellBuyListService;
    private final MemberService memberService;

    @PostMapping("/buyMusicList")
    public String buyComplete(BuyMusicListDto buyMusicListDto, HttpServletRequest request,
                              HttpServletResponse response){
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        String referer = request.getHeader("Referer");
        log.info("이전 경로 확인 = {} ", referer);
        if(loginId == null){
            log.info("구매하시려면 로그인해주세요");
            Cookie cookie = new Cookie("url",referer);
            response.addCookie(cookie);
            return "redirect:/loginInterceptor";
        }
        Member result = sellBuyListService.buyMusicList(buyMusicListDto.getMusicListId(), loginId);

        if (result == null){
            log.info("잔액부족");
        }else{
            log.info("구매성공");

        }

        return "redirect:" + referer;
    }
    @GetMapping("/sellList")
    public String sellList(@RequestParam(value = "page", defaultValue = "0") int page,
                       HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        Member member = memberService.findByLoginId(loginId);
        model.addAttribute("member", member);
        Page<SellBuyList> paging = sellBuyListService.findSellList(page, loginId);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());
        int temp = page / 7;
        int start = temp * 7;

        if (paging.getTotalPages() == 0) {
            log.info("여기11111 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages());
        } else if (paging.getTotalPages() - start > 7) {
            log.info("여기22222 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", start + 6);
        } else if (paging.getTotalPages() < 6){
            log.info("여기33333 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        }



        log.info("스타트 페이지 확인 해보기 = '{}' ", start);

        log.info("스타트 페이지 확인 해보기 = '{}' ", start);


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
            log.info("SellBuyList 테이블 구매자아이디 = {}", sellBuyList.getBuyMember().getLoginId());
            log.info("SellBuyList 테이블 판매자아이디 = {}", sellBuyList.getSellMember().getLoginId());
            log.info("SellBuyList 테이블 구매판매시간 = {}", sellBuyList.getCreateTime());
            log.info("SellBuyList 테이블 구매판매시간 = {}", sellBuyList.getMusicList());

            log.info("MusicList 테이블 기본키Id = {}", sellBuyList.getMusicList().getId());
            log.info("MusicList 테이블 제목 = {}", sellBuyList.getMusicList().getTitle());
            log.info("MusicList 테이블 글쓴이닉네임 = {}", sellBuyList.getMusicList().getMember().getNickname());
        }

        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());
        int temp = page / 7;
        int start = temp * 7;

        if (paging.getTotalPages() == 0) {
            log.info("여기11111 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages());
        } else if (paging.getTotalPages() - start > 7) {
            log.info("여기22222 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", start + 6);
        } else if (paging.getTotalPages() < 6){
            log.info("여기33333 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        }

        log.info("스타트 페이지 확인 해보기 = '{}' ", start);


        return "/musicList/buyMusicListForm";
    }

}
