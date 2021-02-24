package cz.muni.ics.kypo.training.adaptive.repository.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QuerydslPredicateExecutor<Question> {

    Optional<Question> findByIdAndQuestionnairePhaseId(Long questionId, Long questionnaireId);
}
