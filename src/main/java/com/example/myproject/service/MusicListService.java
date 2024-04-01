package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.AddMusicListFormDto;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.FileRepository;
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
    private final FileRepository fileRepository;

    public Map<String, String> createAddItem(HttpServletRequest request, AddMusicListFormDto addItemFormDto) throws IOException {

        HttpSession session = request.getSession();
        String loginId = (String)session.getAttribute("loginId");
        Member member = memberRepository.findByLoginId(loginId);
        Map<String, String> errors = new HashMap<>();
        MusicList musicList = new MusicList(); //db 저장할 객체 생성
        musicList.setMember(member); // 외래키 설정
        musicList.setTitle(addItemFormDto.getTitle());
        musicList.setType(addItemFormDto.getType());
        musicList.setLevel(addItemFormDto.getLevel());
        musicList.setPrice(addItemFormDto.getPrice());
        musicList.setContent(addItemFormDto.getContent());
        musicList.setLocalDateTime(LocalDateTime.now());
        musicList.setMemberName(member.getNickname());

        MultipartFile pdfFile = addItemFormDto.getPdfFile();
        log.info("파일이름 확인 해보기 = {} ", pdfFile);
        String originalFilename = pdfFile.getOriginalFilename();
        String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
        String savePath = "C:/Users/asd/Desktop/study/pdf/" + storedFileName;
        pdfFile.transferTo(new File(savePath));
        FileList fileList = new FileList(); //db 저장할 객체 생성
        fileList.setOriginalFilename(originalFilename);
        fileList.setStoredFilename(storedFileName);
        musicList.setFileLists(fileList); // 외래키 설정

        if (!StringUtils.hasText(addItemFormDto.getTitle())){
            errors.put("title", "제목 입력 필수입니다");
        }
        if (!StringUtils.hasText(addItemFormDto.getType())){
            errors.put("type", "악기타입 입력 필수입니다");
        }
        if (!StringUtils.hasText(addItemFormDto.getLevel())){
            errors.put("level", "난이도 입력 필수입니다 ");
        }
        if (addItemFormDto.getPrice()==null){
            errors.put("price", "가격 입력 필수입니다 ");
        }
        if (!StringUtils.hasText(addItemFormDto.getContent())){
            errors.put("content", "내용 입력 필수입니다");
        }
        if (!errors.isEmpty()){
            return errors;
        }

        musicListRepository.save(musicList);
        fileRepository.save(fileList);
        return null;
    }

    public Page<MusicList> findAllItemList(int page) {
        Pageable pageable = PageRequest.of(page, 15);
        return musicListRepository.findAll(pageable);
    }
}
