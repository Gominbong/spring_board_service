package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.domain.Member;
import com.example.myproject.domain.MusicList;
import com.example.myproject.dto.MusicListFormDto;
import com.example.myproject.dto.UpdateMusicListFormDto;
import com.example.myproject.repository.MusicListRepository;
import com.example.myproject.repository.FileListRepository;
import com.example.myproject.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                                             String loginId) {

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
        musicList.setViews(0);
        musicList.setLikeCount(0);
        musicList.setSalesQuantity(0);
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        String temp = String.valueOf(localDateTime);
        String createTime = temp.replace("T", " ");
        musicList.setCreateTime(createTime);
        musicListRepository.save(musicList);

        List<MultipartFile> pdfFiles = musicListFormDto.getPdfFiles();

        if(!pdfFiles.get(0).isEmpty()){
            for(MultipartFile multipartFile : pdfFiles ){
                String originalFilename = multipartFile.getOriginalFilename();
                String Path = "C:/Users/asd/Desktop/study/pdf/";
                String encode = originalFilename.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "_");
                String storedFileName = System.currentTimeMillis() + "_" + encode;
                try {
                    multipartFile.transferTo(new File(Path+storedFileName));
                } catch (IOException e) {
                    e.getStackTrace();
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
        return musicListRepository.findBySoftDeleteIsNull(pageable);
    }

    public MusicList findById(Long id) {
        return musicListRepository.findByMusicList(id);
    }


    @Transactional
    public void deleteMusicList(Long id) {
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicList.setSoftDelete("Yes");

    }

    @Transactional
    public Map<String, String> updateMusicList(Long id, UpdateMusicListFormDto updateMusicListFormDto) {

        Map<String, String> errors = new HashMap<>();

        if (!StringUtils.hasText(updateMusicListFormDto.getType())){
            errors.put("type", "악기타입 입력 필수입니다");
        }
        if (!StringUtils.hasText(updateMusicListFormDto.getTitle())){
            errors.put("title", "제목 입력 필수입니다");
        }
        if (!StringUtils.hasText(updateMusicListFormDto.getLevel())){
            errors.put("level", "난이도 입력 필수입니다 ");
        }
        if (updateMusicListFormDto.getPrice()==null){
            errors.put("price", "가격 입력 필수입니다 ");
        }
        if (!errors.isEmpty()){
            return errors;
        }

        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        musicList.setTitle(updateMusicListFormDto.getTitle());
        musicList.setType(updateMusicListFormDto.getType());
        musicList.setLevel(updateMusicListFormDto.getLevel());
        musicList.setPrice(updateMusicListFormDto.getPrice());
        musicList.setContent(updateMusicListFormDto.getContent());

        List<String> filename = updateMusicListFormDto.getFilename();
        log.info("업데이트디티오 = {}", updateMusicListFormDto.toString());
        if (filename != null){
            for (String file : filename) {
                log.info("수정페이지에서 삭제버튼누른 파일이름 = '{}'", file);
                FileList fileList = fileRepository.findByStoredFilename(file);
                fileRepository.delete(fileList);
                File file1 = new File("C:/Users/asd/Desktop/study/pdf/"+file);
                file1.delete();
            }
        }

        List<MultipartFile> pdfFiles = updateMusicListFormDto.getPdfFiles();
        if(!pdfFiles.get(0).isEmpty()){
            for(MultipartFile multipartFile : pdfFiles ){
                String originalFilename = multipartFile.getOriginalFilename();
                String Path = "C:/Users/asd/Desktop/study/pdf/";
                String encode = originalFilename.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]", "_");
                String storedFileName = System.currentTimeMillis() + "_" + encode;

                try {
                    multipartFile.transferTo(new File(Path+storedFileName));
                } catch (IOException e) {

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

    public UpdateMusicListFormDto setUpdateMusicListFormDto(Long id) {
        UpdateMusicListFormDto updateMusicListFormDto = new UpdateMusicListFormDto();
        MusicList musicList = musicListRepository.findById(id).orElseThrow();
        updateMusicListFormDto.setTitle(musicList.getTitle());
        updateMusicListFormDto.setType(musicList.getType());
        updateMusicListFormDto.setLevel(musicList.getLevel());
        updateMusicListFormDto.setPrice(musicList.getPrice());
        updateMusicListFormDto.setContent(musicList.getContent());

        return updateMusicListFormDto;

    }
}
