package cz.muni.ics.kypo.training.adaptive.mapping.mapstruct;

import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessedTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

/**
 * The TrainingRunMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type TrainingRunMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingRunMapper extends ParentMapper {
    TrainingRun mapToEntity(TrainingRunDTO dto);

    TrainingRunDTO mapToDTO(TrainingRun entity);

    TrainingRunByIdDTO mapToFindByIdDTO(TrainingRun entity);

    List<TrainingRun> mapToList(Collection<TrainingRunDTO> dtos);

    List<TrainingRunDTO> mapToListDTO(Collection<TrainingRun> entities);

    Set<TrainingRun> mapToSet(Collection<TrainingRunDTO> dtos);

    Set<TrainingRunDTO> mapToSetDTO(Collection<TrainingRun> entities);

    default Optional<TrainingRun> mapToOptional(TrainingRunDTO dto) {
        return Optional.ofNullable(mapToEntity(dto));
    }

    default Optional<TrainingRunDTO> mapToOptional(TrainingRun entity) {
        return Optional.ofNullable(mapToDTO(entity));
    }

    default Page<TrainingRunDTO> mapToPageDTO(Page<TrainingRun> objects) {
        List<TrainingRunDTO> mapped = mapToListDTO(objects.getContent());
        return new PageImpl<>(mapped, objects.getPageable(), mapped.size());
    }

    default Page<TrainingRun> mapToPage(Page<TrainingRunDTO> objects) {
        List<TrainingRun> mapped = mapToList(objects.getContent());
        return new PageImpl<>(mapped, objects.getPageable(), mapped.size());
    }

    default PageResultResource<TrainingRunDTO> mapToPageResultResource(Page<TrainingRun> objects) {
        List<TrainingRunDTO> mapped = new ArrayList<>();
        objects.forEach(object -> mapped.add(mapToDTO(object)));
        return new PageResultResource<>(mapped, createPagination(objects));
    }

    default PageResultResource<AccessedTrainingRunDTO> mapToPageResultResourceAccessed(Page<AccessedTrainingRunDTO> objects) {
        List<AccessedTrainingRunDTO> mapped = new ArrayList<>();
        objects.forEach(mapped::add);
        return new PageResultResource<>(mapped, createPagination(objects));
    }
}
