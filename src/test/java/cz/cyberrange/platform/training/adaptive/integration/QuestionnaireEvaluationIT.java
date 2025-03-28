package cz.cyberrange.platform.training.adaptive.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.URIPath;
import cz.cyberrange.platform.training.adaptive.util.TestDataFactory;
import cz.cyberrange.platform.training.adaptive.rest.controller.TrainingRunsRestController;
import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionsPhaseRelationResult;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.TrainingPhaseQuestionsFulfillment;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionAnswerDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnairePhaseAnswersDTO;
import cz.cyberrange.platform.training.adaptive.rest.handler.CustomRestExceptionHandler;
import cz.cyberrange.platform.training.adaptive.persistence.repository.AdaptiveQuestionsFulfillmentRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.QuestionsPhaseRelationResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static cz.cyberrange.platform.training.adaptive.util.ObjectConverter.convertObjectToJson;
import static cz.cyberrange.platform.training.adaptive.util.TestDataFactory.ANOTHER_CORRECT_QUESTION_CHOICE;
import static cz.cyberrange.platform.training.adaptive.util.TestDataFactory.CORRECT_QUESTION_CHOICE;
import static cz.cyberrange.platform.training.adaptive.util.TestDataFactory.WRONG_QUESTION_CHOICE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        IntegrationTestApplication.class,
        TrainingRunsRestController.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class QuestionnaireEvaluationIT {

    private MockMvc mvc;
    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TrainingRunsRestController trainingRunsRestController;
    @Autowired
    private QuestionsPhaseRelationResultRepository questionsPhaseRelationResultRepository;
    @Autowired
    private AdaptiveQuestionsFulfillmentRepository adaptiveQuestionsFulfillmentRepository;
    @Autowired
    @Qualifier("objMapperRESTApi")
    private ObjectMapper mapper;


    private TrainingRun trainingRun1;
    private Question freeformQuestion, multipleChoiceQuestion, ratingFormQuestion;
    private QuestionnairePhase questionnairePhase;
    private TrainingPhase trainingPhase, trainingPhase2;
    private QuestionPhaseRelation questionPhaseRelation;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(trainingRunsRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .setControllerAdvice(new CustomRestExceptionHandler())
                .build();

        TrainingDefinition trainingDefinition = testDataFactory.getReleasedDefinition();
        TrainingInstance trainingInstance = testDataFactory.getOngoingInstance();
        trainingInstance.setTrainingDefinition(trainingDefinition);

        questionnairePhase = testDataFactory.getAdaptive();
        questionnairePhase.setTrainingDefinition(trainingDefinition);

        trainingPhase = testDataFactory.getTrainingPhase1();
        trainingPhase.setOrder(1);
        trainingPhase.setTrainingDefinition(trainingDefinition);
        trainingPhase2 = testDataFactory.getTrainingPhase2();
        trainingPhase2.setOrder(2);
        trainingPhase2.setTrainingDefinition(trainingDefinition);

        freeformQuestion = testDataFactory.getFreeFormQuestion();
        freeformQuestion.setQuestionnairePhase(questionnairePhase);
        freeformQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoice()));
        multipleChoiceQuestion = testDataFactory.getMultipleChoiceQuestion();
        multipleChoiceQuestion.setQuestionnairePhase(questionnairePhase);
        multipleChoiceQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));
        ratingFormQuestion = testDataFactory.getRatingFormQuestion();
        ratingFormQuestion.setQuestionnairePhase(questionnairePhase);
        ratingFormQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));

        questionPhaseRelation = new QuestionPhaseRelation();
        questionPhaseRelation.setOrder(0);
        questionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        questionPhaseRelation.setQuestions(Set.of(freeformQuestion, multipleChoiceQuestion, ratingFormQuestion));
        questionPhaseRelation.setSuccessRate(50);
        questionPhaseRelation.setRelatedTrainingPhase(trainingPhase);

        User participant1 = testDataFactory.getUser1();
        trainingRun1 = testDataFactory.getRunningRun();
        trainingRun1.setParticipantRef(participant1);
        trainingRun1.setCurrentPhase(questionnairePhase);
        trainingRun1.setTrainingInstance(trainingInstance);

        entityManager.persist(trainingDefinition);
        entityManager.persist(trainingInstance);
        entityManager.persist(questionnairePhase);
        entityManager.persist(trainingPhase);
        entityManager.persist(trainingPhase2);
        entityManager.persist(freeformQuestion);
        entityManager.persist(multipleChoiceQuestion);
        entityManager.persist(ratingFormQuestion);
        entityManager.persist(questionPhaseRelation);

        entityManager.persist(participant1);
        entityManager.persist(trainingRun1);
    }

    @Test
    void evaluateQuestionnaire_allRight() throws Exception {
        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO2 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(1));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(1.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(true));
    }

    @Test
    void evaluateQuestionnaire_allWrong() throws Exception {
        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getWrongAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO2 = testDataFactory.getWrongAnswer();
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getWrongAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(1));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(0.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(false));
    }

    @Test
    void evaluateQuestionnaire_emptyAnswersProvided() throws Exception {
        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getEmptyAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO2 = testDataFactory.getEmptyAnswer();
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getEmptyAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(1));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(0.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(false));
    }

    @Test
    void evaluateQuestionnaire_twoQuestionPhaseRelations_allRight() throws Exception {
        QuestionPhaseRelation secondQuestionPhaseRelation = new QuestionPhaseRelation();
        secondQuestionPhaseRelation.setOrder(1);
        secondQuestionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        secondQuestionPhaseRelation.setQuestions(Set.of(ratingFormQuestion));
        secondQuestionPhaseRelation.setSuccessRate(100);
        secondQuestionPhaseRelation.setRelatedTrainingPhase(trainingPhase);
        entityManager.persist(secondQuestionPhaseRelation);

        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO2 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(2));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(1.0, 0.001));
        QuestionsPhaseRelationResult secondQuestionPhaseResult = questionPhaseResults.get(1);
        assertThat(secondQuestionPhaseResult.getAchievedResult(), closeTo(1.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(true));
    }

    @Test
    void evaluateQuestionnaire_twoQuestionPhaseRelations_mixedResults() throws Exception {
        QuestionPhaseRelation secondQuestionPhaseRelation = new QuestionPhaseRelation();
        secondQuestionPhaseRelation.setOrder(1);
        secondQuestionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        secondQuestionPhaseRelation.setQuestions(Set.of(freeformQuestion));
        secondQuestionPhaseRelation.setSuccessRate(100);
        secondQuestionPhaseRelation.setRelatedTrainingPhase(trainingPhase);
        entityManager.persist(secondQuestionPhaseRelation);

        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getWrongAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO2 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(2));

        QuestionsPhaseRelationResult questionPhaseResult = findCorrespondingPhaseRelation(questionPhaseResults, questionPhaseRelation);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(0.66, 0.01));
        QuestionsPhaseRelationResult secondQuestionPhaseResult = findCorrespondingPhaseRelation(questionPhaseResults, secondQuestionPhaseRelation);
        assertThat(secondQuestionPhaseResult.getAchievedResult(), closeTo(0.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(false));
    }

    @Test
    void evaluateQuestionnaire_threeQuestionPhaseRelations_twoTrainingPhases_mixedResults() throws Exception {
        QuestionPhaseRelation secondQuestionPhaseRelation = new QuestionPhaseRelation();
        secondQuestionPhaseRelation.setOrder(1);
        secondQuestionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        secondQuestionPhaseRelation.setQuestions(Set.of(freeformQuestion));
        secondQuestionPhaseRelation.setSuccessRate(100);
        secondQuestionPhaseRelation.setRelatedTrainingPhase(trainingPhase);
        entityManager.persist(secondQuestionPhaseRelation);

        QuestionPhaseRelation thirdQuestionPhaseRelation = new QuestionPhaseRelation();
        thirdQuestionPhaseRelation.setOrder(2);
        thirdQuestionPhaseRelation.setQuestionnairePhase(questionnairePhase);
        thirdQuestionPhaseRelation.setQuestions(Set.of(ratingFormQuestion));
        thirdQuestionPhaseRelation.setSuccessRate(100);
        thirdQuestionPhaseRelation.setRelatedTrainingPhase(trainingPhase2);
        entityManager.persist(thirdQuestionPhaseRelation);

        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getWrongAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO2 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(3));

        QuestionsPhaseRelationResult questionPhaseResult = findCorrespondingPhaseRelation(questionPhaseResults, questionPhaseRelation);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(0.66, 0.01));
        QuestionsPhaseRelationResult secondQuestionPhaseResult = findCorrespondingPhaseRelation(questionPhaseResults, secondQuestionPhaseRelation);
        assertThat(secondQuestionPhaseResult.getAchievedResult(), closeTo(0.0, 0.001));
        QuestionsPhaseRelationResult thirdQuestionPhaseResult = findCorrespondingPhaseRelation(questionPhaseResults, thirdQuestionPhaseRelation);
        assertThat(thirdQuestionPhaseResult.getAchievedResult(), closeTo(1.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(2));
        TrainingPhaseQuestionsFulfillment firstPhaseFulfillment = findCorrespondingPhaseFulfillment(questionsFulfillmentList, trainingPhase);
        assertThat(firstPhaseFulfillment.isFulfilled(), is(false));
        TrainingPhaseQuestionsFulfillment secondPhaseFulfillment = findCorrespondingPhaseFulfillment(questionsFulfillmentList, trainingPhase2);
        assertThat(secondPhaseFulfillment.isFulfilled(), is(true));
    }

    @Test
    void evaluateQuestionnaire_moreCorrectAnswersInMCQ_correctlyAnswered() throws Exception {
        multipleChoiceQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getAnotherCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));
        entityManager.persist(multipleChoiceQuestion);

        QuestionAnswerDTO questionAnswerDTO2 = new QuestionAnswerDTO();
        questionAnswerDTO2.setAnswers(Set.of(CORRECT_QUESTION_CHOICE, ANOTHER_CORRECT_QUESTION_CHOICE));
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());

        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(1));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(1.0, 0.001));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(true));
    }

    @Test
    void evaluateQuestionnaire_moreCorrectAnswersInMCQ_notCompletelyAnswered() throws Exception {
        multipleChoiceQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getAnotherCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));
        entityManager.persist(multipleChoiceQuestion);

        QuestionAnswerDTO questionAnswerDTO2 = new QuestionAnswerDTO();
        questionAnswerDTO2.setAnswers(Set.of(ANOTHER_CORRECT_QUESTION_CHOICE));
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());

        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(1));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(0.66, 0.01));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(true));
    }

    @Test
    void evaluateQuestionnaire_moreCorrectAnswersInMCQ_answeredWithWrongAnswer() throws Exception {
        multipleChoiceQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getAnotherCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));
        entityManager.persist(multipleChoiceQuestion);

        QuestionAnswerDTO questionAnswerDTO2 = new QuestionAnswerDTO();
        questionAnswerDTO2.setAnswers(Set.of(CORRECT_QUESTION_CHOICE, ANOTHER_CORRECT_QUESTION_CHOICE, WRONG_QUESTION_CHOICE));
        questionAnswerDTO2.setQuestionId(multipleChoiceQuestion.getId());

        QuestionAnswerDTO questionAnswerDTO1 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO1.setQuestionId(freeformQuestion.getId());
        QuestionAnswerDTO questionAnswerDTO3 = testDataFactory.getCorrectAnswer();
        questionAnswerDTO3.setQuestionId(ratingFormQuestion.getId());

        QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO = new QuestionnairePhaseAnswersDTO();
        questionnairePhaseAnswersDTO.setAnswers(List.of(questionAnswerDTO1, questionAnswerDTO2, questionAnswerDTO3));
        callRestForQuestionnaireEvaluation(questionnairePhaseAnswersDTO);

        List<QuestionsPhaseRelationResult> questionPhaseResults = questionsPhaseRelationResultRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionPhaseResults, notNullValue());
        assertThat(questionPhaseResults, hasSize(1));
        QuestionsPhaseRelationResult questionPhaseResult = questionPhaseResults.get(0);
        assertThat(questionPhaseResult.getAchievedResult(), closeTo(0.66, 0.01));

        List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList = adaptiveQuestionsFulfillmentRepository.findByTrainingRunId(trainingRun1.getId());
        assertThat(questionsFulfillmentList, notNullValue());
        assertThat(questionsFulfillmentList, hasSize(1));
        TrainingPhaseQuestionsFulfillment questionsFulfillment = questionsFulfillmentList.get(0);
        assertThat(questionsFulfillment.isFulfilled(), is(true));
    }

    private void callRestForQuestionnaireEvaluation(QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO) throws Exception {
        mvc.perform(put(URIPath.QUESTIONNAIRE_EVALUATION.getUri(), trainingRun1.getId())
                .content(convertObjectToJson(questionnairePhaseAnswersDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andReturn().getResponse();
    }

    private QuestionsPhaseRelationResult findCorrespondingPhaseRelation(List<QuestionsPhaseRelationResult> questionsPhaseRelationResults, QuestionPhaseRelation questionPhaseRelation) {
        return questionsPhaseRelationResults.stream()
                .filter(x -> x.getQuestionPhaseRelation().equals(questionPhaseRelation))
                .findFirst()
                .orElse(null);
    }

    private TrainingPhaseQuestionsFulfillment findCorrespondingPhaseFulfillment(List<TrainingPhaseQuestionsFulfillment> questionsFulfillmentList, TrainingPhase trainingPhase) {
        return questionsFulfillmentList.stream()
                .filter(x -> x.getTrainingPhase().equals(trainingPhase))
                .findFirst()
                .orElse(null);
    }
}
