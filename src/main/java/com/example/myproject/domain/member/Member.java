package com.example.myproject.domain.member;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Entity
@Slf4j
@RequiredArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String nickname;

}
