package cz.cyberrange.platform.training.adaptive.persistence.repository;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionAnswer;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionAnswerId;
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
