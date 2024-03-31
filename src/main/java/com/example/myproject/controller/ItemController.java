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

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final AddItemService addItemService;

    @GetMapping("/addItem")
    public String addItemForm(HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));
        model.addAttribute("addItemFormDto", new AddItemFormDto());

        return "/addSheetMusic/addItemForm";
    }

    @PostMapping("/addItem")
    public String addItemForm(AddItemFormDto addItemFormDto, HttpServletRequest request, Model model) throws IOException {


        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        log.info("세션 로그인한 아이디 = {} ", loginId);

        Map<String, String> errors = addItemService.createAddItem(request, addItemFormDto);
        if (errors != null){
            model.addAttribute("errors", errors);
            return "/addSheetMusic/addItemForm";
        }
        return "redirect:/";
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
