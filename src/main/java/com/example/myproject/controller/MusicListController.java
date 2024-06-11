package com.example.myproject.controller;

import com.example.myproject.domain.*;
import com.example.myproject.dto.*;
import com.example.myproject.service.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import javax.crypto.SecretKey;
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
    private final CommentService commentService;
    private final LoginService loginService;

    @PostMapping("/download")
    public ResponseEntity<Resource> downloadAttach(DownloadDto downloadDto) throws MalformedURLException {
        FileList file = fileListService.findById(downloadDto.getFileListId());

        String originalFilename = file.getOriginalFilename();
        String storedFilename = file.getStoredFilename();
        String os = System.getProperty("os.name").toLowerCase();
        String Path;
        if (os.contains("win")){
            Path = "C:/Users/asd/Desktop/study/pdf/" + storedFilename;
        }else{
            Path = "/upload/" + storedFilename;
        }

        UrlResource urlResource = new UrlResource("file:"+Path);
        String encodedUploadFileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) //다운로드받는파일명
                .body(urlResource);
    }

    @GetMapping("/searchSort")
    public String selectSort(SearchSortDto searchSortDto, HttpServletResponse response,
                             HttpServletRequest request, Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page){

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }

        model.addAttribute("searchDto", searchSortDto);
        Page<MusicList> paging = musicListService.musicListSearchSort(page, searchSortDto);
        musicListService.pageStartEndNumber(page, paging, model);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);

        return "musicListSearchSortForm";
    }

    @GetMapping("/homeSort")
    public String homeSort(HomeSortDto homeSortDto, HttpServletRequest request, Model model,
                           HttpServletResponse response,
                       @RequestParam(value = "page", defaultValue = "0") int page){

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }

        model.addAttribute("homeSortDto", homeSortDto);
        Page<MusicList> paging = musicListService.homeSort(page, homeSortDto);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        musicListService.pageStartEndNumber(page, paging, model);

        return "homeSortForm";
    }

    @GetMapping("/search")
    public String search(SearchDto searchDto, @RequestParam(value = "page", defaultValue = "0") int page,
                         HttpServletResponse response,
                         HttpServletRequest request, Model model){

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }

        model.addAttribute("searchDto", searchDto);
        model.addAttribute("searchDto", searchDto);
        Page<MusicList> paging = musicListService.musicListSearch(page, searchDto);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        musicListService.pageStartEndNumber(page, paging, model);

        return "musicListSearchForm";
    }

    @PostMapping("/commentEdit")
    public String commentEdit(CommentUpdateDto commentUpdateDto, HttpServletRequest request,
                              HttpServletResponse response) {
        String referer = request.getHeader("Referer");
        String loginId = loginService.loginIdCheck(request, response);
        if (loginId == null){
            return "redirect:" + referer;
        }
        commentService.commentEdit(commentUpdateDto);

        return "redirect:" + referer;
    }

    @PostMapping("/commentDelete")
    public String commentDelete(CommentDeleteDto commentDeleteDto, HttpServletRequest request,
                                HttpServletResponse response){
        String referer = request.getHeader("Referer");
        String loginId = loginService.loginIdCheck(request, response);
        if (loginId == null){
            return "redirect:" + referer;
        }
        commentService.commentDelete(commentDeleteDto);

        return "redirect:" + referer;
    }

    @PostMapping("replyAdd")
    public String replyAdd(CommentReplyFormDto commentReplyFormDto, HttpServletRequest request,
                           HttpServletResponse response){
        String referer = request.getHeader("Referer");

        String loginId = loginService.loginIdCheck(request, response);
        if (loginId == null){
            return "redirect:" + referer;
        }

        log.info("로그인아이디static 확인 = {}", loginId);
        commentService.replyAdd(commentReplyFormDto, loginId);

        return "redirect:" + referer;
    }

    @PostMapping("/comment")
    public String commentAdd(CommentFormDto commentFormDto, HttpServletRequest request, HttpServletResponse response){
        String referer = request.getHeader("Referer");

        String loginId = loginService.loginIdCheck(request, response);
        if (loginId == null){
            return "redirect:" + referer;
        }
        log.info("로그인아이디static 확인 = {}", loginId);

        List<Comment> commentList = commentService.findCommentList(commentFormDto.getMusicListId());
        if (commentList.isEmpty()){
            int parent = 0;
            Comment comment = commentService.commentAdd(commentFormDto, loginId, parent);
        }else{
            List<Comment> byMusicListIdAndDivWidthSize =
                    commentService.findByMusicListIdAndDivWidthSize(commentFormDto.getMusicListId());
            int parent = byMusicListIdAndDivWidthSize.size();
            Comment comment = commentService.commentAdd(commentFormDto, loginId, parent);
        }

        return "redirect:" + referer;
    }

    @GetMapping("/content")
    public String content(@RequestParam("musicListId") Long id, Model model,
                          HttpServletRequest request, HttpServletResponse response)
            throws MalformedURLException {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디2확인해보자 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }
        Cookie jwtCookie = WebUtils.getCookie(request, "jwtToken");
        if (jwtCookie != null){
            SecretKey key = Keys.hmacShaKeyFor("c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK".getBytes(StandardCharsets.UTF_8));
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtCookie.getValue());
            model.addAttribute("loginId",claimsJws.getPayload().getSubject());
        }

        LikeCount likeCount = likeCountService.findMyLike(id, loginId);
        MusicList musicList = musicListService.findById(id);
        List<FileList> fileList = fileListService.findByFiles(id);
        SellBuyList sellBuyList = sellBuyListService.myBuyInfo(id, loginId);
        Member member = memberService.findByLoginId(loginId);
        List<Comment> commentList = commentService.findCommentList(id);
        model.addAttribute("commentList", commentList);

        if (likeCount != null){
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
        return "musicList/contentMusicListForm";
    }

    @PostMapping("/deleteMusicList")
    public String deleteMusicList(MusicListDeleteDto musicListDeleteDto) {
        musicListService.deleteMusicList(musicListDeleteDto.getMusicListId());
        return "redirect:/";
    }

    @PostMapping("/EditMusicList")
    public String updateMusicListComplete(@RequestParam("musicListId") Long id, HttpServletRequest request,
                                          MusicListUpdateDto musicListUpdateDto, Model model, HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }

        model.addAttribute("musicListId", id);
        List<FileList> fileList = fileListService.findByFiles(id);
        Map<String, String> errors = musicListService.updateMusicList(id, musicListUpdateDto);
        if (errors != null) {
            model.addAttribute("fileList", fileList);
            model.addAttribute("errors", errors);
            return "musicList/editMusicListForm";
        }

        return "redirect:/";
    }

    @PostMapping("/editMusicList")
    public String updateMusicList(@RequestParam("musicListId") Long id, HttpServletRequest request,
                                  HttpServletResponse response, Model model) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }

        MusicListUpdateDto musicListUpdateDto = musicListService.setMusicListUpdateDto(id);
        List<FileList> fileList = fileListService.findByFiles(id);

        model.addAttribute("musicListUpdateDto", musicListUpdateDto);
        model.addAttribute("fileList", fileList);
        model.addAttribute("musicListId", id);

        return "musicList/editMusicListForm";
    }

    @GetMapping("/addMusicList")
    public String addMusicList(HttpServletRequest request, Model model, HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
        }

        model.addAttribute("musicListFormDto", new MusicListFormDto());

        return "musicList/addMusicListForm";
    }

    @PostMapping("/addMusicList")
    public String addMusicList(MusicListFormDto musicListFormDto, HttpServletRequest request, Model model,
                               HttpServletResponse response) {

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);
        if (loginId != null){
            model.addAttribute("loginId", loginId);
            
        }



        Map<String, String> errors = musicListService.createAddItem(musicListFormDto, loginId);
        if (errors != null) {
            model.addAttribute("errors", errors);
            return "musicList/addMusicListForm";
        }

        return "redirect:/";
    }

    @PostMapping("/likeCount")
    public String likeCount(LikeCountDto likeCountDto, HttpServletRequest request, HttpServletResponse response){
        String referer = request.getHeader("Referer");

        String loginId = loginService.loginIdCheck(request, response);
        log.info("로그인아이디static 확인 = {}", loginId);

        LikeCount like = likeCountService.like(likeCountDto.getMusicListId(), loginId);
        if (like != null){
            log.info("추천 성공");
        }else {
            log.info("추천은 1개아이디당 한번만가능");
        }

        return "redirect:" + referer;
    }



}