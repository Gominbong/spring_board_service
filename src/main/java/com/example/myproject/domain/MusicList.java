package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MusicList {

    @Id
    @GeneratedValue
    @Column(name = "musicList_id")
    private Long id;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String loginId;
    private String memberNickname;
    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private String type;
    private String level;
    private Integer price;
    private LocalDateTime localDateTime;
    private String softDelete;


}
