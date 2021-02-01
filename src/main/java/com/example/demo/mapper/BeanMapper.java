package com.example.demo.mapper;

import com.example.demo.domain.AbstractPhase;
import com.example.demo.domain.DecisionMatrixRow;
import com.example.demo.domain.InfoPhase;
import com.example.demo.domain.TrainingPhase;
import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionChoice;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.domain.Task;
import com.example.demo.dto.AbstractPhaseDto;
import com.example.demo.dto.DecisionMatrixRowDto;
import com.example.demo.dto.InfoPhaseDto;
import com.example.demo.dto.InfoPhaseUpdateDto;
import com.example.demo.dto.TrainingPhaseDto;
import com.example.demo.dto.TrainingPhaseUpdateDto;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.dto.QuestionChoiceUpdateDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionUpdateDto;
import com.example.demo.dto.QuestionnaireLevelDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.TaskUpdateDto;
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
        } else if (abstractPhase instanceof Task) {
            abstractPhaseDto = toDto((Task) abstractPhase);
        } else if (abstractPhase instanceof QuestionnaireLevel) {
            abstractPhaseDto = toDto((QuestionnaireLevel) abstractPhase);
        } else {
            throw new RuntimeException("Unknown level type " + abstractPhase.getClass().getName());
        }

        return abstractPhaseDto;
    }

    List<AbstractPhaseDto> toDtoList(List<AbstractPhase> abstractPhase);

    @Mapping(target = "phaseType", constant = "task")
    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    Task toEntity(TaskCreateDto taskCreateDto);

    Task toEntity(TaskUpdateDto taskUpdateDto);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseDto toDto(InfoPhase infoPhase);

    InfoPhase toEntity(InfoPhaseUpdateDto infoPhaseUpdateDto);

    @Mapping(target = "phaseType", constant = "GAME")
    TrainingPhaseDto toDto(TrainingPhase trainingPhase);

    TrainingPhase toEntity(TrainingPhaseDto trainingPhaseDto);

    TrainingPhase toEntity(TrainingPhaseUpdateDto trainingPhaseUpdateDto);

    DecisionMatrixRow toEntity(DecisionMatrixRowDto decisionMatrixRowDto);

    DecisionMatrixRowDto toDto(DecisionMatrixRow decisionMatrixRow);

    QuestionChoice toEntity(QuestionChoiceDto questionChoiceDto);

    QuestionChoice toEntity(QuestionChoiceUpdateDto questionChoiceDto);

    QuestionChoiceDto toDto(QuestionChoice questionChoice);

    Question toEntity(QuestionDto questionDto);

    Question toEntity(QuestionUpdateDto questionUpdateDto);

    QuestionDto toDto(Question question);

    QuestionnaireLevel toEntity(QuestionnaireUpdateDto questionnaireUpdateDto);

    QuestionnaireLevel toEntity(QuestionnaireLevelDto questionnaireLevelDto);

    QuestionnaireLevelDto toDto(QuestionnaireLevel questionnaireLevel);
}
