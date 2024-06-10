package com.example.myproject.controller;

import com.example.myproject.domain.Cart;
import com.example.myproject.domain.Member;
import com.example.myproject.dto.*;
import com.example.myproject.service.CartService;
import com.example.myproject.service.MemberService;
import com.example.myproject.service.MyInfoService;
import com.example.myproject.service.SellBuyListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;
import static com.example.myproject.controller.LoginMember.loginMember;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MyInfoController {

    private final MemberService memberService;
    private final CartService cartService;

    private final MyInfoService myInfoService;
    private final SellBuyListService sellBuyListService;

    @PostMapping("/cartBuy")
    public String cartBuy(CartDto cartDto, HttpServletRequest request){

        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        Member result = sellBuyListService.buyMusicList(cartDto.getMusicListId(), loginId);
        if (result != null){
            cartService.deleteCartList(cartDto.getCartListId());
        }

        return "redirect:cart";
    }

    @PostMapping("/cartBuyMulti")
    public String cartBuyMulti(CartBuyMultiDto cartBuyMultiDto, HttpServletRequest request){
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        log.info(cartBuyMultiDto.toString());
        List<Long> musicListId = cartBuyMultiDto.getMusicListId();

        for (Long id : musicListId) {
            sellBuyListService.buyMusicList(id, loginId);
        }

        List<Long> cartListId = cartBuyMultiDto.getCartListId();
        for (Long id : cartListId) {
            cartService.deleteCartList(id);
        }

        return "redirect:cart";
    }

    @PostMapping("/cartDeleteMulti")
    public String cartDeleteMulti(CartDeleteMultiDto cartDeleteMultiDto){
        log.info(cartDeleteMultiDto.toString());
        List<Long> cartListId = cartDeleteMultiDto.getCartListId();
        for (Long id : cartListId) {
            cartService.deleteCartList(id);
        }
        return "redirect:cart";
    }

    @PostMapping("/cartDelete")
    public String cartDelete(@RequestParam("cartId") Long id){

        cartService.deleteCartList(id);
        return "redirect:cart";
    }

    @PostMapping("/cartAdd")
    public String cartAdd(@RequestParam("musicListId") Long id, HttpServletRequest request){

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        Cart cart = cartService.createCart(id, loginId);

        return "redirect:cart";
    }

    @GetMapping("/cart")
    public String cart(HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        List<Cart> cartList = cartService.findCartList(loginId);
        Member member = memberService.findByLoginId(loginId);
        if (member == null){
            model.addAttribute("memberCash", -1);
        }else{
            Integer memberCash = member.getCash();
            model.addAttribute("memberCash", memberCash);
        }

        model.addAttribute("cartList", cartList);

        return "login/cartForm";
    }

    @GetMapping("/myInfo")
    public String myInfo(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String loginId = (String)session.getAttribute("loginId");
            model.addAttribute("loginId", loginId);
            Member member = memberService.findByLoginId(loginId);

            model.addAttribute("member", member);
        }

        return "login/myInfoForm";
    }


    @PostMapping("/myInfoEdit")
    public String myInfoEdit(MyInfoEditDto myInfoEditDto, HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        Map<String, String> errors = myInfoService.myInfoEdit(myInfoEditDto, loginId);

        if (errors != null){
            model.addAttribute("errors", errors);
            model.addAttribute("loginId", loginId);
            Member member = memberService.findByLoginId(loginId);

            model.addAttribute("member", member);
            return "login/myInfoForm";
        }

        return "redirect:myInfo";
    }


    @PostMapping("/addCash")
    public String addCash(AddCashDto addCashDto, HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        Map<String, String> errors = memberService.addCash(loginId, addCashDto);

        if (errors != null){
            model.addAttribute("errors", errors);
            model.addAttribute("loginId", loginId);
            Member member = memberService.findByLoginId(loginId);
            model.addAttribute("member", member);
            return "login/myInfoForm";
        }

        return "redirect:myInfo";
    }




















}
