package com.example.myproject.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
public class AddMusicListFormDto {

    private String title;
    private String type;
    private String level;
    private Integer price;
    private String content;

    private LocalDateTime localDateTime;

    private MultipartFile pdfFile;
    private String originalFileName;
    private String storedFileName;


}
