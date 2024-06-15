package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;
    private String createTime;
    private String loginId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;

}
