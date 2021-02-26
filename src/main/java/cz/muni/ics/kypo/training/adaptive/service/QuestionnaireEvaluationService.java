package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.phase.QuestionnairePhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.questions.*;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.enums.QuestionnaireType;
import cz.muni.ics.kypo.training.adaptive.exceptions.BadRequestException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionAnswerRepository;
import cz.muni.ics.kypo.training.adaptive.repository.QuestionPhaseResultRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.QuestionPhaseRelationRepository;
import cz.muni.ics.kypo.training.adaptive.repository.phases.QuestionRepository;
import cz.muni.ics.kypo.training.adaptive.repository.training.TrainingRunRepository;
import cz.muni.ics.kypo.training.adaptive.service.audit.AuditEventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionnaireEvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireEvaluationService.class);

    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionPhaseRelationRepository questionPhaseRelationRepository;
    private final QuestionPhaseResultRepository questionPhaseResultRepository;
    private final TrainingRunRepository trainingRunRepository;
    private final AuditEventsService auditEventsService;

    @Autowired
    public QuestionnaireEvaluationService(QuestionRepository questionRepository,
                                          QuestionAnswerRepository questionAnswerRepository,
                                          QuestionPhaseRelationRepository questionPhaseRelationRepository,
                                          QuestionPhaseResultRepository questionPhaseResultRepository,
                                          TrainingRunRepository trainingRunRepository,
                                          AuditEventsService auditEventsService) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.questionPhaseRelationRepository = questionPhaseRelationRepository;
        this.questionPhaseResultRepository = questionPhaseResultRepository;
        this.trainingRunRepository = trainingRunRepository;
        this.auditEventsService = auditEventsService;
    }

    /**
     * Evaluate and store answers to questionnaire.
     *
     * @param trainingRunId             id of training run to answer the questionnaire.
     * @param questionnairePhaseAnswers answers to questionnaire to be evaluated. Map of entries (Key = question ID, Value = answer).
     * @throws EntityNotFoundException training run is not found.
     */
    public List<QuestionAnswer> saveAndEvaluateAnswersToQuestionnaire(Long trainingRunId, Map<Long, Set<String>> questionnairePhaseAnswers) {
        TrainingRun trainingRun = this.findByIdWithPhase(trainingRunId);
        this.checkIfCanBeEvaluated(trainingRun);

        List<QuestionAnswer> storedQuestionsAnswers = new ArrayList<>();
        for (Map.Entry<Long, Set<String>> questionAnswers : questionnairePhaseAnswers.entrySet()) {
            Long questionnaireId = trainingRun.getCurrentPhase().getId();
            storedQuestionsAnswers.add(saveQuestionAnswer(questionAnswers.getKey(), questionAnswers.getValue(), questionnaireId, trainingRun));
        }
        if (((QuestionnairePhase) trainingRun.getCurrentPhase()).getQuestionnaireType() == QuestionnaireType.ADAPTIVE) {
            this.evaluateAnswersToQuestionnaire(trainingRun, storedQuestionsAnswers);
        }
        auditEventsService.auditPhaseCompletedAction(trainingRun);
        auditEventsService.auditQuestionnaireAnswersAction(trainingRun, storedQuestionsAnswers.stream()
                .collect(Collectors.toMap(questionAnswer -> questionAnswer.getQuestion().getText(), QuestionAnswer::getAnswers))
                .toString());
        trainingRun.setPhaseAnswered(true);
        return storedQuestionsAnswers;
    }

    private void checkIfCanBeEvaluated(TrainingRun trainingRun) {
        if (!(trainingRun.getCurrentPhase() instanceof QuestionnairePhase)) {
            throw new BadRequestException("Current phase is not questionnaire phase and cannot be evaluated.");
        }
        if (trainingRun.isPhaseAnswered())
            throw new EntityConflictException(new EntityErrorDetail(TrainingRun.class, "id", Long.class, trainingRun.getId(),
                    "Current phase of the training run has been already answered."));
    }

    private QuestionAnswer saveQuestionAnswer(Long questionId, Set<String> answers, Long questionnaireId, TrainingRun trainingRun) {
        Question question = this.findQuestionByIdAndQuestionnairePhaseId(questionId, questionnaireId);
        QuestionAnswer questionAnswer = new QuestionAnswer(question, trainingRun);
        questionAnswer.setAnswers(answers);
        return questionAnswerRepository.save(questionAnswer);
    }

    private void evaluateAnswersToQuestionnaire(TrainingRun trainingRun, List<QuestionAnswer> questionAnswers) {
        if (CollectionUtils.isEmpty(questionAnswers)) {
            return;
        }
        Set<Long> questionIdList = questionAnswers.stream()
                .map(QuestionAnswer::getQuestionAnswerId)
                .map(QuestionAnswerId::getQuestionId)
                .collect(Collectors.toSet());

        List<QuestionPhaseRelation> questionPhaseRelations = questionPhaseRelationRepository.findAllByQuestionIdList(questionIdList);
        for (QuestionPhaseRelation questionPhaseRelation : questionPhaseRelations) {
            double numberOfCorrectlyAnsweredQuestions = 0;
            if (CollectionUtils.isEmpty(questionPhaseRelation.getQuestions())) {
                LOG.warn("No questions found for question phase relation {}", questionPhaseRelation.getId());
                continue;
            }

            for (Question question : questionPhaseRelation.getQuestions()) {
                Optional<QuestionAnswer> correspondingAnswer = findCorrespondingAnswer(question.getId(), questionAnswers);
                if (correspondingAnswer.isPresent() && checkCorrectnessOfAnswersToQuestion(correspondingAnswer.get().getAnswers(), question)) {
                    numberOfCorrectlyAnsweredQuestions++;
                } else {
                    LOG.debug("No answer found for question {}. It is assumed as a wrong answer", question.getId());
                }
            }
            double achievedResult = numberOfCorrectlyAnsweredQuestions / questionPhaseRelation.getQuestions().size();

            QuestionPhaseResult questionPhaseResult = new QuestionPhaseResult();
            questionPhaseResult.setAchievedResult(achievedResult);
            questionPhaseResult.setQuestionPhaseRelation(questionPhaseRelation);
            questionPhaseResult.setTrainingRun(trainingRun);
            questionPhaseResultRepository.save(questionPhaseResult);
        }
    }

    private Optional<QuestionAnswer> findCorrespondingAnswer(Long questionId, List<QuestionAnswer> answers) {
        return answers.stream()
                .filter(a -> Objects.equals(questionId, a.getQuestionAnswerId().getQuestionId()))
                .findFirst();
    }

    private boolean checkCorrectnessOfAnswersToQuestion(Set<String> answers, Question question) {
        int numberOfCorrectAnswers = (int) question.getChoices().stream()
                .filter(questionChoice -> answers.contains(questionChoice.getText()) && questionChoice.isCorrect())
                .count();
        return numberOfCorrectAnswers == answers.size();
    }

    private TrainingRun findByIdWithPhase(Long runId) {
        return trainingRunRepository.findByIdWithPhase(runId).orElseThrow(() -> new EntityNotFoundException(
                new EntityErrorDetail(TrainingRun.class, "id", runId.getClass(), runId)));
    }

    private Question findQuestionByIdAndQuestionnairePhaseId(Long questionId, Long questionnaireId) {
        return questionRepository.findByIdAndQuestionnairePhaseId(questionId, questionnaireId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(Question.class, "id", Long.class, questionId)));
    }
}
