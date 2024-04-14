package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FileList {

    @Id
    @GeneratedValue
    @Column(name = "fileList_id")
    private Long id;
    private String originalFilename;
    private String storedFilename;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;
}
