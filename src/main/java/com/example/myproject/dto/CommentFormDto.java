package com.example.myproject.dto;

import lombok.Data;

@Data
public class CommentFormDto {
    private Long musicListId;
    private Long parent;
    private String commentContent;

}
