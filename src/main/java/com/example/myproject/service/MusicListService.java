package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.MusicListFormDto;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.FileListRepository;
import com.example.myproject.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
    public Map<String, String> createAddItem(HttpServletRequest request, MusicListFormDto musicListFormDto,
                                             String loginId) throws IOException {

        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(musicListFormDto.getTitle())){
            errors.put("title", "제목 입력 필수입니다");
        }
        if (!StringUtils.hasText(musicListFormDto.getType())){
            errors.put("type", "악기타입 입력 필수입니다");
        }
        if (!StringUtils.hasText(musicListFormDto.getLevel())){
            errors.put("level", "난이도 입력 필수입니다 ");
        }
        if (musicListFormDto.getPrice()==null){
            errors.put("price", "가격 입력 필수입니다 ");
        }
        if (!errors.isEmpty()){
            return errors;
        }

        Member member = memberRepository.findByLoginId(loginId);
        MusicList musicList = new MusicList();
        musicList.setMember(member); // 외래키 설정
        musicList.setTitle(musicListFormDto.getTitle());
        musicList.setType(musicListFormDto.getType());
        musicList.setLevel(musicListFormDto.getLevel());
        musicList.setPrice(musicListFormDto.getPrice());
        musicList.setContent(musicListFormDto.getContent());
        musicList.setLoginId(loginId);
        musicList.setLocalDateTime(LocalDateTime.now().withNano(0));
        musicList.setMemberNickname(member.getNickname());
        musicListRepository.save(musicList);

        List<MultipartFile> storePdfFiles = musicListFormDto.getPdfFiles();

        for (MultipartFile multipartFile : storePdfFiles){
            if (!multipartFile.isEmpty()){
                String originalFilename = multipartFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/Users/asd/Desktop/study/pdf/" + storedFileName;
                multipartFile.transferTo(new File(savePath));
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
        Pageable pageable = PageRequest.of(page, 15);
        return musicListRepository.findBySoftDeleteIsNull(pageable);
    }

    public MusicList findById(Long id) {
        return musicListRepository.findById(id).orElseThrow();
    }

    public void deleteMusicList(Long id) {
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicList.setSoftDelete("Yes");

    }

    public MusicListFormDto setUpdateMusicListForm (Long id) {
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        MusicListFormDto musicListFormDto = new MusicListFormDto();
        musicListFormDto.setTitle(musicList.getTitle());
        musicListFormDto.setContent(musicList.getContent());
        musicListFormDto.setType(musicList.getType());
        musicListFormDto.setLevel(musicList.getLevel());
        musicListFormDto.setPrice(musicList.getPrice());
        return musicListFormDto;

    }

    @Transactional
    public void updateMusicList(Long id, MusicListFormDto musicListFormDto) {
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicList.setTitle(musicListFormDto.getTitle());
        musicList.setContent(musicListFormDto.getContent());
        musicList.setType(musicListFormDto.getType());
        musicList.setLevel(musicListFormDto.getLevel());
        musicList.setPrice(musicListFormDto.getPrice());
    }
}
