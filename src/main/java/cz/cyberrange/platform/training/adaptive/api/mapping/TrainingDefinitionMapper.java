package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.responses.PageResultResource;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionByIdDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionCreateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionInfoDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The TrainingDefinitionMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type TrainingDefinitionMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring",
        uses = {UserRefMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingDefinitionMapper extends ParentMapper {

    TrainingDefinition mapToEntity(TrainingDefinitionByIdDTO dto);

    TrainingDefinitionByIdDTO mapToDTOById(TrainingDefinition entity);

    TrainingDefinitionDTO mapToDTO(TrainingDefinition entity);

    TrainingDefinitionInfoDTO mapToInfoDTO(TrainingDefinition entity);

    TrainingDefinition mapCreateToEntity(TrainingDefinitionCreateDTO dto);

    TrainingDefinition mapUpdateToEntity(TrainingDefinitionUpdateDTO dto);

    List<TrainingDefinition> mapToList(Collection<TrainingDefinitionByIdDTO> dtos);

    List<TrainingDefinitionByIdDTO> mapToListDTO(Collection<TrainingDefinition> entities);

    Set<TrainingDefinition> mapToSet(Collection<TrainingDefinitionByIdDTO> dtos);

    Set<TrainingDefinitionByIdDTO> mapToSetDTO(Collection<TrainingDefinition> entities);

    default Optional<TrainingDefinition> mapToOptional(TrainingDefinitionByIdDTO dto) {
        return Optional.ofNullable(mapToEntity(dto));
    }

    default Optional<TrainingDefinitionByIdDTO> mapToOptional(TrainingDefinition entity) {
        return Optional.ofNullable(mapToDTOById(entity));
    }

    default Page<TrainingDefinitionByIdDTO> mapToPageDTO(Page<TrainingDefinition> objects) {
        List<TrainingDefinitionByIdDTO> mapped = mapToListDTO(objects.getContent());
        return new PageImpl<>(mapped, objects.getPageable(), mapped.size());
    }

    default Page<TrainingDefinition> mapToPage(Page<TrainingDefinitionByIdDTO> objects) {
        List<TrainingDefinition> mapped = mapToList(objects.getContent());
        return new PageImpl<>(mapped, objects.getPageable(), mapped.size());
    }

    default PageResultResource<TrainingDefinitionDTO> mapToPageResultResource(Page<TrainingDefinition> objects) {
        List<TrainingDefinitionDTO> mapped = new ArrayList<>();
        objects.forEach(object -> mapped.add(mapToDTO(object)));
        return new PageResultResource<>(mapped, createPagination(objects));
    }

    default PageResultResource<TrainingDefinitionInfoDTO> mapToPageResultResourceInfoDTO(Page<TrainingDefinition> objects) {
        List<TrainingDefinitionInfoDTO> mapped = new ArrayList<>();
        objects.forEach(object -> mapped.add(mapToInfoDTO(object)));
        return new PageResultResource<>(mapped, createPagination(objects));
    }
}
