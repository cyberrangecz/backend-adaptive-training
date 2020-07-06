package com.example.demo.mapper;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.Hint;
import com.example.demo.domain.InfoLevel;
import com.example.demo.dto.AssessmentLevelDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.HintDto;
import com.example.demo.dto.InfoLevelDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ObjectMapper {

    ObjectMapper INSTANCE = Mappers.getMapper(ObjectMapper.class);

    AssessmentLevelDto toDto(AssessmentLevel assessmentLevel);

    AssessmentLevel toEntity(AssessmentLevelDto assessmentLevel);

    GameLevelDto toDto(GameLevel gameLevel);

    GameLevel toEntity(GameLevelDto gameLevel);

    InfoLevelDto toDto(InfoLevel infoLevel);

    InfoLevel toEntity(InfoLevelDto infoLevel);

    HintDto toDto(Hint hint);

    Hint toEntity(HintDto hint);
}
