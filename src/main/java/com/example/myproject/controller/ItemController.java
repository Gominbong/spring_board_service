package com.example.myproject.controller;

import com.example.myproject.dto.AddItemFormDto;
import com.example.myproject.service.AddItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final AddItemService addItemService;
    @GetMapping("/addItem")
    public String addItemForm(HttpServletRequest request, Model model){
        model.addAttribute("addItemFormDto", new AddItemFormDto());
        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));

        return "/addSheetMusic/addItemForm";
    }

    @GetMapping("/addItemComplete")
    public String addItemForm(){
        return "/addSheetMusic/addItemCompleteForm";
    }

    @PostMapping("/addItem")
    public String addItemForm(AddItemFormDto addItemFormDto, HttpServletRequest request){


        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        log.info("세션 로그인한 아이디 = {} ", loginId);

        addItemService.createAddItem(request, addItemFormDto);
        return "redirect:/addItemComplete";
    }

    @GetMapping("/buyList")
    public String buyList(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));

        return "/addSheetMusic/buyListForm";
    }

    @GetMapping("/sellList")
    public String cart(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));

        return "/addSheetMusic/sellListForm";
    }
}
