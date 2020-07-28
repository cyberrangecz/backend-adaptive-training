package com.example.demo.service;

import com.example.demo.domain.GameLevel;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.GameLevelRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameLevelService {

    private static final Logger LOG = LoggerFactory.getLogger(GameLevelService.class);

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

    public GameLevelDto updateGameLevel(Long id, GameLevelUpdateDto gameLevelUpdateDto) {
        Optional<GameLevel> persistedGameLevel = gameLevelRepository.findById(id);

        if (persistedGameLevel.isEmpty()) {
            LOG.error("No game level found with attribute {}.", id);
            return new GameLevelDto();
        }

        GameLevel gameLevel = BeanMapper.INSTANCE.toEntity(gameLevelUpdateDto);
        gameLevel.setId(persistedGameLevel.get().getId());

        GameLevel savedEntity = gameLevelRepository.save(gameLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }
}
