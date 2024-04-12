package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.repository.FileListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileListService {

    private final FileListRepository fileListRepository;


    public List<FileList> findByFiles(Long id) {
        return fileListRepository.findByMusicList_Id(id);
    }

    public FileList findById(Long fileId) {
        return fileListRepository.findById(fileId).orElseThrow();
    }
}
