package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FileList {

    @Id
    @GeneratedValue
    @Column(name = "fileList_id")
    private Long id;

    private String originalFilename;
    private String storedFilename;


}
