package com.example.demo.service;

import com.example.demo.dto.GameLevelDto;

import java.util.List;

public interface GameLevelService {

    List<GameLevelDto> findAllGameLevels();

    Long updateGameLevel(Long id, GameLevelDto gameLevelDto);
}
