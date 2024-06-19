package com.example.myproject.repository;

import com.example.myproject.domain.FileList;

import java.util.List;


public interface FileListRepositoryCustom {
    List<FileList> findByMusicListIdQueryDsl(Long musicListId);

    FileList findByStoredFilenameQueryDsl(String storedFilename);
}
