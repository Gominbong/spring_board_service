package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "parentMember_id")
    private Member parentMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;

    private int parent;
    private int child;
    private int child1;
    private int child2;
    private int child3;
    private String createTime;
    private int divWidthSize;
    private String softDelete;

    private String parentMemberNickname;
    private String parentMemberLoginId;




}
