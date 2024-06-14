package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.dto.BuyMusicListDto;
import com.example.myproject.service.LoginService;
import com.example.myproject.service.MemberService;
import com.example.myproject.service.SellBuyListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final LoginService loginService;

    @PostMapping("/buyMusicList")
    public String buyComplete(BuyMusicListDto buyMusicListDto, HttpServletRequest request,
                              HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        log.info("이전 경로 확인 = {} ", referer);
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        if (loginId == null) {
            log.info("구매실패 jwt 로그인 유지시간 초과");
            return "redirect:" + referer;
        } else {
            sellBuyListService.buyMusicList(buyMusicListDto.getMusicListId(), loginId);
        }

        return "redirect:" + referer;
    }

    @GetMapping("/sellList")
    public String sellList(@RequestParam(value = "page", defaultValue = "0") int page, HttpServletResponse response,
                           HttpServletRequest request, Model model) {
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
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
    public String buyList(@RequestParam(value = "page", defaultValue = "0") int page, HttpServletResponse response,
                          HttpServletRequest request, Model model) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        model.addAttribute("loginId", loginId);

        Page<SellBuyList> paging = sellBuyListService.findBuyList(page, loginId);
        sellBuyListService.pageStartEndNumber(page, paging, model);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);

        return "musicList/buyMusicListForm";
    }

}
