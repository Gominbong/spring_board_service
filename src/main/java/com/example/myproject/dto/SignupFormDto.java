package com.example.myproject.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SignupFormDto {
    private String id;
    private String pw;
    private String nick;
    private LocalDate localDate;

}