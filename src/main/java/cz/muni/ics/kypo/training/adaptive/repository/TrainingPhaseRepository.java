package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.TrainingPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingPhaseRepository extends JpaRepository<TrainingPhase, Long> {

    @Query("SELECT COUNT(p.id) FROM TrainingPhase p WHERE p.trainingDefinitionId = :trainingDefinitionId")
    int getNumberOfExistingPhases(@Param("trainingDefinitionId") Long trainingDefinitionId);

    List<TrainingPhase> findAllByTrainingDefinitionIdOrderByOrder(Long trainingDefinitionId);
}
