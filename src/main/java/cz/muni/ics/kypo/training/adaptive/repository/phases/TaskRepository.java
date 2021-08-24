package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import cz.muni.ics.kypo.training.adaptive.dto.sankeydiagram.NodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, QuerydslPredicateExecutor<Task> {

    @Query("SELECT COALESCE(MAX(g.order), -1) FROM Task g WHERE g.trainingPhase.id = :phaseId")
    Integer getCurrentMaxOrder(@Param("phaseId") Long phaseId);

    @Modifying
    @Query("UPDATE Task t SET t.order = t.order - 1 " +
            "WHERE t.trainingPhase.id = :trainingPhaseId " +
            "AND t.order > :lowerBound " +
            "AND t.order <= :upperBound ")
    void decreaseOrderOfTasksOnInterval(@Param("trainingPhaseId") Long trainingPhaseId,
                                        @Param("lowerBound") int lowerBound,
                                        @Param("upperBound") int upperBound);


    @Modifying
    @Query("UPDATE Task t SET t.order = t.order + 1 " +
            "WHERE t.trainingPhase.id = :trainingPhaseId " +
            "AND t.order >= :lowerBound " +
            "AND t.order < :upperBound ")
    void increaseOrderOfTasksOnInterval(@Param("trainingPhaseId") Long trainingPhaseId,
                                        @Param("lowerBound") int lowerBound,
                                        @Param("upperBound") int upperBound);

    @Modifying
    @Query("UPDATE Task t SET t.order = t.order - 1 " +
            "WHERE t.trainingPhase.id = :trainingPhaseId " +
            "AND t.order > :order ")
    void decreaseOrderAfterTaskWasDeleted(@Param("trainingPhaseId") Long trainingPhaseId,
                                          @Param("order") int order);

    @Query("SELECT DISTINCT new cz.muni.ics.kypo.training.adaptive.dto.sankeydiagram.NodeDTO(t.id, t.order, t.title, tp.id, tp.order, tp.title) FROM Task t " +
            "JOIN t.trainingPhase tp " +
            "JOIN tp.trainingDefinition td " +
            "WHERE td.id = :trainingDefinitionId " +
            "ORDER BY tp.order, t.order ASC")
    List<NodeDTO> findAllTasksByTrainingDefinitionId(@Param("trainingDefinitionId") Long trainingDefinitionId);

    List<Task> findAllByIdIsInAndTrainingPhaseId(List<Long> taskIds, Long phaseId);
}
