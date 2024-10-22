package cz.cyberrange.platform.training.adaptive.api.mapping;

import cz.cyberrange.platform.training.adaptive.persistence.entity.Submission;
import cz.cyberrange.platform.training.adaptive.api.dto.SubmissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubmissionMapper extends ParentMapper {

    List<SubmissionDTO> mapToSubmissionListDTO(Collection<Submission> entities);

    @Mapping(source = "trainingRun.id", target = "trainingRunId")
    @Mapping(source = "phase.id", target = "phaseId")
    @Mapping(source = "type", target = "submissionType")
    SubmissionDTO mapToDTO(Submission entity);
}
