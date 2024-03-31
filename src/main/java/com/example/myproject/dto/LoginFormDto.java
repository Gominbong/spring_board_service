package com.example.myproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginFormDto {

    @NotEmpty
    private String id;
    @NotEmpty
    private String pw;
}