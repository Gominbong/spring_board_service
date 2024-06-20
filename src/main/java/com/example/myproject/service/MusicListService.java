package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.*;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.FileListRepository;
import com.example.myproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class MusicListService {

    private final MusicListRepository musicListRepository;
    private final MemberRepository memberRepository;
    private final FileListRepository fileRepository;

    public Map<String, String> createAddItem(MusicListFormDto musicListFormDto, String loginId) {

        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(musicListFormDto.getTitle())) {
            errors.put("title", "제목 입력 필수입니다");
        }
        if (!StringUtils.hasText(musicListFormDto.getType())) {
            errors.put("type", "악기타입 입력 필수입니다");
        }
        if (!StringUtils.hasText(musicListFormDto.getLevel())) {
            errors.put("level", "난이도 입력 필수입니다 ");
        }
        if (musicListFormDto.getPrice() == null) {
            errors.put("price", "가격 입력 필수입니다 ");
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        Member member = memberRepository.findByLoginIdQueryDsl(loginId);
        MusicList musicList = new MusicList();
        musicList.setMember(member); // 외래키 설정
        musicList.setTitle(musicListFormDto.getTitle());
        musicList.setType(musicListFormDto.getType());
        musicList.setLevel(musicListFormDto.getLevel());
        musicList.setPrice(musicListFormDto.getPrice());
        musicList.setContent(musicListFormDto.getContent());
        musicList.setLikeCount(0);
        musicList.setSalesQuantity(0);
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        musicList.setCreateTime(createTime);
        musicListRepository.save(musicList);
        List<MultipartFile> pdfFiles = musicListFormDto.getPdfFiles();

        if (!pdfFiles.get(0).isEmpty()) {
            for (MultipartFile multipartFile : pdfFiles) {
                String originalFilename = multipartFile.getOriginalFilename();
                String encode = originalFilename.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "_");
                String storedFileName = System.currentTimeMillis() + "_" + encode;

                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    try {
                        multipartFile.transferTo(new File("C:/Users/asd/Desktop/study/pdf/" + storedFileName));
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                } else {
                    try {
                        multipartFile.transferTo(new File(storedFileName));
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }

                FileList fileList = new FileList();
                fileList.setMusicList(musicList); //외래키 설정
                fileList.setOriginalFilename(originalFilename);
                fileList.setStoredFilename(storedFileName);
                fileRepository.save(fileList);
            }
        }

        return null;
    }

    public Page<MusicList> findMusicList(int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
        return musicListRepository.findBySoftDeleteIsNullQueryDsl(pageable);
    }


    public MusicList findById(Long id) {
        return musicListRepository.findByMusicListQueryDsl(id);
    }


    @Transactional
    public void deleteMusicList(Long id) {
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicList.setSoftDelete("Yes");
        log.info("뮤직리스트 논리적 삭제 성공");
    }

    @Transactional
    public Map<String, String> updateMusicList(Long id, MusicListUpdateDto musicListUpdateDto) {

        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(musicListUpdateDto.getType())) {
            errors.put("type", "악기타입 입력 필수입니다");
        }
        if (!StringUtils.hasText(musicListUpdateDto.getTitle())) {
            errors.put("title", "제목 입력 필수입니다");
        }
        if (!StringUtils.hasText(musicListUpdateDto.getLevel())) {
            errors.put("level", "난이도 입력 필수입니다 ");
        }
        if (musicListUpdateDto.getPrice() == null) {
            errors.put("price", "가격 입력 필수입니다 ");
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicList.setTitle(musicListUpdateDto.getTitle());
        musicList.setType(musicListUpdateDto.getType());
        musicList.setLevel(musicListUpdateDto.getLevel());
        musicList.setPrice(musicListUpdateDto.getPrice());
        musicList.setContent(musicListUpdateDto.getContent());

        List<String> filename = musicListUpdateDto.getFilename();
        log.info("업데이트디티오 = {}", musicListUpdateDto.toString());
        if (filename != null) {
            for (String file : filename) {
                log.info("수정페이지에서 삭제버튼누른 파일이름 = '{}'", file);
                FileList fileList = fileRepository.findByStoredFilenameQueryDsl(file);
                fileRepository.delete(fileList);

                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    File file1 = new File("C:/Users/asd/Desktop/study/pdf/" + file);
                    file1.delete();
                } else {
                    File file1 = new File("/upload/" + file);
                    file1.delete();
                }

            }
        }

        List<MultipartFile> pdfFiles = musicListUpdateDto.getPdfFiles();
        if (!pdfFiles.get(0).isEmpty()) {
            for (MultipartFile multipartFile : pdfFiles) {

                String originalFilename = multipartFile.getOriginalFilename();
                String encode = originalFilename.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "_");
                String storedFileName = System.currentTimeMillis() + "_" + encode;

                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    try {
                        multipartFile.transferTo(new File("C:/Users/asd/Desktop/study/pdf/" + storedFileName));
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                } else {
                    try {
                        multipartFile.transferTo(new File(storedFileName));
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public MusicListUpdateDto setMusicListUpdateDto(Long id) {
        MusicListUpdateDto musicListUpdateDto = new MusicListUpdateDto();
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicListUpdateDto.setTitle(musicList.getTitle());
        musicListUpdateDto.setType(musicList.getType());
        musicListUpdateDto.setLevel(musicList.getLevel());
        musicListUpdateDto.setPrice(musicList.getPrice());
        musicListUpdateDto.setContent(musicList.getContent());

        return musicListUpdateDto;

    }

    public Page<MusicList> musicListSearchSort(int page, SearchSortDto searchSortDto) {

        if (searchSortDto.getSearchType().equals("searchTitle")
                && searchSortDto.getSortType().equals("sortSelect")) {
            log.info("여기1111");
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
            return musicListRepository.findMusicListByTitleContainsQueryDsl(pageable, searchSortDto.getSearch());
        } else if (searchSortDto.getSearchType().equals("searchTitle")
                && searchSortDto.getSortType().equals("sortPrice")) {
            log.info("여기2222");
            log.info("dto확인 = {}", searchSortDto.getSearch());
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "price"));
            return musicListRepository.findMusicListByTitleContainsQueryDsl(pageable,
                    searchSortDto.getSearch());
        } else if (searchSortDto.getSearchType().equals("searchTitle")
                && searchSortDto.getSortType().equals("sortLike")) {
            log.info("여기3333");
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "likeCount"));
            return musicListRepository.findMusicListByTitleContainsQueryDsl(pageable, searchSortDto.getSearch());
        } else if (searchSortDto.getSearchType().equals("searchTitle")
                && searchSortDto.getSortType().equals("sortQuantity")) {
            log.info("여기4444");
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "salesQuantity"));
            return musicListRepository.findMusicListByTitleContainsQueryDsl(pageable, searchSortDto.getSearch());
        }

        if (searchSortDto.getSearchType().equals("searchNickname")
                && searchSortDto.getSortType().equals("sortSelect")) {
            log.info("여기1111");
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
            return musicListRepository.findMusicListByNicknameContainsQueryDsl(pageable, searchSortDto.getSearch());
        } else if (searchSortDto.getSearchType().equals("searchNickname")
                && searchSortDto.getSortType().equals("sortPrice")) {
            log.info("여기2222");
            log.info("dto확인 = {}", searchSortDto.getSearch());
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "price"));
            return musicListRepository.findMusicListByNicknameContainsQueryDsl(pageable,
                    searchSortDto.getSearch());
        } else if (searchSortDto.getSearchType().equals("searchNickname")
                && searchSortDto.getSortType().equals("sortLike")) {
            log.info("여기3333");
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "likeCount"));
            return musicListRepository.findMusicListByNicknameContainsQueryDsl(pageable, searchSortDto.getSearch());
        } else if (searchSortDto.getSearchType().equals("searchNickname")
                && searchSortDto.getSortType().equals("sortQuantity")) {
            log.info("여기4444");
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "salesQuantity"));
            return musicListRepository.findMusicListByNicknameContainsQueryDsl(pageable, searchSortDto.getSearch());
        }

        return null;
    }

    public Page<MusicList> musicListSearch(int page, SearchDto searchDto) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
        if (searchDto.getSearchType().equals("searchTitle")) {
            return musicListRepository.findMusicListByTitleContainsQueryDsl(pageable, searchDto.getSearch());
        }
        if (searchDto.getSearchType().equals("searchNickname")) {
            return musicListRepository.findMusicListByNicknameContainsQueryDsl(pageable, searchDto.getSearch());
        }
        log.info("ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ");
        return null;
    }

    public Page<MusicList> homeSort(int page, HomeSortDto homeSortDto) {

        if (homeSortDto.getSortType().equals("sortSelect")) {
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "id"));
            return musicListRepository.findBySoftDeleteIsNullQueryDsl(pageable);
        }
        if (homeSortDto.getSortType().equals("sortPrice")) {
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "price"));
            return musicListRepository.findBySoftDeleteIsNullQueryDsl(pageable);
        }
        if (homeSortDto.getSortType().equals("sortLike")) {
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "likeCount"));
            return musicListRepository.findBySoftDeleteIsNullQueryDsl(pageable);
        }
        if (homeSortDto.getSortType().equals("sortQuantity")) {
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "salesQuantity"));
            return musicListRepository.findBySoftDeleteIsNullQueryDsl(pageable);
        }

        return null;
    }

    public void pageStartEndNumber(int page, Page<MusicList> paging, Model model) {
        int temp = page / 7;
        int start = temp * 7;


        if (paging.getTotalPages() == 0 || paging.getTotalPages() == 1) {
            model.addAttribute("start", 0);
            model.addAttribute("end", 0);
        } else if (start == 0 && paging.getTotalPages() <= 7) {
            model.addAttribute("start", 0);
            model.addAttribute("end", paging.getTotalPages() - 1);
        } else if (start != 0 && paging.getTotalPages() - start <= 7) {
            model.addAttribute("start", start);
            model.addAttribute("end", paging.getTotalPages() - 1);
        } else {
            model.addAttribute("start", start);
            model.addAttribute("end", start + 6);
        }

    }
}
