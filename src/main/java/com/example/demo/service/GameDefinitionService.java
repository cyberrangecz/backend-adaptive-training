package com.example.demo.service;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.UnityLevel;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import com.example.demo.dto.input.LevelType;
import com.example.demo.repository.AssessmentLevelRepository;
import com.example.demo.repository.GameLevelRepository;
import com.example.demo.repository.UnityLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public GameDefinitionCreateDto createGameDefinition(List<GameDefinitionCreateDto> gameDefinition) {

        for (GameDefinitionCreateDto gameDefinitionCreateDto : gameDefinition) {
            if (gameDefinitionCreateDto.getType() == LevelType.assessment) {
                AssessmentLevel assessmentLevel = new AssessmentLevel();
                assessmentLevel.setTitle(gameDefinitionCreateDto.getTitle());
                assessmentLevel.setOrderInTrainingDefinition(gameDefinitionCreateDto.getOrder());

                assessmentLevelRepository.save(assessmentLevel);
            } else if (gameDefinitionCreateDto.getType() == LevelType.unity) {
                UnityLevel unityLevel = new UnityLevel();
                unityLevel.setTitle(gameDefinitionCreateDto.getTitle());
                unityLevel.setOrderInTrainingDefinition(gameDefinitionCreateDto.getOrder());

                unityLevelRepository.save(unityLevel);


                List<BaseLevel> subLevels = new ArrayList<>();
                for (GameDefinitionCreateDto subLevel : gameDefinitionCreateDto.getSubLevels()) {
                    if (subLevel.getType() == LevelType.game) {
                        GameLevel gameLevel = new GameLevel();
                        gameLevel.setTitle(subLevel.getTitle());
                        gameLevel.setOrderInTrainingDefinition(subLevel.getOrder());
                        gameLevel.setUnityLevel(unityLevel);

                        subLevels.add(gameLevel);
                        gameLevelRepository.save(gameLevel);
                    }
                }
                unityLevel.setSubLevels(subLevels);
                unityLevelRepository.save(unityLevel);
            }
        }

        return null;
    }
}
