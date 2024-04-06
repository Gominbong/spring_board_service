package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.MusicListFormDto;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.FileListRepository;
import com.example.myproject.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
@Transactional
@Service
@Slf4j
public class MusicListService {

    private final MusicListRepository musicListRepository;
    private final MemberRepository memberRepository;
    private final FileListRepository fileRepository;
    int i=0;
    public Map<String, String> createAddItem(HttpServletRequest request, MusicListFormDto musicListFormDto) throws IOException {

        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        Member member = memberRepository.findByLoginId(loginId);
        Map<String, String> errors = new HashMap<>();
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
        if (!StringUtils.hasText(musicListFormDto.getContent())){
            errors.put("content", "내용 입력 필수입니다");
        }
        if (!errors.isEmpty()){
            return errors;
        }

        musicListRepository.save(musicList);
        return null;
    }

    public Page<MusicList> findAllItemList(int page) {
        Pageable pageable = PageRequest.of(page, 15);
        return musicListRepository.findAll(pageable);
    }

    public MusicList findById(Long id) {
        return musicListRepository.findById(id).orElseThrow();
    }

}
