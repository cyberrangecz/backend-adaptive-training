package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionPhaseRelationArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire.QuestionPhaseRelationExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire.QuestionPhaseRelationImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionPhaseRelationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The DecisionMatrixMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type DecisionMatrixRow and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionPhaseRelationMapper extends ParentMapper {
    // INFO PHASE
    QuestionPhaseRelation mapToEntity(QuestionPhaseRelationImportDTO dto);

    QuestionPhaseRelation mapToEntity(QuestionPhaseRelationDTO dto);

    @Mapping(target = "phaseId", source = "relatedTrainingPhase")
    @Mapping(target = "questionIds", source = "questions")
    QuestionPhaseRelationDTO mapToQuestionPhaseRelationDTO(QuestionPhaseRelation entity);

    @Mapping(target = "phaseId", source = "relatedTrainingPhase")
    @Mapping(target = "questionIds", source = "questions")
    QuestionPhaseRelationExportDTO mapToQuestionPhaseRelationExportDTO(QuestionPhaseRelation entity);

    @Mapping(target = "phaseId", source = "relatedTrainingPhase")
    @Mapping(target = "questionIds", source = "questions")
    QuestionPhaseRelationArchiveDTO mapToQuestionPhaseRelationArchiveDTO(QuestionPhaseRelation entity);

    List<QuestionPhaseRelation> mapToList(Collection<QuestionPhaseRelationDTO> dtos);

    List<QuestionPhaseRelationDTO> mapToListDTO(Collection<QuestionPhaseRelation> entities);

    Set<QuestionPhaseRelation> mapToSet(Collection<QuestionPhaseRelationDTO> dtos);

    Set<QuestionPhaseRelationDTO> mapToSetDTO(Collection<QuestionPhaseRelation> entities);

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
