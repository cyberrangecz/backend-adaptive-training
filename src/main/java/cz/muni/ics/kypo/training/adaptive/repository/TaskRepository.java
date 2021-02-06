package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long> {

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


    @Transactional
    @Modifying
    @Query("UPDATE Task t SET t.order = t.order - 1 " +
            "WHERE t.trainingPhase.id = :trainingPhaseId " +
            "AND t.order > :order ")
    void decreaseOrderAfterTaskWasDeleted(@Param("trainingPhaseId") Long trainingPhaseId,
                                          @Param("order") int order);
}
