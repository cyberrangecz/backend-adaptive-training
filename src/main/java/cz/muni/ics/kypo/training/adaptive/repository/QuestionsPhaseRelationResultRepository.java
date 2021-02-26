package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionsPhaseRelationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsPhaseRelationResultRepository extends JpaRepository<QuestionsPhaseRelationResult, Long>, QuerydslPredicateExecutor<QuestionsPhaseRelationResult> {
}
