package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.Question;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionAnswer;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionAnswerId;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionChoice;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionPhaseRelation;
import cz.muni.ics.kypo.training.adaptive.domain.QuestionPhaseResult;
import cz.muni.ics.kypo.training.adaptive.dto.run.QuestionAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.run.QuestionnairePhaseAnswersDTO;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionAnswerRepository;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionPhaseRelationRepository;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionPhaseResultRepository;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionnaireEvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireEvaluationService.class);

    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionPhaseRelationRepository questionPhaseRelationRepository;
    private final QuestionPhaseResultRepository questionPhaseResultRepository;

    @Autowired
    public QuestionnaireEvaluationService(QuestionRepository questionRepository, QuestionAnswerRepository questionAnswerRepository,
                                          QuestionPhaseRelationRepository questionPhaseRelationRepository, QuestionPhaseResultRepository questionPhaseResultRepository) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.questionPhaseRelationRepository = questionPhaseRelationRepository;
        this.questionPhaseResultRepository = questionPhaseResultRepository;
    }

    public List<QuestionAnswer> saveAnswersToQuestionnaire(Long runId, QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO) {
        List<QuestionAnswer> result = new ArrayList<>();
        for (QuestionAnswerDTO answer : questionnairePhaseAnswersDTO.getAnswers()) {
            Question question = questionRepository.findById(answer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question was not found"));
            // TODO throw proper exception once kypo2-training is migrated

            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setQuestionAnswerId(new QuestionAnswerId(question, runId));
            questionAnswer.setAnswer(answer.getAnswer());

            questionAnswerRepository.save(questionAnswer);
            result.add(questionAnswer);
        }

        return result;
    }

    public void evaluateAnswersToQuestionnaire(Long runId, List<QuestionAnswer> answers) {
        if (CollectionUtils.isEmpty(answers)) {
            return;
        }

        Set<Long> questionIdList = answers.stream()
                .map(QuestionAnswer::getQuestionAnswerId)
                .map(QuestionAnswerId::getQuestion)
                .map(Question::getId)
                .collect(Collectors.toSet());

        List<QuestionPhaseRelation> questionPhaseRelations = questionPhaseRelationRepository.findAllByQuestionIdList(questionIdList);
        for (QuestionPhaseRelation questionPhaseRelation : questionPhaseRelations) {
            int numberOfCorrectAnswers = 0;
            if (CollectionUtils.isEmpty(questionPhaseRelation.getQuestions())) {
                LOG.warn("No questions found for question phase relation {}", questionPhaseRelation.getId());
                continue;
            }

            for (Question question : questionPhaseRelation.getQuestions()) {
                Optional<QuestionAnswer> correspondingAnswer = findCorrespondingAnswer(question.getId(), answers);
                if (correspondingAnswer.isPresent()) {
                    String answer = correspondingAnswer.get().getAnswer();
                    Optional<QuestionChoice> correspondingQuestionChoice = findCorrespondingQuestionChoice(answer, question.getChoices());
                    if (correspondingQuestionChoice.isPresent() && correspondingQuestionChoice.get().isCorrect()) {
                        numberOfCorrectAnswers++;
                    }
                } else {
                    LOG.debug("No answer found for question {}. It is assumed as a wrong answer", question.getId());
                }
            }

            int achievedResult = numberOfCorrectAnswers * 100 / questionPhaseRelation.getQuestions().size();

            QuestionPhaseResult questionPhaseResult = new QuestionPhaseResult();
            questionPhaseResult.setAchievedResult(achievedResult);
            questionPhaseResult.setQuestionPhaseRelation(questionPhaseRelation);
            questionPhaseResult.setTrainingRunId(runId);

            questionPhaseResultRepository.save(questionPhaseResult);
        }
    }

    private Optional<QuestionAnswer> findCorrespondingAnswer(Long questionId, List<QuestionAnswer> answers) {
        return answers.stream()
                .filter(a -> Objects.equals(questionId, a.getQuestionAnswerId().getQuestion().getId()))
                .findFirst();
    }

    private Optional<QuestionChoice> findCorrespondingQuestionChoice(String answer, List<QuestionChoice> questionChoices) {
        return questionChoices.stream()
                .filter(q -> Objects.equals(answer, q.getText()))
                .findFirst();
    }
}
