package com.example.myproject.repository;

import com.example.myproject.domain.FileList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileListRepository extends JpaRepository<FileList, Long> {

    List<FileList> findByMusicListId(Long musicListId);

    FileList findByStoredFilename(String storedFilename);

}
