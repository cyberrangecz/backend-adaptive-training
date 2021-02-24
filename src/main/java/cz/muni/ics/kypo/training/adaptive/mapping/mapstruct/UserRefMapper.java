package cz.muni.ics.kypo.training.adaptive.mapping.mapstruct;

import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.export.UserRefExportDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

/**
 * The UserRefMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type UserRefMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring", uses = {TrainingInstanceMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRefMapper extends ParentMapper {

    User mapToEntity(UserRefDTO dto);

    UserRefDTO mapToDTO(User entity);

    List<User> mapToList(Collection<UserRefDTO> dtos);

    List<UserRefDTO> mapToListDTO(Collection<User> entities);

    Set<User> mapToSet(Collection<UserRefDTO> dtos);

    Set<UserRefDTO> mapToSetDTO(Collection<User> entities);

    List<UserRefExportDTO> mapUserRefExportDTOToUserRefDTO(Collection<UserRefDTO> userRefDTOS);

    default Optional<User> mapToOptional(UserRefDTO dto) {
        return Optional.ofNullable(mapToEntity(dto));
    }

    default Optional<UserRefDTO> mapToOptional(User entity) {
        return Optional.ofNullable(mapToDTO(entity));
    }

    default Page<UserRefDTO> mapToPageDTO(Page<User> objects) {
        List<UserRefDTO> mapped = mapToListDTO(objects.getContent());
        return new PageImpl<>(mapped, objects.getPageable(), mapped.size());
    }

    default Page<User> mapToPage(Page<UserRefDTO> objects) {
        List<User> mapped = mapToList(objects.getContent());
        return new PageImpl<>(mapped, objects.getPageable(), mapped.size());
    }

    default PageResultResource<UserRefDTO> mapToPageResultResource(Page<User> objects) {
        List<UserRefDTO> mapped = new ArrayList<>();
        objects.forEach(object -> mapped.add(mapToDTO(object)));
        return new PageResultResource<>(mapped, createPagination(objects));
    }
}
