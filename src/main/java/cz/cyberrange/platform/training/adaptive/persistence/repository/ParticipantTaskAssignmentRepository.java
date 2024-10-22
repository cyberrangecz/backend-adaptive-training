package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.ParticipantTaskAssignment;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.NodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantTaskAssignmentRepository extends
        JpaRepository<ParticipantTaskAssignment, Long>, ParticipantTaskAssignmentRepositoryCustom, QuerydslPredicateExecutor<ParticipantTaskAssignment> {

    @Query("SELECT DISTINCT new cz.cyberrange.platform.training.adaptive.dto.visualizations.sankey.NodeDTO(t.id, t.order, t.title, ap.id, ap.order, ap.title) FROM ParticipantTaskAssignment p " +
            "JOIN p.task t " +
            "JOIN p.abstractPhase ap " +
            "JOIN p.trainingRun tr " +
            "JOIN tr.trainingInstance ti " +
            "WHERE ti.id = :trainingInstanceId " +
            "ORDER BY ap.order, t.order ASC")
    List<NodeDTO> findAllVisitedTasks(@Param("trainingInstanceId") Long trainingInstanceId);

    List<ParticipantTaskAssignment> findAllByTrainingRunTrainingInstanceId(Long trainingInstanceId);

    Optional<ParticipantTaskAssignment> findByAbstractPhaseIdAndTrainingRunId(Long trainingPhaseId, Long trainingRunId);

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);
}
