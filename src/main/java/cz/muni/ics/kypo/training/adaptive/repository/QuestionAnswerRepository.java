package cz.muni.ics.kypo.training.adaptive.repository;

import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionAnswer;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.QuestionAnswerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, QuestionAnswerId>, QuerydslPredicateExecutor<QuestionAnswer> {

    @Modifying
    void deleteAllByTrainingRunId(Long trainingRunId);

    List<QuestionAnswer> getAllByTrainingRunId(Long trainingRunId);
}
