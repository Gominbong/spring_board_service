package com.example.myproject.dto;

import lombok.Data;

@Data
public class CommentReplyFormDto {
    //대댓글 부모 댓글Id
    private Long commentId;
    private String replyContent;
}
