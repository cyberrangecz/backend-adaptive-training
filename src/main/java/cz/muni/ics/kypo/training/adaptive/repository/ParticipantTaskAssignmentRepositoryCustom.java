package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.LinkDTO;

import java.util.List;
import java.util.Set;

/**
 * The interface Training definition repository custom.
 */
public interface ParticipantTaskAssignmentRepositoryCustom {

    /**
     * Find all training definitions.
     *
     * @param trainingInstanceId training instance id
     * @return the page of training definitions
     */
    Set<LinkDTO> findAllTaskTransitions(Long trainingDefinitionId, Long trainingInstanceId);

}
