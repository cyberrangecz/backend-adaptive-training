package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.QTrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPhaseRepository extends JpaRepository<TrainingPhase, Long> , QuerydslPredicateExecutor<TrainingPhase> {

    @Query("SELECT COUNT(p.id) FROM TrainingPhase p WHERE p.trainingDefinition.id = :trainingDefinitionId")
    int getNumberOfExistingPhases(@Param("trainingDefinitionId") Long trainingDefinitionId);

    List<TrainingPhase> findAllByTrainingDefinitionIdOrderByOrder(Long trainingDefinitionId);

    Optional<TrainingPhase> findByTrainingDefinitionIdAndOrder(Long trainingDefinitionId, Integer trainingPhaseOrder);
}
