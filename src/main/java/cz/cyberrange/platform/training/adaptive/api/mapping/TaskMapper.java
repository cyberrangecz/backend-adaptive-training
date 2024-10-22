package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.api.dto.archive.phases.training.TaskArchiveDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.export.phases.training.TaskExportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.training.TaskImportDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskCopyDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.preview.TaskPreviewDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.view.TaskViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * The HintMapper is an utility class to map items into data transfer objects. It provides the implementation of mappings between Java bean type HintMapper and
 * DTOs classes. Code is generated during compile time.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper extends ParentMapper {
    Task mapToEntity(TaskDTO dto);

    Task mapToEntity(TaskCopyDTO dto);

    Task mapToEntity(TaskUpdateDTO dto);

    Task mapToEntity(TaskImportDTO dto);

    TaskArchiveDTO mapToTaskArchiveDTO(Task entity);

    TaskExportDTO mapToTaskExportDTO(Task entity);

    TaskDTO mapToTaskDTO(Task entity);

    TaskViewDTO mapToTaskViewDTO(Task entity);

    TaskPreviewDTO mapToTaskPreviewDTO(Task entity);

    List<Task> mapToList(Collection<TaskDTO> dtos);

    List<TaskDTO> mapToListDTO(Collection<Task> entities);

    Set<Task> mapToSet(Collection<TaskDTO> dtos);

    Set<TaskDTO> mapToSetDTO(Collection<Task> entities);

}
