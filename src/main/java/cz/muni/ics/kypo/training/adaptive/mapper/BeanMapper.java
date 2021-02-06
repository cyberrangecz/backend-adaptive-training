package cz.muni.ics.kypo.training.adaptive.mapper;

import cz.muni.ics.kypo.training.adaptive.domain.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.DecisionMatrixRow;
import cz.muni.ics.kypo.training.adaptive.domain.InfoPhase;
import cz.muni.ics.kypo.training.adaptive.domain.Question;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.Task;
import cz.muni.ics.kypo.training.adaptive.domain.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.DecisionMatrixRowDto;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.InfoPhaseUpdateDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionChoiceDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionPhaseRelationDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionRequiredIdDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionUpdateDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionnairePhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.QuestionnaireUpdateDto;
import cz.muni.ics.kypo.training.adaptive.dto.TaskCreateDto;
import cz.muni.ics.kypo.training.adaptive.dto.TaskDto;
import cz.muni.ics.kypo.training.adaptive.dto.TaskUpdateDto;
import cz.muni.ics.kypo.training.adaptive.dto.TrainingPhaseDto;
import cz.muni.ics.kypo.training.adaptive.dto.TrainingPhaseUpdateDto;
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

    @Mapping(target = "phaseId", source = "relatedTrainingPhase")
    @Mapping(target = "questionIds", source = "questions")
    QuestionPhaseRelationDto toDto(QuestionPhaseRelation questionnairePhase);

    default Long mapTrainingPhaseId(TrainingPhase trainingPhase) {
        if (trainingPhase == null) {
            return null;
        } else {
            return trainingPhase.getId();
        }
    }

    default Long mapQuestionId(Question question) {
        if (question == null) {
            return null;
        } else {
            return question.getId();
        }
    }
}
