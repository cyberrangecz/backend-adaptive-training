package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionAnswer;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, QuestionAnswerId> {
}
