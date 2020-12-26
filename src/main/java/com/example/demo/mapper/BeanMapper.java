package com.example.demo.mapper;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.Attachment;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.InfoLevel;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.dto.AssessmentLevelDto;
import com.example.demo.dto.AttachmentDto;
import com.example.demo.dto.GameLevelCreateDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.dto.InfoLevelCreateDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.PhaseLevelDto;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    AttachmentDto toDto(Attachment attachment);

    Attachment toEntity(AttachmentDto attachment);

    PhaseLevelDto toDto(PhaseLevel phaseLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    PhaseLevel toEntity(PhaseLevelDto phaseLevel);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    AssessmentLevel toAssessmentLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    InfoLevel toInfoLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    GameLevel toGameLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    PhaseLevel toPhaseLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    AssessmentLevel updateAssessmentLevel(@MappingTarget AssessmentLevel assessmentLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    InfoLevel updateInfoLevel(@MappingTarget InfoLevel infoLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    @Mapping(target = "phaseLevel", ignore = true)
    @Mapping(target = "attachments", ignore = true) // TODO not really sure about this
    GameLevel updateGameLevel(@MappingTarget GameLevel gameLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "orderInTrainingDefinition", source = "order")
    @Mapping(target = "subLevels", ignore = true)
    PhaseLevel updatePhaseLevel(@MappingTarget PhaseLevel phaseLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "type", constant = "assessment")
    GameDefinitionCreateDto toLevelDefinitionDto(AssessmentLevel assessmentLevel);

    @Mapping(target = "type", constant = "game")
    GameDefinitionCreateDto toLevelDefinitionDto(GameLevel gaLevel);

    @Mapping(target = "type", constant = "info")
    GameDefinitionCreateDto toLevelDefinitionDto(InfoLevel infoLevel);

    @Mapping(target = "type", constant = "unity")
    GameDefinitionCreateDto toLevelDefinitionDto(PhaseLevel phaseLevel);
}
