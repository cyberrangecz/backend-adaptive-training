package com.example.demo.service;

import com.example.demo.domain.GameLevel;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.GameLevelRepository;
import com.example.demo.service.GameLevelService;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameLevelService {

    private final GameLevelRepository gameLevelRepository;

    @Autowired
    public GameLevelService(GameLevelRepository gameLevelRepository) {
        this.gameLevelRepository = gameLevelRepository;
    }

    public List<GameLevelDto> findAllGameLevels() {

        Iterable<GameLevel> allGameLevels = gameLevelRepository.findAll();

        List<GameLevelDto> result = new ArrayList<>();

        if (!IterableUtils.isEmpty(allGameLevels)) {
            for (GameLevel gameLevel : allGameLevels) {
                result.add(BeanMapper.INSTANCE.toDto(gameLevel));
            }
        }

        return result;
    }

    public GameLevelDto updateGameLevel(Long id, GameLevelDto gameLevelDto) {
        GameLevel gameLevel = BeanMapper.INSTANCE.toEntity(gameLevelDto);

        GameLevel savedEntity = gameLevelRepository.save(gameLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }
}
