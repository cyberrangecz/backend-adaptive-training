package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.phase.*;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.access.AccessPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.access.AccessPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.access.AccessPhaseViewDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.access.AccessPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.info.InfoPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.questionnaire.QuestionnairePhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.phases.training.TrainingPhaseArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.AbstractPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.access.AccessPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.info.InfoPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.questionnaire.QuestionnairePhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.phases.training.TrainingPhaseExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.access.AccessPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.info.InfoPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.questionnaire.QuestionnairePhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.phases.training.TrainingPhaseImportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.view.QuestionnairePhaseViewDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.view.TrainingPhaseViewDTO;
import cz.muni.ics.kypo.training.adaptive.exceptions.InternalServerErrorException;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * The PhaseMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type Phase and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring", uses = {
        QuestionMapper.class,
        TaskMapper.class,
        DecisionMatrixMapper.class,
        QuestionPhaseRelationMapper.class,
        MitreTechniqueMapper.class
},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhaseMapper extends ParentMapper {
    TaskMapper TASK_MAPPER = Mappers.getMapper(TaskMapper.class);

    // INFO PHASE
    InfoPhase mapToEntity(InfoPhaseDTO dto);

    InfoPhase mapToEntity(InfoPhaseUpdateDTO dto);

    InfoPhase mapToEntity(InfoPhaseImportDTO dto);

    BasicPhaseInfoDTO mapToBasicPhaseInfoDTO(InfoPhase entity);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseDTO mapToInfoPhaseDTO(InfoPhase entity);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseExportDTO mapToInfoPhaseExportDTO(InfoPhase entity);

    @Mapping(target = "phaseType", constant = "INFO")
    InfoPhaseArchiveDTO mapToInfoPhaseArchiveDTO(InfoPhase entity);

    // QUESTIONNAIRE PHASE
    QuestionnairePhase mapToEntity(QuestionnairePhaseDTO dto);

    QuestionnairePhase mapToEntity(QuestionnairePhaseImportDTO dto);

    QuestionnairePhase mapToEntity(QuestionnaireUpdateDTO dto);

    BasicPhaseInfoDTO mapToBasicPhaseInfoDTO(QuestionnairePhase entity);

    @Mapping(target = "phaseRelations", source = "questionPhaseRelations")
    @Mapping(target = "phaseType", constant = "QUESTIONNAIRE")
    QuestionnairePhaseDTO mapToQuestionnairePhaseDTO(QuestionnairePhase entity);

    @Mapping(target = "phaseType", constant = "QUESTIONNAIRE")
    QuestionnairePhaseViewDTO mapToQuestionnairePhaseViewDTO(QuestionnairePhase entity);

    @Mapping(target = "phaseRelations", source = "questionPhaseRelations")
    @Mapping(target = "phaseType", constant = "QUESTIONNAIRE")
    QuestionnairePhaseArchiveDTO mapToQuestionnairePhaseArchiveDTO(QuestionnairePhase entity);

    @Mapping(target = "phaseRelations", source = "questionPhaseRelations")
    @Mapping(target = "phaseType", constant = "QUESTIONNAIRE")
    QuestionnairePhaseExportDTO mapToQuestionnairePhaseExportDTO(QuestionnairePhase entity);

    // TRAINING PHASE
    TrainingPhase mapToEntity(TrainingPhaseDTO dto);

    TrainingPhase mapToEntity(TrainingPhaseUpdateDTO dto);

    TrainingPhase mapToEntity(TrainingPhaseImportDTO dto);

    @Mapping(target = "phaseType", constant = "TRAINING")
    BasicPhaseInfoDTO mapToBasicPhaseInfoDTO(TrainingPhase entity);

    @Mapping(target = "phaseType", constant = "TRAINING")
    TrainingPhaseDTO mapToTrainingPhaseDTO(TrainingPhase entity);

    @Mapping(target = "phaseType", constant = "TRAINING")
    TrainingPhaseViewDTO mapToTrainingPhaseViewDTO(TrainingPhase entity, @Context Task taskEntity);

    @AfterMapping
    default void setTaskViewDTO(@MappingTarget TrainingPhaseViewDTO target, @Context Task taskEntity) {
        target.setTask(TASK_MAPPER.mapToTaskViewDTO(taskEntity));
    }

    @Mapping(target = "phaseType", constant = "TRAINING")
    @Mapping(source = "mitreTechniques", target = "mitreTechniques", qualifiedByName = "ignoreIds")
    TrainingPhaseExportDTO mapToTrainingPhaseExportDTO(TrainingPhase entity);

    @Mapping(target = "phaseType", constant = "TRAINING")
    @Mapping(source = "mitreTechniques", target = "mitreTechniques", qualifiedByName = "ignoreIds")
    TrainingPhaseArchiveDTO mapToTrainingPhaseArchiveDTO(TrainingPhase entity);

    default String mapExpectedCommandToString(ExpectedCommand entity) {
        return entity.getCommand();
    }

    default ExpectedCommand mapStringToExpectedCommand(String command) {
        ExpectedCommand expectedCommand = new ExpectedCommand();
        expectedCommand.setCommand(command);
        return expectedCommand;
    }

    // ACCESS PHASE
    AccessPhase mapToEntity(AccessPhaseDTO dto);

    AccessPhase mapToEntity(AccessPhaseUpdateDTO dto);

    AccessPhase mapToEntity(AccessPhaseImportDTO dto);

    @Mapping(target = "phaseType", constant = "ACCESS")
    BasicPhaseInfoDTO mapToBasicPhaseInfoDTO(AccessPhase entity);

    @Mapping(target = "phaseType", constant = "ACCESS")
    AccessPhaseDTO mapToAccessPhaseDTO(AccessPhase entity);

    @Mapping(target = "phaseType", constant = "ACCESS")
    AccessPhaseViewDTO mapToAccessPhaseViewDTO(AccessPhase entity);

    @Mapping(target = "phaseType", constant = "ACCESS")
    AccessPhaseExportDTO mapToAccessPhaseExportDTO(AccessPhase entity);

    @Mapping(target = "phaseType", constant = "ACCESS")
    AccessPhaseArchiveDTO mapToAccessPhaseArchiveDTO(AccessPhase entity);

    // ABSTRACT
    List<AbstractPhaseDTO> mapToListDTO(Collection<AbstractPhase> entities);

    default AbstractPhaseDTO mapToDTO(AbstractPhase entity) {
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            AbstractPhaseDTO abstractPhaseDTO;
        if (entity instanceof TrainingPhase) {
            abstractPhaseDTO = mapToTrainingPhaseDTO((TrainingPhase) entity);
        } else if (entity instanceof InfoPhase) {
            abstractPhaseDTO = mapToInfoPhaseDTO((InfoPhase) entity);
        } else if (entity instanceof QuestionnairePhase) {
            abstractPhaseDTO = mapToQuestionnairePhaseDTO((QuestionnairePhase) entity);
        } else if (entity instanceof AccessPhase) {
            abstractPhaseDTO = mapToAccessPhaseDTO((AccessPhase) entity);
        } else {
            throw new InternalServerErrorException("Phase with id: " + entity.getId() + " in given training definition with id: " + entity.getTrainingDefinition().getId() +
                    " is not instance of questionnaire, training, access or info phase.");
        }
        return abstractPhaseDTO;
    }

    default AbstractPhaseArchiveDTO mapToArchiveDTO(AbstractPhase entity) {
        AbstractPhaseArchiveDTO abstractPhaseArchiveDTO;
        if (entity instanceof TrainingPhase) {
            abstractPhaseArchiveDTO = mapToTrainingPhaseArchiveDTO((TrainingPhase) entity);
        } else if (entity instanceof InfoPhase) {
            abstractPhaseArchiveDTO = mapToInfoPhaseArchiveDTO((InfoPhase) entity);
        } else if (entity instanceof QuestionnairePhase) {
            abstractPhaseArchiveDTO = mapToQuestionnairePhaseArchiveDTO((QuestionnairePhase) entity);
        } else if (entity instanceof AccessPhase) {
            abstractPhaseArchiveDTO = mapToAccessPhaseArchiveDTO((AccessPhase) entity);
        } else {
            throw new InternalServerErrorException("Phase with id: " + entity.getId() + " is not instance of questionnaire, training, access or info phase.");
        }
        return abstractPhaseArchiveDTO;
    }

    default AbstractPhaseExportDTO mapToExportDTO(AbstractPhase entity) {
        AbstractPhaseExportDTO abstractPhaseExportDTO;
        if (entity instanceof TrainingPhase) {
            abstractPhaseExportDTO = mapToTrainingPhaseExportDTO((TrainingPhase) entity);
        } else if (entity instanceof InfoPhase) {
            abstractPhaseExportDTO = mapToInfoPhaseExportDTO((InfoPhase) entity);
        } else if (entity instanceof QuestionnairePhase) {
            abstractPhaseExportDTO = mapToQuestionnairePhaseExportDTO((QuestionnairePhase) entity);
        } else if (entity instanceof AccessPhase) {
            abstractPhaseExportDTO = mapToAccessPhaseExportDTO((AccessPhase) entity);
        } else {
            throw new InternalServerErrorException("Phase with id: " + entity.getId() + " is not instance of questionnaire, training, access, or info phase.");
        }
        return abstractPhaseExportDTO;
    }
}
