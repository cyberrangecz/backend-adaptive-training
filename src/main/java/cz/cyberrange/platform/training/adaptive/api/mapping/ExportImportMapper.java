package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.training.TrainingDefinitionArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.training.TrainingInstanceArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.training.TrainingRunArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.training.TrainingDefinitionExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.training.TrainingRunExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.ImportTrainingDefinitionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * The ExportImportMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type ExportImportMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring",
        uses = {UserRefMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExportImportMapper extends ParentMapper {

    TrainingDefinitionExportDTO mapToDTO(TrainingDefinition entity);

    TrainingDefinition mapToEntity(ImportTrainingDefinitionDTO dto);

    TrainingInstanceArchiveDTO mapToDTO(TrainingInstance entity);

    TrainingRunExportDTO mapToDTO(TrainingRun entity);

    TrainingDefinitionArchiveDTO mapToArchiveDTO(TrainingDefinition entity);

    TrainingRunArchiveDTO mapToArchiveDTO(TrainingRun entity);
}
