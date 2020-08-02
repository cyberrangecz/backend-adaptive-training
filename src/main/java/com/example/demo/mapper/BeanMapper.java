package com.example.demo.mapper;

import com.example.demo.domain.AssessmentLevel;
import com.example.demo.domain.Attachment;
import com.example.demo.domain.GameLevel;
import com.example.demo.domain.Hint;
import com.example.demo.domain.InfoLevel;
import com.example.demo.dto.AssessmentLevelDto;
import com.example.demo.dto.AttachmentDto;
import com.example.demo.dto.GameLevelCreateDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.dto.HintDto;
import com.example.demo.dto.InfoLevelCreateDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface BeanMapper {

    BeanMapper INSTANCE = Mappers.getMapper(BeanMapper.class);

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

    HintDto toDto(Hint hint);

    Hint toEntity(HintDto hint);

    AttachmentDto toDto(Attachment hint);

    Attachment toEntity(AttachmentDto hint);
}
