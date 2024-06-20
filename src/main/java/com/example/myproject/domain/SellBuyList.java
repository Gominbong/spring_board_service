package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class SellBuyList {

    @Id
    @GeneratedValue
    @Column(name = "sellBuyList_id")
    private Long id;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "buy_Member_id")
    private Member buyMember;
    @ManyToOne (fetch = LAZY)
    @JoinColumn(name = "sell_Member_id")
    private Member sellMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musicList_id")
    private MusicList musicList;
    private String createTime;

}