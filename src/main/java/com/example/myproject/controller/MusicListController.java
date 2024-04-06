package com.example.myproject.controller;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.MusicListFormDto;
import com.example.myproject.dto.ContentMusicListFormDto;
import com.example.myproject.service.FileListService;
import com.example.myproject.service.MusicListService;
import jakarta.servlet.http.HttpServletRequest;
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
        model.addAttribute("loginId", session.getAttribute("loginId"));

        MusicList musicList = musicListService.findById(id);
        List<FileList> fileList = fileListService.findByFiles(id);
        log.info("파일 확인해보기  =  {}", fileList );

        for (FileList file : fileList) {
            UrlResource urlResource = new UrlResource("file:" + file.getStoredFilename());
        }


        model.addAttribute("musicList", musicList);
        model.addAttribute("fileList", fileList);

        return "/musicList/contentMusicListForm";
    }

    @PostMapping("/content")
    public String contentForm(@RequestParam("musicListId") Long id, Model model,
                              ContentMusicListFormDto contentMusicListFormDto){

        MusicList byId = musicListService.findById(id);
        log.info("확인해보기용 = '{}' ", byId.getMemberNickname() );
        return "/musicList/contentMusicListForm";
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