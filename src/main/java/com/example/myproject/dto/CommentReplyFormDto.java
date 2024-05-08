package com.example.myproject.dto;

import lombok.Data;

@Data
public class CommentReplyFormDto {
    private Long commentId;
    private String replyContent;
}
