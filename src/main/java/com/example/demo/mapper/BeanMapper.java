package com.example.demo.mapper;

import com.example.demo.domain.AbstractPhase;
import com.example.demo.domain.DecisionMatrixRow;
import com.example.demo.domain.InfoPhase;
import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionChoice;
import com.example.demo.domain.QuestionPhaseRelation;
import com.example.demo.domain.QuestionnairePhase;
import com.example.demo.domain.Task;
import com.example.demo.domain.TrainingPhase;
import com.example.demo.dto.AbstractPhaseDto;
import com.example.demo.dto.DecisionMatrixRowDto;
import com.example.demo.dto.InfoPhaseDto;
import com.example.demo.dto.InfoPhaseUpdateDto;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionPhaseRelationDto;
import com.example.demo.dto.QuestionRequiredIdDto;
import com.example.demo.dto.QuestionUpdateDto;
import com.example.demo.dto.QuestionnairePhaseDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.dto.TrainingPhaseDto;
import com.example.demo.dto.TrainingPhaseUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface BeanMapper {

    BeanMapper INSTANCE = Mappers.getMapper(BeanMapper.class);

    default AbstractPhaseDto toDto(AbstractPhase abstractPhase) {
        AbstractPhaseDto abstractPhaseDto;
        if (abstractPhase instanceof TrainingPhase) {
            abstractPhaseDto = toDto((TrainingPhase) abstractPhase);
        } else if (abstractPhase instanceof InfoPhase) {
            abstractPhaseDto = toDto((InfoPhase) abstractPhase);
        } else if (abstractPhase instanceof QuestionnairePhase) {
            abstractPhaseDto = toDto((QuestionnairePhase) abstractPhase);
        } else {
            throw new RuntimeException("Unknown level type " + abstractPhase.getClass().getName());
        }

        return abstractPhaseDto;
    }

    List<AbstractPhaseDto> toDtoList(List<AbstractPhase> abstractPhase);

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    Task toEntity(TaskCreateDto taskCreateDto);

    Task toEntity(TaskUpdateDto taskUpdateDto);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseDto toDto(InfoPhase infoPhase);

    InfoPhase toEntity(InfoPhaseUpdateDto infoPhaseUpdateDto);

    @Mapping(target = "phaseType", constant = "TRAINING")
    TrainingPhaseDto toDto(TrainingPhase trainingPhase);

    TrainingPhase toEntity(TrainingPhaseDto trainingPhaseDto);

    TrainingPhase toEntity(TrainingPhaseUpdateDto trainingPhaseUpdateDto);

    DecisionMatrixRow toEntity(DecisionMatrixRowDto decisionMatrixRowDto);

    DecisionMatrixRowDto toDto(DecisionMatrixRow decisionMatrixRow);

    QuestionChoice toEntity(QuestionChoiceDto questionChoiceDto);

    QuestionChoiceDto toDto(QuestionChoice questionChoice);

    Question toEntity(QuestionDto questionDto);

    Question toEntity(QuestionRequiredIdDto questionDto);

    Question toEntity(QuestionUpdateDto questionUpdateDto);

    QuestionDto toDto(Question question);

    @Mapping(target = "questionPhaseRelations", source = "phaseRelations")
    QuestionnairePhase toEntity(QuestionnaireUpdateDto questionnaireUpdateDto);

    QuestionnairePhase toEntity(QuestionnairePhaseDto questionnairePhaseDto);

    @Mapping(target = "phaseRelations", source = "questionPhaseRelations")
    QuestionnairePhaseDto toDto(QuestionnairePhase questionnairePhase);

    QuestionPhaseRelation toEntity(QuestionPhaseRelationDto questionnairePhaseDto);

    QuestionPhaseRelationDto toDto(QuestionPhaseRelation questionnairePhase);
}
