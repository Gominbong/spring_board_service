package com.example.myproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    @GetMapping("/add")
    public String addItem(HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));
        return "/items/addFrom";
    }

    @GetMapping("/buyList")
    public String buyList(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));
        return "/items/buyListForm";
    }

    @GetMapping("/sellList")
    public String cart(HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));
        return "/items/sellListForm";
    }
}
