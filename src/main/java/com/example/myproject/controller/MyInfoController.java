package com.example.myproject.controller;

import com.example.myproject.domain.Cart;
import com.example.myproject.domain.Member;
import com.example.myproject.dto.*;
import com.example.myproject.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MyInfoController {

    private final MemberService memberService;
    private final CartService cartService;

    private final MyInfoService myInfoService;
    private final SellBuyListService sellBuyListService;
    private final LoginService loginService;

    @PostMapping("/cartBuy")
    public String cartBuy(CartDto cartDto, HttpServletRequest request, HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        if (loginId == null) {
            log.info("장바구니 1개 구매 실패 jwt 로그인 유지시간 초과");
        } else {
            Member result = sellBuyListService.buyMusicList(cartDto.getMusicListId(), loginId);
            if (result != null) {

                cartService.deleteCartList(cartDto.getCartListId(), loginId);
            }
        }


        return "redirect:cart";
    }

    @PostMapping("/cartBuyMulti")
    public String cartBuyMulti(CartBuyMultiDto cartBuyMultiDto, HttpServletRequest request,
                               HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);

        if (loginId == null) {
            log.info("장바구니 체크목록 구매 실패 jwt 로그인 유지시간 초과");
        } else {
            log.info(cartBuyMultiDto.toString());
            List<Long> musicListId = cartBuyMultiDto.getMusicListId();

            for (Long id : musicListId) {
                sellBuyListService.buyMusicList(id, loginId);
            }

            List<Long> cartListId = cartBuyMultiDto.getCartListId();
            for (Long id : cartListId) {
                cartService.deleteCartList(id, loginId);
            }
        }

        return "redirect:cart";
    }

    @PostMapping("/cartDeleteMulti")
    public String cartDeleteMulti(CartDeleteMultiDto cartDeleteMultiDto, HttpServletRequest request,
                                  HttpServletResponse response) {
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        if (loginId == null) {
            log.info("장바구니 체크목록 삭제실패 jwt 로그인 유지시간 초과");
        } else {
            log.info(cartDeleteMultiDto.toString());
            List<Long> cartListId = cartDeleteMultiDto.getCartListId();
            for (Long id : cartListId) {
                cartService.deleteCartList(id, loginId);
            }
        }

        return "redirect:cart";
    }

    @PostMapping("/cartDelete")
    public String cartDelete(@RequestParam("cartId") Long id, HttpServletRequest request,
                             HttpServletResponse response) {
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        if (loginId == null) {
            log.info("장바구니 삭제실패 jwt 로그인 유지시간 초과");
        } else {
            cartService.deleteCartList(id, loginId);
        }

        return "redirect:cart";
    }

    @PostMapping("/cartAdd")
    public String cartAdd(@RequestParam("musicListId") Long id, HttpServletRequest request,
                          HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);

        if (loginId == null) {
            log.info("장바구니 추가 실패 jwt 로그인 유지시간 초과");
        } else {
            cartService.createAddCart(id, loginId);
        }

        return "redirect:cart";
    }

    @GetMapping("/cart")
    public String cart(HttpServletRequest request, Model model, HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        Member loginMember = memberService.findByLoginId(loginId);
        model.addAttribute("member", loginMember);
        log.info("로그인 아이디 확인 = {}", loginId);

        List<Cart> cartList = cartService.findCartList(loginId);
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            model.addAttribute("memberCash", -1);
        } else {
            Integer memberCash = member.getCash();
            model.addAttribute("memberCash", memberCash);
        }
        model.addAttribute("cartList", cartList);


        return "login/cartForm";
    }

    @GetMapping("/myInfo")
    public String myInfo(HttpServletRequest request, Model model, HttpServletResponse response) {
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        Member loginMember = memberService.findByLoginId(loginId);
        model.addAttribute("member", loginMember);


        return "login/myInfoForm";
    }


    @PostMapping("/myInfoEdit")
    public String myInfoEdit(MyInfoEditDto myInfoEditDto, HttpServletRequest request, Model model,
                             HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);

        if (loginId == null) {
            log.info("jwt 로그인 유지시간 초과");
            return "redirect:" + referer;
        } else {
            Map<String, String> errors = myInfoService.myInfoNicknameEdit(myInfoEditDto, loginId);
            if (errors != null) {
                model.addAttribute("errors", errors);
                Member loginMember = memberService.findByLoginId(loginId);
                model.addAttribute("loginMember", loginMember);

                Member member = memberService.findByLoginId(loginId);

                model.addAttribute("member", member);
                return "login/myInfoForm";
            }
        }


        return "redirect:myInfo";
    }


    @PostMapping("/addCash")
    public String addCash(AddCashDto addCashDto, HttpServletRequest request, Model model,
                          HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인 아이디 확인 = {}", loginId);
        if (loginId == null) {
            return "redirect:" + referer;
        } else {
            Member loginMember = memberService.findByLoginId(loginId);
            model.addAttribute("member", loginMember);
            Map<String, String> errors = memberService.addCash(loginId, addCashDto);

            if (errors != null) {
                model.addAttribute("errors", errors);
                model.addAttribute("loginId", loginId);
                model.addAttribute("member", loginMember);
                return "login/myInfoForm";
            }
        }

        return "redirect:myInfo";
    }


}
