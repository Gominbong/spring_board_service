package com.example.myproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class MusicListFormDto {

    private String title;
    private String type;
    private String level;
    private Integer price;
    private String content;

    private LocalDateTime localDateTime;
    private List<MultipartFile> pdfFiles;

}
