package com.example.myproject.controller;

import com.example.myproject.domain.*;
import com.example.myproject.dto.*;
import com.example.myproject.service.*;
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
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
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
    private final MemberService memberService;
    private final LikeCountService likeCountService;

    @PostMapping("/download")
    public ResponseEntity<Resource> downloadAttach(DownloadDto downloadDto) throws MalformedURLException {
        FileList file = fileListService.findById(downloadDto.getFileListId());

        String originalFilename = file.getOriginalFilename();
        String storedFilename = file.getStoredFilename();
        String Path = "C:/Users/asd/Desktop/study/pdf/" + storedFilename;

        UrlResource urlResource = new UrlResource("file:" + Path);

        String encodedUploadFileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) //다운로드받는파일명
                .body(urlResource);
    }

    @GetMapping("/content")
    public String content(@RequestParam("musicListId") Long id, Model model,
                              HttpServletRequest request, HttpServletResponse response)
            throws MalformedURLException {
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        LikeCount likeCount = likeCountService.findMyLike(id, loginId);
        MusicList musicList = musicListService.findById(id);
        List<FileList> fileList = fileListService.findByFiles(id);
        SellBuyList sellBuyList = sellBuyListService.myBuyInfo(id, loginId);
        Member member = memberService.findByLoginId(loginId);
        if (likeCount != null){
            log.info("아이디1개당1개추천만가능합니다.");
            model.addAttribute("likeCount", 1);
        }else{
            model.addAttribute("likeCount", 0);
        }

        if (member == null){
            model.addAttribute("memberCash",-1);
        }else{
            Integer memberCash = member.getCash();
            model.addAttribute("memberCash", memberCash);
        }

        if (musicList.getSoftDelete() != null && musicList.getSalesQuantity() == 0) {
            log.info("총판매수량 = {}", musicList.getSalesQuantity());
            log.info("삭제된 게시글 입니다.");
            return "/musicList/deletedMusicListForm";
        }

        if (fileList.isEmpty()) {
            log.info("첨부파일없다.");
        }

        model.addAttribute("fileList", fileList);
        model.addAttribute("musicList", musicList);
        model.addAttribute("sellBuyList", sellBuyList);
        return "/musicList/contentMusicListForm";
    }

    @PostMapping("/deleteMusicList")
    public String deleteMusicList(MusicListDeleteDto musicListDeleteDto) {
        musicListService.deleteMusicList(musicListDeleteDto.getMusicListId());
        log.info("삭제할 아이디값 확인 = {} ", musicListDeleteDto.getMusicListId());
        log.info("논리적 삭제 완료 내용 필드값 null 업데이트해서 삭제 구분");
        return "redirect:/";
    }

    @PostMapping("/EditMusicList")
    public String updateMusicListComplete(@RequestParam("musicListId") Long id,
                                          UpdateMusicListFormDto updateMusicListFormDto,
                                          HttpServletRequest request, Model model) {

        log.info("업데이트 뮤직리스트 아이디 = {}", id);
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        model.addAttribute("musicListId", id);
        List<FileList> fileList = fileListService.findByFiles(id);
        Map<String, String> errors = musicListService.updateMusicList(id, updateMusicListFormDto);
        if (errors != null) {
            model.addAttribute("fileList", fileList);
            model.addAttribute("errors", errors);
            return "/musicList/editMusicListForm";
        }

        return "redirect:/";
    }

    @PostMapping("/editMusicList")
    public String updateMusicList(@RequestParam("musicListId") Long id, HttpServletRequest request,
                                  Model model) {

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        UpdateMusicListFormDto updateMusicListFormDto = musicListService.setUpdateMusicListFormDto(id);
        List<FileList> fileList = fileListService.findByFiles(id);

        model.addAttribute("updateMusicListFormDto", updateMusicListFormDto);
        model.addAttribute("fileList", fileList);
        model.addAttribute("musicListId", id);

        return "/musicList/editMusicListForm";
    }

    @GetMapping("/addMusicList")
    public String addMusicList(HttpServletRequest request, Model model) {

        log.info("등록 페이지 입니다.");
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        log.info("세션 로그인한 아이디 = '{}' ", loginId);
        model.addAttribute("musicListFormDto", new MusicListFormDto());

        return "/musicList/addMusicListForm";
    }

    @PostMapping("/addMusicList")
    public String addMusicList(MusicListFormDto musicListFormDto, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        log.info("세션 로그인한 아이디 = '{}' ", loginId);


        Map<String, String> errors = musicListService.createAddItem(request, musicListFormDto, loginId);
        if (errors != null) {
            model.addAttribute("errors", errors);
            return "/musicList/addMusicListForm";
        }

        return "redirect:/";
    }

    @PostMapping("/likeCount")
    public String likeCount(LikeCountDto likeCountDto, HttpServletRequest request){
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        LikeCount like = likeCountService.like(likeCountDto.getMusicListId(), loginId);
        if (like != null){
            log.info("추천 성공");
        }else {
            log.info("추천은 1개아이디당 한번만가능");
        }

        return "redirect:" + referer;
    }

}