package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LikeCount {

    @Id
    @GeneratedValue
    @Column(name = "likeCount_id")
    private Long id;
    private String loginId;
    private Long musicListId;

}
