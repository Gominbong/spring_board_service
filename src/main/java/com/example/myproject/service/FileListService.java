package com.example.myproject.service;

import com.example.myproject.domain.FileList;
import com.example.myproject.repository.FileListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileListService {

    private final FileListRepository fileListRepository;


    public List<FileList> findByFiles(Long id) {
        return fileListRepository.findByMusicListId(id);
    }

    public FileList findById(Long fileId) {
        return fileListRepository.findById(fileId).orElseThrow();
    }
}
