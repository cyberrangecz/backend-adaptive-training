package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.QuestionPhaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPhaseRelationRepository extends JpaRepository<QuestionPhaseRelation, Long> {
}
