package cz.muni.ics.kypo.training.adaptive.mapping;

import cz.muni.ics.kypo.training.adaptive.domain.Submission;
import cz.muni.ics.kypo.training.adaptive.dto.SubmissionDTO;
import org.mapstruct.*;

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
