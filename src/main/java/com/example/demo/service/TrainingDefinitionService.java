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
import com.example.demo.util.TrainingMapperHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TrainingDefinitionService {

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

    @Autowired
    private TrainingMapperHelper trainingMapperHelper;

    public GameDefinitionCreateDto createTrainingDefinition(TrainingDefinitionDto trainingDefinitionDto) {

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

    public TrainingDefinitionDto getTrainingDefinition(Long id) {
        Optional<TrainingDefinition> trainingDefinition = trainingDefinitionRepository.findById(id);

        if (trainingDefinition.isEmpty()) {
            // TODO throw 404
            return new TrainingDefinitionDto();
        }

        TrainingDefinitionDto trainingDefinitionDto = BeanMapper.INSTANCE.toDto(trainingDefinition.get());
        trainingDefinitionDto.setLevels(trainingMapperHelper.getLevelsFrom(trainingDefinition.get()));

        return trainingDefinitionDto;
    }

    public List<TrainingDefinitionDto> getAllTrainingDefinitions() {
        List<TrainingDefinition> trainingDefinitions = trainingDefinitionRepository.findAll();

        return BeanMapper.INSTANCE.toDto(trainingDefinitions);
    }

    public GameDefinitionCreateDto updateTrainingDefinition(Long id, TrainingDefinitionDto trainingDefinitionDto) {
        Optional<TrainingDefinition> trainingDefinition = trainingDefinitionRepository.findById(id);

        if (trainingDefinition.isEmpty()) {
            // TODO throw 404
            return new GameDefinitionCreateDto();
        }
        TrainingDefinition trainingDefinitionEntity = trainingDefinition.get();

        TrainingDefinition convertedEntity = BeanMapper.INSTANCE.toEntity(trainingDefinitionDto);
        convertedEntity.setId(trainingDefinitionEntity.getId());


        for (BaseLevel originalLevel : trainingDefinitionEntity.getLevels()) {
            for (GameDefinitionCreateDto updatedLevel : trainingDefinitionDto.getLevels()) {
                if (Objects.equals(originalLevel.getId(), updatedLevel.getId())) {
                    originalLevel = updateTrainingLevel(originalLevel, updatedLevel);
                    originalLevel.setTrainingDefinition(convertedEntity);
                }
            }
        }

        convertedEntity.setLevels(trainingDefinitionEntity.getLevels());

        trainingDefinitionRepository.save(convertedEntity);

        return null;
    }

    private <T extends BaseLevel> BaseLevel updateTrainingLevel(T originalLevel, GameDefinitionCreateDto updatedLevel) {
        BaseLevel updatedEntity = null;
        if (updatedLevel.getType() == LevelType.assessment) {
            updatedEntity = BeanMapper.INSTANCE.updateAssessmentLevel((AssessmentLevel) originalLevel, updatedLevel);
        } else if (updatedLevel.getType() == LevelType.unity) {
            updatedEntity = BeanMapper.INSTANCE.updateUnityLevel((UnityLevel) originalLevel, updatedLevel);

            for (GameLevel originalSubLevel : ((UnityLevel) originalLevel).getSubLevels()) {
                for (GameDefinitionCreateDto updatedSubLevel : updatedLevel.getSubLevels()) {
                    if (Objects.equals(originalSubLevel.getId(), updatedSubLevel.getId())) {
                        originalSubLevel = BeanMapper.INSTANCE.updateGameLevel(originalSubLevel, updatedSubLevel);
                    }
                }
            }
        } else if (updatedLevel.getType() == LevelType.game) {
            updatedEntity = BeanMapper.INSTANCE.updateGameLevel((GameLevel) originalLevel, updatedLevel);
        } else if (updatedLevel.getType() == LevelType.info) {
            updatedEntity = BeanMapper.INSTANCE.updateInfoLevel((InfoLevel) originalLevel, updatedLevel);
        }

        return updatedEntity;
    }
}
