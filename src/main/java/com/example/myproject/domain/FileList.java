package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FileList {

    @Id
    @GeneratedValue
    private Long id;

    private String originalFilename;
    private String storedFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;


}
