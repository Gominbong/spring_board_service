package com.example.myproject.repository;

import com.example.myproject.domain.FileList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileList, Long> {

}
