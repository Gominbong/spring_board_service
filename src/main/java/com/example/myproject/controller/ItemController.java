package com.example.myproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    @GetMapping("/add")
    public String additem(){
        return "/items/addFrom";
    }

    @GetMapping("/buylist")
    public String buylist(){
        return "/items/buyListForm";
    }
}
