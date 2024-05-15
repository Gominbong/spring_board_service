package com.example.myproject.controller;

import com.example.myproject.domain.*;
import com.example.myproject.dto.*;
import com.example.myproject.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String selectSort(SearchSortDto searchSortDto,
                             HttpServletRequest request, Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page){
        log.info(" 디티오 확인1 = {}", searchSortDto.getSortType());
        log.info(" 디티오 확인2 = {}", searchSortDto.getSearchType());
        log.info(" 디티오 확인3 = {}", searchSortDto.getSearch());

        model.addAttribute("menu", "home");
        model.addAttribute("searchDto", searchSortDto);
        log.info("셀릭트창 확인 = {} " , searchSortDto.getSearchType());
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);

        if (searchSortDto.getSearchType().equals("searchNickname")){
            model.addAttribute("searchDto", searchSortDto);
        } else if (searchSortDto.getSearchType().equals("searchTitle")){
            model.addAttribute("searchDto", searchSortDto);

        }

        Page<MusicList> paging = musicListService.musicListSearchSort(page, searchSortDto);


        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());
        int temp = page / 7;
        int start = temp * 7;
        log.info("스타트 페이지 확인 = '{}'", start);

        if (paging.getTotalPages() ==0 || paging.getTotalPages()==1){
            log.info("여기11111 = {}", paging.getTotalPages());
            model.addAttribute("start", 0);
            model.addAttribute("end", 0);
        }else if (start ==0 && paging.getTotalPages() <=7){
            log.info("여기33333 = {}", paging.getTotalPages());
            model.addAttribute("start", 0);
            model.addAttribute("end", paging.getTotalPages() -1);
        }else if (start != 0 && paging.getTotalPages() - start <=7){
            log.info("여기44444 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        } else {
            log.info("여기2222 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", start +6);
        }


        return "musicListSearchSortForm";
    }

    @GetMapping("/homeSort")
    public String homeSort(HomeSortDto homeSortDto, HttpServletRequest request, Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page){

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        model.addAttribute("menu", "home");
        model.addAttribute("homeSortDto", homeSortDto);
        Page<MusicList> paging = musicListService.homeSort(page, homeSortDto);
        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());
        int temp = page / 7;
        int start = temp * 7;
        log.info("스타트 페이지 확인 = '{}'", start);

        if (paging.getTotalPages() ==0 || paging.getTotalPages()==1){
            log.info("여기11111 = {}", paging.getTotalPages());
            model.addAttribute("start", 0);
            model.addAttribute("end", 0);
        }else if (start ==0 && paging.getTotalPages() <=7){
            log.info("여기33333 = {}", paging.getTotalPages());
            model.addAttribute("start", 0);
            model.addAttribute("end", paging.getTotalPages() -1);
        }else if (start != 0 && paging.getTotalPages() - start <=7){
            log.info("여기44444 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        } else {
            log.info("여기2222 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", start +6);
        }

        return "homeSortForm";
    }

    @GetMapping("/search")
    public String search(SearchDto searchDto, @RequestParam(value = "page", defaultValue = "0") int page,
                         HttpServletRequest request, Model model){
        model.addAttribute("menu", "home");
        model.addAttribute("searchDto", searchDto);

        log.info("서치 dto 확인1 = {} " , searchDto.getSearchType());
        log.info("서치 dto 확인2 = {} " , searchDto.getSortType());
        log.info("서치 dto 확인3 = {} " , searchDto.getSearch());
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        if (searchDto.getSearchType().equals("searchNickname")){
            model.addAttribute("searchDto", searchDto);
        } else if (searchDto.getSearchType().equals("searchTitle")){
            model.addAttribute("searchDto", searchDto);

        }


        Page<MusicList> paging = musicListService.musicListSearch(page, searchDto);


        model.addAttribute("page", page);
        model.addAttribute("paging", paging);
        log.info("전체 페이지수 확인 = '{}'", paging.getTotalPages());
        int temp = page / 7;
        int start = temp * 7;
        log.info("스타트 페이지 확인 = '{}'", start);

        if (paging.getTotalPages() ==0 || paging.getTotalPages()==1){
            log.info("여기11111 = {}", paging.getTotalPages());
            model.addAttribute("start", 0);
            model.addAttribute("end", 0);
        }else if (start ==0 && paging.getTotalPages() <=7){
            log.info("여기33333 = {}", paging.getTotalPages());
            model.addAttribute("start", 0);
            model.addAttribute("end", paging.getTotalPages() -1);
        }else if (start != 0 && paging.getTotalPages() - start <=7){
            log.info("여기44444 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages()-1);
        } else {
            log.info("여기2222 = {}", paging.getTotalPages());
            model.addAttribute("start", start);
            model.addAttribute("end", start +6);
        }

        return "musicListSearchForm";
    }

    @PostMapping("/commentEdit")
    public String commentEdit(CommentUpdateDto commentUpdateDto, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        log.info("여기기기기기이 = {}", referer);
        commentService.commentEdit(commentUpdateDto);

        return "redirect:" + referer;
    }

    @PostMapping("/commentDelete")
    public String commentDelete(CommentDeleteDto commentDeleteDto, HttpServletRequest request){
        String referer = request.getHeader("Referer");
        commentService.commentDelete(commentDeleteDto);

        return "redirect:" + referer;
    }

    @PostMapping("replyAdd")
    public String replyAdd(CommentReplyFormDto commentReplyFormDto, HttpServletRequest request){
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");

        log.info("대댓글 comment id 값 = {}", commentReplyFormDto.getCommentId());
        log.info("대댓글 내용 = {}", commentReplyFormDto.getReplyContent());

        commentService.replyAdd(commentReplyFormDto, loginId);

        return "redirect:" + referer;
    }

    @PostMapping("/comment")
    public String commentAdd(CommentFormDto commentFormDto, HttpServletRequest request){
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
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
                              HttpServletRequest request)
            throws MalformedURLException {

        String property = System.getProperty("java.io.tmpdir");
        log.info("프로퍼티 확인 = {}", property);
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        LikeCount likeCount = likeCountService.findMyLike(id, loginId);
        MusicList musicList = musicListService.findById(id);
        List<FileList> fileList = fileListService.findByFiles(id);
        SellBuyList sellBuyList = sellBuyListService.myBuyInfo(id, loginId);
        Member member = memberService.findByLoginId(loginId);
        List<Comment> commentList = commentService.findCommentList(id);
        model.addAttribute("commentList", commentList);
        log.info("댓글 개수 확인해보기 = {}", commentList.size());

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
        return "musicList/contentMusicListForm";
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
                                          MusicListUpdateDto musicListUpdateDto,
                                          HttpServletRequest request, Model model) {

        log.info("업데이트 뮤직리스트 아이디 = {}", id);
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
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
                                  Model model) {

        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        MusicListUpdateDto musicListUpdateDto = musicListService.setMusicListUpdateDto(id);
        List<FileList> fileList = fileListService.findByFiles(id);

        model.addAttribute("musicListUpdateDto", musicListUpdateDto);
        model.addAttribute("fileList", fileList);
        model.addAttribute("musicListId", id);

        return "musicList/editMusicListForm";
    }

    @GetMapping("/addMusicList")
    public String addMusicList(HttpServletRequest request, Model model) {

        log.info("등록 페이지 입니다.");
        HttpSession session = request.getSession();
        String loginId = (String) session.getAttribute("loginId");
        model.addAttribute("loginId", loginId);
        log.info("세션 로그인한 아이디 = '{}' ", loginId);
        model.addAttribute("musicListFormDto", new MusicListFormDto());

        return "musicList/addMusicListForm";
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
            return "musicList/addMusicListForm";
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