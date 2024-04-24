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
    private Long id;
    private Integer count;
    private String loginId;
    private Long musicListId;

}
