package com.example.myproject.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDeleteMultiDto {
    private List<Long> cartListId;
}
