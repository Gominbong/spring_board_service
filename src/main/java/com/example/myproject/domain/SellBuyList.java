package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SellBuyList {

    @Id
    @GeneratedValue
    private Long id;
    private String buyMemberLoginId;
    private String sellMemberLoginId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;
    private LocalDateTime localDateTime;

}