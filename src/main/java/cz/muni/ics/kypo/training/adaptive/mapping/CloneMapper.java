package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * The TrainingDefinitionMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type TrainingDefinitionMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring",
        uses = {UserRefMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CloneMapper extends ParentMapper {

    @Mapping(target = "state", constant = "UNRELEASED")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", expression = "java(new java.util.HashSet<>())")
    TrainingDefinition clone(TrainingDefinition entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainingDefinition", ignore = true)
    InfoPhase clone(InfoPhase entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainingDefinition", ignore = true)
    TrainingPhase clone(TrainingPhase entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainingDefinition", ignore = true)
    QuestionnairePhase clone(QuestionnairePhase entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questionPhaseRelations", ignore = true)
    @Mapping(target = "questionnairePhase", ignore = true)
    @Mapping(target = "choices", ignore = true)
    Question clone(Question entity);

    List<Question> clone(List<Question> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    QuestionChoice clone(QuestionChoice entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainingPhase", ignore = true)
    Task clone(Task entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainingPhase", ignore = true)
    DecisionMatrixRow clone(DecisionMatrixRow entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "relatedTrainingPhase", ignore = true)
    @Mapping(target = "questionnairePhase", ignore = true)
    QuestionPhaseRelation clone(QuestionPhaseRelation entity);
}
