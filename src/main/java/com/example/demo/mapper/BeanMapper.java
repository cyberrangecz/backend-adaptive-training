package com.example.demo.mapper;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.Attachment;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.Hint;
import com.example.demo.domain.InfoLevel;
import com.example.demo.domain.TrainingDefinition;
import com.example.demo.domain.UnityLevel;
import com.example.demo.dto.AssessmentLevelDto;
import com.example.demo.dto.AttachmentDto;
import com.example.demo.dto.GameLevelCreateDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.dto.HintDto;
import com.example.demo.dto.InfoLevelCreateDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.TrainingDefinitionDto;
import com.example.demo.dto.UnityLevelDto;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface BeanMapper {

    BeanMapper INSTANCE = Mappers.getMapper(BeanMapper.class);

    AssessmentLevelDto toDto(AssessmentLevel assessmentLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    AssessmentLevel toEntity(AssessmentLevelDto assessmentLevel);

    GameLevelDto toDto(GameLevel gameLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    GameLevel toEntity(GameLevelDto gameLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    GameLevel toEntity(GameLevelCreateDto gameLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    GameLevel toEntity(GameLevelUpdateDto gameLevel);

    InfoLevelDto toDto(InfoLevel infoLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    InfoLevel toEntity(InfoLevelDto infoLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    InfoLevel toEntity(InfoLevelCreateDto gameLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    InfoLevel toEntity(InfoLevelUpdateDto gameLevel);

    HintDto toDto(Hint hint);

    Hint toEntity(HintDto hint);

    AttachmentDto toDto(Attachment hint);

    Attachment toEntity(AttachmentDto hint);

    UnityLevelDto toDto(UnityLevel unityLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    UnityLevel toEntity(UnityLevelDto unityLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    AssessmentLevel toAssessmentLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    InfoLevel toInfoLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    GameLevel toGameLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    UnityLevel toUnityLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "type", constant = "assessment")
    GameDefinitionCreateDto toLevelDefinitionDto(AssessmentLevel assessmentLevel);

    @Mapping(target = "type", constant = "game")
    GameDefinitionCreateDto toLevelDefinitionDto(GameLevel gaLevel);

    @Mapping(target = "type", constant = "info")
    GameDefinitionCreateDto toLevelDefinitionDto(InfoLevel infoLevel);

    @Mapping(target = "type", constant = "unity")
    GameDefinitionCreateDto toLevelDefinitionDto(UnityLevel unityLevel);

    @Mapping(target = "levels", ignore = true)
    TrainingDefinition toEntity(TrainingDefinitionDto trainingDefinition);

    @Mapping(target = "levels", ignore = true)
    TrainingDefinitionDto toDto(TrainingDefinition trainingDefinition);
}
