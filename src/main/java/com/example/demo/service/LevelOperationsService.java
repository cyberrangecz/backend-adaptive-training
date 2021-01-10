package com.example.demo.service;

import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.InfoLevel;
import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.input.LevelType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.BaseLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LevelOperationsService {

    @Autowired
    private BaseLevelRepository baseLevelRepository;

    @Autowired
    private InfoLevelService infoLevelService;

    @Autowired
    private AssessmentLevelService assessmentLevelService;

    @Autowired
    private PhaseLevelService phaseLevelService;

    @Autowired
    private GameLevelService gameLevelService;

    public void swapLevelsOrder(Long levelIdFrom, Long levelIdTo) {
        Optional<BaseLevel> levelFrom = baseLevelRepository.findById(levelIdFrom);
        Optional<BaseLevel> levelTo = baseLevelRepository.findById(levelIdTo);

        if (levelFrom.isEmpty() || levelTo.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        int fromOrder = levelFrom.get().getOrder();
        int toOrder = levelTo.get().getOrder();

        levelFrom.get().setOrder(toOrder);
        levelTo.get().setOrder(fromOrder);

        baseLevelRepository.save(levelFrom.get());
        baseLevelRepository.save(levelTo.get());
    }

    public void deleteLevel(Long levelId) {
        Optional<BaseLevel> levelEntity = baseLevelRepository.findById(levelId);

        if (levelEntity.isEmpty()) {
            // TODO throw a proper exception
            return;
        }

        // TODO get all the levels in training definition and decrease their order attribute if needed

        baseLevelRepository.delete(levelEntity.get());
    }

    public BaseLevelDto createLevel(Long trainingDefinitionId, LevelType levelType) {
        BaseLevelDto baseLevelDto;
        if (levelType.equals(LevelType.info)) {
            baseLevelDto = infoLevelService.createDefaultInfoLevel(trainingDefinitionId);
        } else if (levelType.equals(LevelType.assessment)) {
            baseLevelDto = assessmentLevelService.createDefaultAssessmentLevel(trainingDefinitionId);
        } else {
            baseLevelDto = phaseLevelService.createDefaultPhaseLevel(trainingDefinitionId);
        }

        baseLevelDto.setType(levelType);

        return baseLevelDto;
    }

    public BaseLevelDto createTask(Long phaseId) {
        GameLevelDto createdTask = gameLevelService.createDefaultGameLevel(phaseId);
        createdTask.setType(LevelType.game);

        return createdTask;
    }

    public BaseLevelDto getLevel(Long levelId) {
        Optional<BaseLevel> level = baseLevelRepository.findById(levelId);

        if (level.isEmpty()) {
            // TODO throw 404
            return null;
        }

        return BeanMapper.INSTANCE.toDto(level.get());
    }

    public void updateInfoLevel(InfoLevelUpdateDto infoLevelUpdateDto) {
        InfoLevel infoLevel = BeanMapper.INSTANCE.toEntity(infoLevelUpdateDto);
        infoLevelService.updateInfoLevel(infoLevel);
    }

    public void updateTask(GameLevelUpdateDto gameLevelUpdateDto) {
        GameLevel gameLevel = BeanMapper.INSTANCE.toEntity(gameLevelUpdateDto);
        gameLevelService.updateGameLevel(gameLevel);
    }
}
