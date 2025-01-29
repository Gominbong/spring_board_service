package com.example.myproject.dto;


import lombok.Data;

@Data
public class CommentReplyFormDto {

    private Long musicListId;
    private String replyContent;
    private Long parentId;

}
