package com.example.myproject.controller;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.MusicList;
import com.example.myproject.domain.SellBuyList;
import com.example.myproject.dto.MusicListFormDto;
import com.example.myproject.dto.ContentMusicListFormDto;
import com.example.myproject.service.FileListService;
import com.example.myproject.service.MusicListService;
import com.example.myproject.service.SellBuyListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MusicListController {

    private final MusicListService musicListService;
    private final FileListService fileListService;
    private final SellBuyListService sellBuyListService;
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long id) throws MalformedURLException {
        FileList file = fileListService.findById(id);

        String originalFilename = file.getOriginalFilename();
        UrlResource urlResource = new UrlResource("file:" +
                "C:/Users/asd/Desktop/study/pdf/" + file.getStoredFilename());

        log.info("파일 이름 = '{}' ", originalFilename);
        String originalFileName = file.getOriginalFilename();
        String encodedOriginalFileName = UriUtils.encode(originalFileName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\"" + encodedOriginalFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) //다운로드받는파일명
                .body(urlResource);
    }

    @GetMapping("/content")
    public String contentForm(@RequestParam("musicListId") Long id, Model model,
                              HttpServletRequest request) throws MalformedURLException {
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);

        MusicList musicList = musicListService.findById(id);
        List<FileList> fileList = fileListService.findByFiles(id);
        SellBuyList sellBuyList = sellBuyListService.myBuyInfo(musicList, loginId);

        if (musicList.getContent() == null){
            log.info("삭제된 게시글 입니다.");
            return "/musicList/deletedMusicListForm";
        }

        log.info("파일 확인해보기  =  {}", fileList );

        model.addAttribute("musicList", musicList);
        model.addAttribute("fileList", fileList);
        model.addAttribute("sellBuyList", sellBuyList);
        return "/musicList/contentMusicListForm";
    }

    @GetMapping("/deleteMusicList")
    public String deleteMusicList(@RequestParam("musicListId") Long id){
        musicListService.deleteMusicList(id);
        log.info("논리적 삭제 완료 내용 필드값 null 업데이트해서 삭제 구분");

        return "redirect:/";
    }
















    @GetMapping("/addMusicList")
    public String addItemForm(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        log.info("세션 로그인한 아이디 = '{}' ", loginId);
        model.addAttribute("musicListFormDto", new MusicListFormDto());

        return "/musicList/addMusicListForm";
    }

    @PostMapping("/addMusicList")
    public String addItemForm(MusicListFormDto musicListFormDto, HttpServletRequest request, Model model) throws IOException {


        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        log.info("세션 로그인한 아이디 = '{}' ", loginId);

        Map<String, String> errors = musicListService.createAddItem(request, musicListFormDto);
        if (errors != null){
            model.addAttribute("errors", errors);
            return "/musicList/addMusicListForm";
        }

        return "redirect:/";
    }

}