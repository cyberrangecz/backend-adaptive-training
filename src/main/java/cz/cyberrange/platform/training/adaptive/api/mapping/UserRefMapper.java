package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.UserRefExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.responses.PageResultResource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
