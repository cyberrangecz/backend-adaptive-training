package com.example.demo.mapper;

import com.example.demo.domain.Attachment;
import com.example.demo.domain.BaseLevel;
import com.example.demo.domain.DecisionMatrixRow;
import com.example.demo.domain.InfoPhase;
import com.example.demo.domain.PhaseLevel;
import com.example.demo.domain.Question;
import com.example.demo.domain.QuestionChoice;
import com.example.demo.domain.QuestionnaireLevel;
import com.example.demo.domain.Task;
import com.example.demo.dto.AttachmentDto;
import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.DecisionMatrixRowDto;
import com.example.demo.dto.InfoPhaseDto;
import com.example.demo.dto.InfoPhaseUpdateDto;
import com.example.demo.dto.PhaseLevelDto;
import com.example.demo.dto.PhaseLevelUpdateDto;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.dto.QuestionChoiceUpdateDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionUpdateDto;
import com.example.demo.dto.QuestionnaireLevelDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface BeanMapper {

    BeanMapper INSTANCE = Mappers.getMapper(BeanMapper.class);

    default BaseLevelDto toDto(BaseLevel baseLevel) {
        BaseLevelDto baseLevelDto;
        if (baseLevel instanceof PhaseLevel) {
            baseLevelDto = toDto((PhaseLevel) baseLevel);
        } else if (baseLevel instanceof InfoPhase) {
            baseLevelDto = toDto((InfoPhase) baseLevel);
        } else if (baseLevel instanceof Task) {
            baseLevelDto = toDto((Task) baseLevel);
        } else if (baseLevel instanceof QuestionnaireLevel) {
            baseLevelDto = toDto((QuestionnaireLevel) baseLevel);
        } else {
            throw new RuntimeException("Unknown level type " + baseLevel.getClass().getName());
        }

        return baseLevelDto;
    }

    List<BaseLevelDto> toDtoList(List<BaseLevel> baseLevel);

    @Mapping(target = "phaseType", constant = "task")
    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);

    Task toEntity(TaskCreateDto taskCreateDto);

    Task toEntity(TaskUpdateDto taskUpdateDto);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseDto toDto(InfoPhase infoPhase);

    InfoPhase toEntity(InfoPhaseUpdateDto infoPhaseUpdateDto);

    AttachmentDto toDto(Attachment attachment);

    Attachment toEntity(AttachmentDto attachment);

    @Mapping(target = "phaseType", constant = "GAME")
    PhaseLevelDto toDto(PhaseLevel phaseLevel);

    PhaseLevel toEntity(PhaseLevelDto phaseLevel);

    PhaseLevel toEntity(PhaseLevelUpdateDto phaseLevelUpdateDto);

    Task toGameLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    PhaseLevel toPhaseLevel(GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "phaseLevel", ignore = true)
//    @Mapping(target = "attachments", ignore = true)
        // TODO not really sure about this
    Task updateTask(@MappingTarget Task task, GameDefinitionCreateDto gameDefinitionCreateDto);

    @Mapping(target = "subLevels", ignore = true)
    PhaseLevel updatePhaseLevel(@MappingTarget PhaseLevel phaseLevel, GameDefinitionCreateDto gameDefinitionCreateDto);

//    @Mapping(target = "type", constant = "task")
    GameDefinitionCreateDto toLevelDefinitionDto(Task task);

//    @Mapping(target = "type", constant = "info")
    GameDefinitionCreateDto toLevelDefinitionDto(InfoPhase infoPhase);

//    @Mapping(target = "type", constant = "phase")
    GameDefinitionCreateDto toLevelDefinitionDto(PhaseLevel phaseLevel);

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
