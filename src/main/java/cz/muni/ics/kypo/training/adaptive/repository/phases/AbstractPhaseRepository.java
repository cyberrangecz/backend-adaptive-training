package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbstractPhaseRepository extends JpaRepository<AbstractPhase, Long>, QuerydslPredicateExecutor<AbstractPhase> {

    List<AbstractPhase> findAllByTrainingDefinitionIdOrderByOrder(long trainingDefinitionId);

    @Query("SELECT COALESCE(MAX(l.order), -1) FROM AbstractPhase l WHERE l.trainingDefinition.id = :trainingDefinitionId")
    Integer getCurrentMaxOrder(@Param("trainingDefinitionId") Long trainingDefinitionId);

    @Modifying
    @Query("UPDATE AbstractPhase l SET l.order = l.order - 1 " +
            "WHERE l.trainingDefinition.id = :trainingDefinitionId " +
            "AND l.order > :order ")
    void decreaseOrderAfterPhaseWasDeleted(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                           @Param("order") int order);

    @Modifying
    @Query("UPDATE AbstractPhase l SET l.order = l.order + 1 " +
            "WHERE l.trainingDefinition.id = :trainingDefinitionId " +
            "AND l.order >= :lowerBound " +
            "AND l.order < :upperBound ")
    void increaseOrderOfPhasesOnInterval(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                         @Param("lowerBound") int lowerBound,
                                         @Param("upperBound") int upperBound);

    @Modifying
    @Query("UPDATE AbstractPhase l SET l.order = l.order - 1 " +
            "WHERE l.trainingDefinition.id = :trainingDefinitionId " +
            "AND l.order > :lowerBound " +
            "AND l.order <= :upperBound ")
    void decreaseOrderOfPhasesOnInterval(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                         @Param("lowerBound") int lowerBound,
                                         @Param("upperBound") int upperBound);

    @Query("SELECT ap FROM AbstractPhase ap WHERE ap.trainingDefinition.id = :trainingDefinitionId AND ap.id = :phaseId")
    Optional<AbstractPhase> findPhaseInDefinition(@Param("trainingDefinitionId") Long trainingDefinitionId,
                                                  @Param("phaseId") Long phaseId);

    @Query("SELECT ap FROM AbstractPhase ap " +
            "JOIN FETCH ap.trainingDefinition td " +
            "JOIN FETCH td.authors " +
            "WHERE ap.id = :phaseId")
    Optional<AbstractPhase> findByIdWithDefinition(@Param("phaseId") Long phaseId);

    @Query("SELECT ap FROM AbstractPhase ap WHERE ap.trainingDefinition.id = :trainingDefinitionId AND ap.order = 0")
    Optional<AbstractPhase> findFirstPhaseOfTrainingDefinition(@Param("trainingDefinitionId") Long trainingDefinitionId);
}
