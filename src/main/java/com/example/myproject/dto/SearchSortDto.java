package com.example.myproject.dto;

import lombok.Data;

@Data
public class SearchSortDto {
    private String sortType;
    private String searchType;
    private String search;
}
