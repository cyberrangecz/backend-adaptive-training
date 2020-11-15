package com.example.demo.service;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.InfoLevel;
import com.example.demo.domain.TrainingDefinition;
import com.example.demo.domain.UnityLevel;
import com.example.demo.dto.TrainingDefinitionDto;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import com.example.demo.dto.input.LevelType;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.AssessmentLevelRepository;
import com.example.demo.repository.GameLevelRepository;
import com.example.demo.repository.InfoLevelRepository;
import com.example.demo.repository.TrainingDefinitionRepository;
import com.example.demo.repository.UnityLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameDefinitionService {

    @Autowired
    private AssessmentLevelRepository assessmentLevelRepository;

    @Autowired
    private UnityLevelRepository unityLevelRepository;

    @Autowired
    private GameLevelRepository gameLevelRepository;

    @Autowired
    private InfoLevelRepository infoLevelRepository;

    @Autowired
    private TrainingDefinitionRepository trainingDefinitionRepository;

    public GameDefinitionCreateDto createGameDefinition(TrainingDefinitionDto trainingDefinitionDto) {

        TrainingDefinition trainingDefinition = BeanMapper.INSTANCE.toEntity(trainingDefinitionDto);
        trainingDefinitionRepository.save(trainingDefinition);


        // TODO refactor this, it's really ugly
        for (GameDefinitionCreateDto gameDefinitionCreateDto : trainingDefinitionDto.getLevels()) {
            if (gameDefinitionCreateDto.getType() == LevelType.assessment) {
                AssessmentLevel assessmentLevel = BeanMapper.INSTANCE.toAssessmentLevel(gameDefinitionCreateDto);
                assessmentLevel.setTrainingDefinition(trainingDefinition);
                assessmentLevelRepository.save(assessmentLevel);
            } else if (gameDefinitionCreateDto.getType() == LevelType.unity) {
                UnityLevel unityLevel = BeanMapper.INSTANCE.toUnityLevel(gameDefinitionCreateDto);
                unityLevel.setTrainingDefinition(trainingDefinition);

                if (!CollectionUtils.isEmpty(unityLevel.getSubLevels())) {
                    unityLevel.getSubLevels().forEach(x -> x.setUnityLevel(unityLevel));
                    unityLevel.getSubLevels().forEach(x -> x.setTrainingDefinition(trainingDefinition));
                }

                unityLevelRepository.save(unityLevel);
            } else if (gameDefinitionCreateDto.getType() == LevelType.game) {
                GameLevel gameLevel = BeanMapper.INSTANCE.toGameLevel(gameDefinitionCreateDto);
                gameLevel.setTrainingDefinition(trainingDefinition);
                gameLevelRepository.save(gameLevel);

            } else if (gameDefinitionCreateDto.getType() == LevelType.info) {
                InfoLevel infoLevel = BeanMapper.INSTANCE.toInfoLevel(gameDefinitionCreateDto);
                infoLevel.setTrainingDefinition(trainingDefinition);
                infoLevelRepository.save(infoLevel);
            }
        }

        return null;
    }
}
