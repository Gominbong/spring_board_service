package com.example.myproject.dto;

import com.example.myproject.domain.Comment;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long memberId;
    private String memberName;
    private Long parentMemberId;
    private String parentMemberName;
    private Long musicListId;
    private String musicListTitle;
    private Long parentId;
    private String createTime;
    private int depth;
    private String softDelete;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.memberId = comment.getMember() != null ? comment.getMember().getId() : null;
        this.memberName = comment.getMember() != null ? comment.getMember().getNickname() : null;
        this.parentMemberId = comment.getParentMember() != null ? comment.getParentMember().getId() : null;
        this.parentMemberName = comment.getParentMember() != null ? comment.getParentMember().getNickname() : null;
        this.musicListId = comment.getMusicList() != null ? comment.getMusicList().getId() : null;
        this.musicListTitle = comment.getMusicList() != null ? comment.getMusicList().getTitle() : null;
        this.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        this.createTime = comment.getCreateTime();
        this.depth = comment.getDepth();
        this.softDelete = comment.getSoftDelete();
    }

    // getter, setter, toString 생략
}
