package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDate localDate;
    private String musicListTitle;
    private String loginId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;

}
