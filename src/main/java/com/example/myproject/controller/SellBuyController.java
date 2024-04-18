package com.example.myproject.controller;

import com.example.myproject.service.SellBuyListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        sellBuyListService.buyMusicList(id, loginId);
        log.info("구매성공 = '{}'", id);
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
    public String buyList(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);

        return "/musicList/buyMusicListForm";
    }

}
