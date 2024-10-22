package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.DecisionMatrixRow;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.training.DecisionMatrixRowArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.training.DecisionMatrixRowExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.training.DecisionMatrixRowImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.DecisionMatrixRowDTO;
import org.mapstruct.Mapper;
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
public interface DecisionMatrixMapper extends ParentMapper {
    // INFO PHASE
    DecisionMatrixRow mapToEntity(DecisionMatrixRowDTO dto);

    DecisionMatrixRow mapToEntity(DecisionMatrixRowImportDTO dto);

    DecisionMatrixRowDTO mapToDecisionMatrixRowDTO(DecisionMatrixRow entity);

    DecisionMatrixRowExportDTO mapToDecisionMatrixRowExportDTO(DecisionMatrixRow entity);

    DecisionMatrixRowArchiveDTO mapToDecisionMatrixRowArchiveDTO(DecisionMatrixRow entity);

    List<DecisionMatrixRow> mapToList(Collection<DecisionMatrixRowDTO> dtos);

    List<DecisionMatrixRowDTO> mapToListDTO(Collection<DecisionMatrixRow> entities);

    Set<DecisionMatrixRow> mapToSet(Collection<DecisionMatrixRowDTO> dtos);

    Set<DecisionMatrixRowDTO> mapToSetDTO(Collection<DecisionMatrixRow> entities);
}
