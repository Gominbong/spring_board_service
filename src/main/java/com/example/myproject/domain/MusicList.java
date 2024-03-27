package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Data
@NoArgsConstructor
public class MusicList {

    @Id
    @GeneratedValue
    @Column(name = "musicList_id")
    private Long id;

    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    @Column(columnDefinition = "text")
    private String content;
    private String type;
    private String level;
    private int price;
    private LocalDateTime localDateTime;

    @OneToMany(mappedBy = "musicList", fetch = LAZY)
    private List<FileList> fileLists = new ArrayList<>();


}
