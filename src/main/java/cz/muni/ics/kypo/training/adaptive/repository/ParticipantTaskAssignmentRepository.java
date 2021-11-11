package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.NodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantTaskAssignmentRepository extends
        JpaRepository<ParticipantTaskAssignment, Long>, ParticipantTaskAssignmentRepositoryCustom, QuerydslPredicateExecutor<ParticipantTaskAssignment> {

    @Query("SELECT DISTINCT new cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.NodeDTO(t.id, t.order, t.title, ap.id, ap.order, ap.title) FROM ParticipantTaskAssignment p " +
            "JOIN p.task t " +
            "JOIN p.abstractPhase ap " +
            "JOIN p.trainingRun tr " +
            "JOIN tr.trainingInstance ti " +
            "WHERE ti.id = :trainingInstanceId " +
            "ORDER BY ap.order, t.order ASC")
    List<NodeDTO> findAllVisitedTasks(@Param("trainingInstanceId") Long trainingInstanceId);

    List<ParticipantTaskAssignment> findAllByTrainingRunTrainingInstanceId(Long trainingInstanceId);

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);
}
