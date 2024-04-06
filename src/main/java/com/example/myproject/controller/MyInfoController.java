package com.example.myproject.controller;

import com.example.myproject.domain.Member;
import com.example.myproject.dto.MyInfoDto;
import com.example.myproject.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MyInfoController {

    private final MemberService memberService;

    @GetMapping("/myInfo")
    public String myInfo(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            String loginId = (String)session.getAttribute("loginId");
            model.addAttribute("loginId", loginId);
            Member member = memberService.findByLoginId(loginId);

            model.addAttribute("member", member);

        }

        return "/login/myInfoForm";
    }

    @PostMapping("/myInfo")
    public String myInfo(MyInfoDto myInfoDto, HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");

        memberService.addCash(loginId, myInfoDto);
        return "redirect:/myInfo";
    }




















}
