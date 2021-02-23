package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPhaseRepository extends JpaRepository<TrainingPhase, Long> {

    @Query("SELECT COUNT(p.id) FROM TrainingPhase p WHERE p.trainingDefinition.id = :trainingDefinitionId")
    int getNumberOfExistingPhases(@Param("trainingDefinitionId") Long trainingDefinitionId);

    List<TrainingPhase> findAllByTrainingDefinitionIdOrderByOrder(Long trainingDefinitionId);
}
