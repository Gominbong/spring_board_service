package com.example.myproject.dto;

import com.example.myproject.domain.MusicList;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
public class AddItemFormDto {


    private String title;
    private String type;
    private String level;
    private int price;
    private String content;
    private LocalDateTime createDateTime;

    private MultipartFile pdfFile;
    private String originalFileName;
    private String storedFileName;


}
