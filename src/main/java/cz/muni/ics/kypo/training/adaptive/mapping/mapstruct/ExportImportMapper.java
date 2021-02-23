package cz.muni.ics.kypo.training.adaptive.mapping.mapstruct;

import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingDefinitionArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingInstanceArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.archive.training.TrainingRunArchiveDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.training.TrainingDefinitionWithPhasesExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.training.TrainingRunExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.imports.ImportTrainingDefinitionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * The ExportImportMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type ExportImportMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring",
        uses = {UserRefMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExportImportMapper extends ParentMapper {

    TrainingDefinitionWithPhasesExportDTO mapToDTO(TrainingDefinition entity);

    TrainingDefinition mapToEntity(ImportTrainingDefinitionDTO dto);

    TrainingInstanceArchiveDTO mapToDTO(TrainingInstance entity);

    TrainingRunExportDTO mapToDTO(TrainingRun entity);

    TrainingDefinitionArchiveDTO mapToArchiveDTO(TrainingDefinition entity);

    TrainingRunArchiveDTO mapToArchiveDTO(TrainingRun entity);
}
