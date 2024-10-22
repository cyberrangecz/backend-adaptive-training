package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.ParticipantTaskAssignment;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.PreProcessLink;

import java.util.List;
import java.util.Map;

/**
 * The interface Training definition repository custom.
 */
public interface ParticipantTaskAssignmentRepositoryCustom {

    /**
     * Find all task transitions between two phases.
     *
     * @param trainingInstanceId training instance id
     * @return the page of training definitions
     */
    List<PreProcessLink> findTaskTransitionsBetweenTwoPhases(Long trainingDefinitionId, Long trainingInstanceId,
                                                             Long firstPhaseId, Long secondPhaseId);

    Map<Long, Long> findNumberOfParticipantsInTasksOfPhase(Long phaseId);

    Map<Long, List<ParticipantTaskAssignment>> findAllByTrainingInstanceAndGroupedByTrainingRun(Long instanceId);

    List<ParticipantTaskAssignment> findAllByTrainingRun(Long trainingRunId);

}
