package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.dto.sankeydiagram.PreProcessLink;

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

}
