package com.example.myproject.controller;

import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.MusicListFormDto;
import com.example.myproject.dto.ContentMusicListFormDto;
import com.example.myproject.service.MusicListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MusicListController {

    private final MusicListService musicListService;



    @GetMapping("/content")
    public String contentForm(@RequestParam("musicListId") Long id, Model model,
                              HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));

        MusicList musicList = musicListService.findById(id);

        model.addAttribute("musicList", musicList);

        return "/musicList/contentMusicListForm";
    }

    @PostMapping("/content")
    public String contentForm(@RequestParam("musicListId") Long id, Model model,
                              ContentMusicListFormDto contentMusicListFormDto){
        MusicList byId = musicListService.findById(id);
        log.info("확인해보기용 = {} ", byId.getMemberNickname() );
        return "/musicList/contentMusicListForm";
    }


    @GetMapping("/addMusicList")
    public String addItemForm(HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));
        model.addAttribute("musicListFormDto", new MusicListFormDto());

        return "/musicList/addMusicListForm";
    }

    @PostMapping("/addMusicList")
    public String addItemForm(MusicListFormDto musicListFormDto, HttpServletRequest request, Model model) throws IOException {


        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        log.info("세션 로그인한 아이디 = {} ", loginId);

        Map<String, String> errors = musicListService.createAddItem(request, musicListFormDto);
        if (errors != null){
            model.addAttribute("errors", errors);
            return "/musicList/addMusicListForm";
        }

        return "redirect:/";
    }

    @GetMapping("/buyList")
    public String buyList(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));

        return "/musicList/buyMusicListForm";
    }

    @GetMapping("/sellList")
    public String cart(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        model.addAttribute("loginId", session.getAttribute("loginId"));

        return "/musicList/sellMusicListForm";
    }
}