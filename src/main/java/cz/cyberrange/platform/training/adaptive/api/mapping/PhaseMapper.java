package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AccessPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.ExpectedCommand;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.InfoPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.BasicPhaseInfoDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.access.AccessPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.access.AccessPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.access.AccessPhaseViewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.AbstractPhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.access.AccessPhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.info.InfoPhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.questionnaire.QuestionnairePhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.training.TrainingPhaseArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.AbstractPhaseExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.access.AccessPhaseExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.info.InfoPhaseExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.questionnaire.QuestionnairePhaseExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.training.TrainingPhaseExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.access.AccessPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.info.InfoPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.questionnaire.QuestionnairePhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.training.TrainingPhaseImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.info.InfoPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.info.InfoPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnairePhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.preview.QuestionnairePhasePreviewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.view.QuestionnairePhaseViewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.preview.TrainingPhasePreviewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.view.TrainingPhaseViewDTO;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.InternalServerErrorException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
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

    @Mapping(target = "phaseType", constant = "QUESTIONNAIRE")
    QuestionnairePhasePreviewDTO mapToQuestionnairePhasePreviewDTO(QuestionnairePhase entity);

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
    TrainingPhasePreviewDTO mapToTrainingPhasePreviewDTO(TrainingPhase entity, @Context Task taskEntity);

    @AfterMapping
    default void setTaskPreviewDTO(@MappingTarget TrainingPhasePreviewDTO target, @Context Task taskEntity) {
        target.setTask(TASK_MAPPER.mapToTaskPreviewDTO(taskEntity));
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
