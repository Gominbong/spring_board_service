package com.example.myproject.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SignupFormDto {
    private String id;
    private String pw;
    private String nick;
    private LocalDate localDate;

}