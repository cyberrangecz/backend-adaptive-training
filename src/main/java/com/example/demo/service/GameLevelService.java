package com.example.demo.service;

import com.example.demo.dto.GameLevelDto;

import java.util.List;

public interface GameLevelService {

    List<GameLevelDto> findAllGameLevels();
}
