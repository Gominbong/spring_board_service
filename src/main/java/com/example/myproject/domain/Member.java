package com.example.myproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Member {

    @GeneratedValue
    @Id
    @Column(name = "member_id")
    private Long id;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String nickname;
    private int cash;
    private LocalDate localDate;



}
