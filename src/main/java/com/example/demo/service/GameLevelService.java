package com.example.demo.service;

import com.example.demo.domain.GameLevel;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.dto.GameLevelCreateDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.GameLevelRepository;
import com.example.demo.repository.PhaseLevelRepository;
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
    private final PhaseLevelRepository phaseLevelRepository;

    @Autowired
    public GameLevelService(GameLevelRepository gameLevelRepository, PhaseLevelRepository phaseLevelRepository) {
        this.gameLevelRepository = gameLevelRepository;
        this.phaseLevelRepository = phaseLevelRepository;
    }

    public GameLevelDto createDefaultGameLevel(Long phaseId) {
        Optional<PhaseLevel> phaseLevel = phaseLevelRepository.findById(phaseId);
        if (phaseLevel.isEmpty()) {
            // TODO return 404
            return null;
        }

        GameLevel gameLevel = new GameLevel();
        gameLevel.setTitle("Title of task");
        gameLevel.setPhaseLevel(phaseLevel.get());
        gameLevel.setOrder(gameLevelRepository.getCurrentMaxOrder(phaseId) + 1);

        GameLevel persistedEntity = gameLevelRepository.save(gameLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }


    public GameLevelDto createGameLevel(GameLevelCreateDto gameLevelCreateDto) {
        GameLevel gameLevel = BeanMapper.INSTANCE.toEntity(gameLevelCreateDto);

        GameLevel persistedEntity = gameLevelRepository.save(gameLevel);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
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

    public GameLevelDto getGameLevel(Long id) {
        Optional<GameLevel> gameLevel = gameLevelRepository.findById(id);

        if (gameLevel.isEmpty()) {
            LOG.error("No game level found with ID {}.", id);
            return new GameLevelDto();
        }

        return BeanMapper.INSTANCE.toDto(gameLevel.get());
    }

    public GameLevelDto updateGameLevel(Long id, GameLevelUpdateDto gameLevelUpdateDto) {
        Optional<GameLevel> persistedGameLevel = gameLevelRepository.findById(id);

        if (persistedGameLevel.isEmpty()) {
            LOG.error("No game level found with ID {}.", id);
            return new GameLevelDto();
        }

        GameLevel gameLevel = BeanMapper.INSTANCE.toEntity(gameLevelUpdateDto);
        gameLevel.setId(persistedGameLevel.get().getId());

        GameLevel savedEntity = gameLevelRepository.save(gameLevel);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void removeGameLevel(Long id) {
        gameLevelRepository.deleteById(id);
    }
}
