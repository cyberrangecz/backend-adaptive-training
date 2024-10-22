package cz.cyberrange.platform.training.adaptive.service;

import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionAnswer;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionAnswerId;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionChoice;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionsPhaseRelationResult;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.TrainingPhaseQuestionsFulfillment;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.BadRequestException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityConflictException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.training.adaptive.persistence.repository.AdaptiveQuestionsFulfillmentRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.QuestionAnswerRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.QuestionsPhaseRelationResultRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionPhaseRelationRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingRunRepository;
import cz.cyberrange.platform.training.adaptive.service.audit.AuditEventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionnaireEvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionnaireEvaluationService.class);

    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionPhaseRelationRepository questionPhaseRelationRepository;
    private final QuestionsPhaseRelationResultRepository questionsPhaseRelationResultRepository;
    private final AdaptiveQuestionsFulfillmentRepository adaptiveQuestionsFulfillmentRepository;
    private final TrainingRunRepository trainingRunRepository;
    private final AuditEventsService auditEventsService;

    @Autowired
    public QuestionnaireEvaluationService(QuestionRepository questionRepository,
                                          QuestionAnswerRepository questionAnswerRepository,
                                          QuestionPhaseRelationRepository questionPhaseRelationRepository,
                                          QuestionsPhaseRelationResultRepository questionsPhaseRelationResultRepository,
                                          AdaptiveQuestionsFulfillmentRepository adaptiveQuestionsFulfillmentRepository,
                                          TrainingRunRepository trainingRunRepository,
                                          AuditEventsService auditEventsService) {
        this.questionRepository = questionRepository;
        this.questionAnswerRepository = questionAnswerRepository;
        this.questionPhaseRelationRepository = questionPhaseRelationRepository;
        this.questionsPhaseRelationResultRepository = questionsPhaseRelationResultRepository;
        this.adaptiveQuestionsFulfillmentRepository = adaptiveQuestionsFulfillmentRepository;
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
            this.evaluateAnswersToAdaptiveQuestionnaire(trainingRun, storedQuestionsAnswers);
        }
        auditEventsService.auditPhaseCompletedAction(trainingRun);
        auditEventsService.auditQuestionnaireAnswersAction(trainingRun, storedQuestionsAnswers.toString());
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

    private void evaluateAnswersToAdaptiveQuestionnaire(TrainingRun trainingRun, List<QuestionAnswer> questionAnswers) {
        if (CollectionUtils.isEmpty(questionAnswers)) {
            return;
        }
        Set<Long> questionIdList = questionAnswers.stream()
                .map(QuestionAnswer::getQuestionAnswerId)
                .map(QuestionAnswerId::getQuestionId)
                .collect(Collectors.toSet());
        List<QuestionPhaseRelation> questionPhaseRelations = questionPhaseRelationRepository.findAllByQuestionIdList(questionIdList);
        Map<Long, Boolean> trainingPhaseQuestionsFulfilledMap = new HashMap<>();
        Map<Long, TrainingPhase> trainingPhasesMap = new HashMap<>();

        for (QuestionPhaseRelation questionPhaseRelation : questionPhaseRelations) {
            if (CollectionUtils.isEmpty(questionPhaseRelation.getQuestions())) {
                LOG.warn("No questions found for question phase relation {}", questionPhaseRelation.getId());
                continue;
            }
            TrainingPhase relatedTrainingPhase = questionPhaseRelation.getRelatedTrainingPhase();
            trainingPhasesMap.putIfAbsent(relatedTrainingPhase.getId(), relatedTrainingPhase);

            double achievedResult = evaluateQuestionPhaseRelation(questionPhaseRelation, questionAnswers);
            boolean trainingPhaseQuestionsFulfilled = trainingPhaseQuestionsFulfilledMap.getOrDefault(relatedTrainingPhase.getId(), true);
            trainingPhaseQuestionsFulfilledMap.put(questionPhaseRelation.getRelatedTrainingPhase().getId(), trainingPhaseQuestionsFulfilled && questionPhaseRelation.getSuccessRate() <= (achievedResult * 100));
            storeQuestionsPhaseRelationResult(questionPhaseRelation, trainingRun, achievedResult);

        }
        storeTrainingPhaseQuestionsFulfillment(trainingRun, trainingPhaseQuestionsFulfilledMap.entrySet(), trainingPhasesMap);
    }

    private double evaluateQuestionPhaseRelation(QuestionPhaseRelation questionPhaseRelation, List<QuestionAnswer> questionAnswers) {
        double numberOfCorrectlyAnsweredQuestions = 0;
        for (Question question : questionPhaseRelation.getQuestions()) {
            Optional<QuestionAnswer> correspondingAnswer = findCorrespondingAnswer(question.getId(), questionAnswers);
            if (correspondingAnswer.isPresent() && checkCorrectnessOfAnswersToQuestion(correspondingAnswer.get().getAnswers(), question)) {
                numberOfCorrectlyAnsweredQuestions++;
            } else {
                LOG.debug("No answer found for question {}. It is assumed as a wrong answer", question.getId());
            }
        }
        return numberOfCorrectlyAnsweredQuestions / questionPhaseRelation.getQuestions().size();

    }

    private void storeTrainingPhaseQuestionsFulfillment(TrainingRun trainingRun,
                                                        Set<Map.Entry<Long, Boolean>> trainingPhasesQuestionsFulfillmentMap,
                                                        Map<Long, TrainingPhase> trainingPhaseMap) {
        for (Map.Entry<Long, Boolean> questionsFulfillment : trainingPhasesQuestionsFulfillmentMap) {
            TrainingPhaseQuestionsFulfillment trainingPhaseQuestionsFulfillment = new TrainingPhaseQuestionsFulfillment();
            trainingPhaseQuestionsFulfillment.setTrainingRun(trainingRun);
            trainingPhaseQuestionsFulfillment.setTrainingPhase(trainingPhaseMap.get(questionsFulfillment.getKey()));
            trainingPhaseQuestionsFulfillment.setFulfilled(questionsFulfillment.getValue());
            this.adaptiveQuestionsFulfillmentRepository.save(trainingPhaseQuestionsFulfillment);
        }
    }

    private void storeQuestionsPhaseRelationResult(QuestionPhaseRelation questionPhaseRelation,
                                                   TrainingRun trainingRun,
                                                   double achievedResult) {
        QuestionsPhaseRelationResult questionPhaseResult = new QuestionsPhaseRelationResult();
        questionPhaseResult.setAchievedResult(achievedResult);
        questionPhaseResult.setQuestionPhaseRelation(questionPhaseRelation);
        questionPhaseResult.setTrainingRun(trainingRun);
        questionsPhaseRelationResultRepository.save(questionPhaseResult);
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
        if (QuestionType.MCQ.equals(question.getQuestionType())) {
            int numberOfCorrectMCQChoices = (int) question.getChoices().stream()
                    .filter(QuestionChoice::isCorrect)
                    .count();
            return numberOfCorrectMCQChoices == numberOfCorrectAnswers && numberOfCorrectAnswers == answers.size();
        } else {
            return numberOfCorrectAnswers == answers.size() && numberOfCorrectAnswers > 0;
        }
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
