package cz.cyberrange.platform.training.adaptive.persistence.repository.phases;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPhaseRepository extends JpaRepository<TrainingPhase, Long>, QuerydslPredicateExecutor<TrainingPhase> {

    @Query("SELECT COUNT(p.id) FROM TrainingPhase p WHERE p.trainingDefinition.id = :trainingDefinitionId")
    int getNumberOfExistingPhases(@Param("trainingDefinitionId") Long trainingDefinitionId);

    List<TrainingPhase> findAllByTrainingDefinitionIdOrderByOrder(Long trainingDefinitionId);

    Optional<TrainingPhase> findByTrainingDefinitionIdAndOrder(Long trainingDefinitionId, Integer trainingPhaseOrder);

    List<TrainingPhase> findAllByTrainingDefinitionId(@Param("trainingDefinitionId") Long trainingDefinitionId);
}
