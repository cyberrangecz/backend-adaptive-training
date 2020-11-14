package com.example.demo.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class GameDefinitionCreateDto {
    private Long id;
    private String title;
    private Integer order;
    private LevelType type;
    private List<? extends GameDefinitionCreateDto> subLevels;
}
