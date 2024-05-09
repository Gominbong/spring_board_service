package com.example.myproject.dto;

import lombok.Data;

@Data
public class CommentUpdateDto {
    private Long commentId;
    private String commentEditContent;
}
