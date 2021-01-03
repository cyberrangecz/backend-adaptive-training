package com.example.demo.mapper;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.Attachment;
import com.example.demo.domain.BaseLevel;
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

    AssessmentLevelDto toDto(BaseLevel baseLevel);

    AssessmentLevelDto toDto(AssessmentLevel assessmentLevel);

    AssessmentLevel toEntity(AssessmentLevelDto assessmentLevel);

    GameLevelDto toDto(GameLevel gameLevel);

    GameLevel toEntity(GameLevelDto gameLevel);

    GameLevel toEntity(GameLevelCreateDto gameLevel);

    GameLevel toEntity(GameLevelUpdateDto gameLevel);

    InfoLevelDto toDto(InfoLevel infoLevel);

    InfoLevel toEntity(InfoLevelDto infoLevel);

    InfoLevel toEntity(InfoLevelCreateDto gameLevel);

    InfoLevel toEntity(InfoLevelUpdateDto gameLevel);

    AttachmentDto toDto(Attachment attachment);

    Attachment toEntity(AttachmentDto attachment);

    PhaseLevelDto toDto(PhaseLevel phaseLevel);

    PhaseLevel toEntity(PhaseLevelDto phaseLevel);

    AssessmentLevel toAssessmentLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    InfoLevel toInfoLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    GameLevel toGameLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    PhaseLevel toPhaseLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    AssessmentLevel updateAssessmentLevel(@MappingTarget AssessmentLevel assessmentLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    InfoLevel updateInfoLevel(@MappingTarget InfoLevel infoLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "phaseLevel", ignore = true)
    @Mapping(target = "attachments", ignore = true) // TODO not really sure about this
    GameLevel updateGameLevel(@MappingTarget GameLevel gameLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "subLevels", ignore = true)
    PhaseLevel updatePhaseLevel(@MappingTarget PhaseLevel phaseLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "type", constant = "assessment")
    GameDefinitionCreateDto toLevelDefinitionDto(AssessmentLevel assessmentLevel);

    @Mapping(target = "type", constant = "game")
    GameDefinitionCreateDto toLevelDefinitionDto(GameLevel gaLevel);

    @Mapping(target = "type", constant = "info")
    GameDefinitionCreateDto toLevelDefinitionDto(InfoLevel infoLevel);

    @Mapping(target = "type", constant = "phase")
    GameDefinitionCreateDto toLevelDefinitionDto(PhaseLevel phaseLevel);
}
