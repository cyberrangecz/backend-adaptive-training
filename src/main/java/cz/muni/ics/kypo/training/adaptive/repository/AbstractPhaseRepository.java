package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.AbstractPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AbstractPhaseRepository extends JpaRepository<AbstractPhase, Long> {

    List<AbstractPhase> findAllByTrainingDefinitionIdOrderByOrder(long trainingDefinitionId);

    @Query("SELECT COALESCE(MAX(l.order), -1) FROM AbstractPhase l WHERE l.trainingDefinitionId = :trainingDefinitionId")
    Integer getCurrentMaxOrder(@Param("trainingDefinitionId") Long trainingDefinitionId);

    @Modifying
    @Query("UPDATE AbstractPhase l SET l.order = l.order - 1 " +
            "WHERE l.trainingDefinitionId = :trainingDefinitionId " +
            "AND l.order > :order ")
    void decreaseOrderAfterLevelWasDeleted(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                           @Param("order") int order);

    @Modifying
    @Query("UPDATE AbstractPhase l SET l.order = l.order + 1 " +
            "WHERE l.trainingDefinitionId = :trainingDefinitionId " +
            "AND l.order >= :lowerBound " +
            "AND l.order < :upperBound ")
    void increaseOrderOfLevelsOnInterval(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                         @Param("lowerBound") int lowerBound,
                                         @Param("upperBound") int upperBound);

    @Modifying
    @Query("UPDATE AbstractPhase l SET l.order = l.order - 1 " +
            "WHERE l.trainingDefinitionId = :trainingDefinitionId " +
            "AND l.order > :lowerBound " +
            "AND l.order <= :upperBound ")
    void decreaseOrderOfLevelsOnInterval(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                         @Param("lowerBound") int lowerBound,
                                         @Param("upperBound") int upperBound);

}
