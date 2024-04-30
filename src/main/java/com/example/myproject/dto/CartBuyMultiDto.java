package com.example.myproject.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartBuyMultiDto {
    private List<Long> musicListId;
    private List<Long> cartListId;
}
