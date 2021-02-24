package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionPhaseResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPhaseResultRepository extends JpaRepository<QuestionPhaseResult, Long>, QuerydslPredicateExecutor<QuestionPhaseResult> {
}
