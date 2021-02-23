package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.*;
import cz.muni.ics.kypo.training.adaptive.dto.training.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface BeanMapper {

    BeanMapper INSTANCE = Mappers.getMapper(BeanMapper.class);

    default AbstractPhaseDTO toDto(AbstractPhase abstractPhase) {
        AbstractPhaseDTO abstractPhaseDto;
        if (abstractPhase instanceof TrainingPhase) {
            abstractPhaseDto = toDto((TrainingPhase) abstractPhase);
        } else if (abstractPhase instanceof InfoPhase) {
            abstractPhaseDto = toDto((InfoPhase) abstractPhase);
        } else if (abstractPhase instanceof QuestionnairePhase) {
            abstractPhaseDto = toDto((QuestionnairePhase) abstractPhase);
        } else {
            throw new RuntimeException("Unknown phase type " + abstractPhase.getClass().getName());
        }

        return abstractPhaseDto;
    }

    List<AbstractPhaseDTO> toDtoList(List<AbstractPhase> abstractPhase);

    TaskDTO toDto(Task task);

    Task toEntity(TaskDTO taskDto);

    Task toEntity(TaskUpdateDTO taskUpdateDto);

    Task toEntity(TaskCopyDTO taskCopyDTO);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseDTO toDto(InfoPhase infoPhase);

    InfoPhase toEntity(InfoPhaseUpdateDTO infoPhaseUpdateDto);

    @Mapping(target = "phaseType", constant = "TRAINING")
    TrainingPhaseDTO toDto(TrainingPhase trainingPhase);

    TrainingPhase toEntity(TrainingPhaseDTO trainingPhaseDto);

    TrainingPhase toEntity(TrainingPhaseUpdateDTO trainingPhaseUpdateDto);

    DecisionMatrixRow toEntity(DecisionMatrixRowDTO decisionMatrixRowDto);

    DecisionMatrixRowDTO toDto(DecisionMatrixRow decisionMatrixRow);

    QuestionChoice toEntity(QuestionChoiceDTO questionChoiceDto);

    QuestionChoiceDTO toDto(QuestionChoice questionChoice);

    Question toEntity(QuestionDTO questionDto);

    QuestionDTO toDto(Question question);

    @Mapping(target = "questionPhaseRelations", source = "phaseRelations")
    QuestionnairePhase toEntity(QuestionnaireUpdateDTO questionnaireUpdateDto);

    QuestionnairePhase toEntity(QuestionnairePhaseDTO questionnairePhaseDto);

    @Mapping(target = "phaseRelations", source = "questionPhaseRelations")
    @Mapping(target = "phaseType", constant = "QUESTIONNAIRE")
    QuestionnairePhaseDTO toDto(QuestionnairePhase questionnairePhase);

    QuestionPhaseRelation toEntity(QuestionPhaseRelationDTO questionnairePhaseDto);

    @Mapping(target = "phaseId", source = "relatedTrainingPhase")
    @Mapping(target = "questionIds", source = "questions")
    QuestionPhaseRelationDTO toDto(QuestionPhaseRelation questionnairePhase);

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
