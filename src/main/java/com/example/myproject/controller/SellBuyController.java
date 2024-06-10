package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.dto.BuyMusicListDto;
import com.example.myproject.service.MemberService;
import com.example.myproject.service.MusicListService;
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
import static com.example.myproject.controller.LoginMember.loginMember;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SellBuyController {

    private final SellBuyListService sellBuyListService;
    private final MemberService memberService;
    private final MusicListService musicListService;

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
            return "redirect:login";
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
        sellBuyListService.pageStartEndNumber(page, paging, model);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);

        return "musicList/sellMusicListForm";
    }

    @GetMapping("/buyList")
    public String buyList(@RequestParam(value = "page", defaultValue = "0") int page,
                          HttpServletRequest request, Model model) {
        model.addAttribute("menu", "buyList");
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        Page<SellBuyList> paging = sellBuyListService.findBuyList(page, loginId);
        sellBuyListService.pageStartEndNumber(page, paging, model);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);


        return "musicList/buyMusicListForm";
    }

}
