package cz.cyberrange.platform.training.adaptive.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.URIPath;
import cz.cyberrange.platform.training.adaptive.util.TestDataFactory;
import cz.cyberrange.platform.training.adaptive.rest.controller.PhasesController;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.InfoPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.MitreTechnique;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.AbstractPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.PhaseCreateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.info.InfoPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.info.InfoPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionChoiceDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnairePhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.DecisionMatrixRowDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TrainingPhaseUpdateDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.technique.MitreTechniqueDTO;
import cz.cyberrange.platform.training.adaptive.persistence.enums.PhaseType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionType;
import cz.cyberrange.platform.training.adaptive.persistence.enums.QuestionnaireType;
import cz.cyberrange.platform.training.adaptive.rest.handler.CustomRestExceptionHandler;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.InfoPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionnairePhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.TrainingPhaseRepository;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static cz.cyberrange.platform.training.adaptive.util.ObjectConverter.convertJsonToObject;
import static cz.cyberrange.platform.training.adaptive.util.ObjectConverter.convertObjectToJson;
import static cz.cyberrange.platform.training.adaptive.util.TestDataFactory.CORRECT_QUESTION_CHOICE;
import static cz.cyberrange.platform.training.adaptive.util.TestDataFactory.WRONG_QUESTION_CHOICE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        IntegrationTestApplication.class,
        PhasesController.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class PhasesControllerIT {

    private MockMvc mvc;
    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PhasesController phasesController;
    @Autowired
    private InfoPhaseRepository infoPhaseRepository;
    @Autowired
    private TrainingPhaseRepository trainingPhaseRepository;
    @Autowired
    private QuestionnairePhaseRepository questionnairePhaseRepository;
    @Autowired
    private UserManagementServiceApi userManagementServiceApi;
    @Autowired
    @Qualifier("objMapperRESTApi")
    private ObjectMapper mapper;

    private TrainingDefinition trainingDefinition;
    private TrainingPhase trainingPhase;
    private QuestionnairePhase questionnairePhase;
    private InfoPhase infoPhase;
    private UserRefDTO designer;
    private MitreTechnique mitreTechnique1, mitreTechnique2;
    private MitreTechniqueDTO mitreTechniqueDTO1, mitreTechniqueDTO2;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(phasesController)
                .setControllerAdvice(new CustomRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();

        trainingDefinition = testDataFactory.getUnreleasedDefinition();

        designer = testDataFactory.getUserRefDTO1();

        trainingPhase = testDataFactory.getTrainingPhase1();
        trainingPhase.setOrder(0);
        trainingPhase.setTrainingDefinition(trainingDefinition);

        questionnairePhase = testDataFactory.getAdaptive();
        questionnairePhase.setOrder(0);
        questionnairePhase.setTrainingDefinition(trainingDefinition);

        infoPhase = testDataFactory.getInfoPhase1();
        infoPhase.setOrder(0);
        infoPhase.setTrainingDefinition(trainingDefinition);

        mitreTechnique1 = testDataFactory.getMitreTechnique1();
        mitreTechnique2 = testDataFactory.getMitreTechnique2();
        mitreTechniqueDTO1 = testDataFactory.getMitreTechniqueDTO1();
        mitreTechniqueDTO2 = testDataFactory.getMitreTechniqueDTO2();

        entityManager.persist(trainingDefinition);
        doReturn(designer).when(userManagementServiceApi).getUserRefDTO();
    }


    @Test
    void createInfoPhase() throws Exception {
        InfoPhaseDTO createdInfoPhase = createPhaseAndDeserializeResponse(PhaseType.INFO, InfoPhaseDTO.class);
        assertThat(createdInfoPhase, notNullValue());
        assertThat(createdInfoPhase.getContent(), is("Content of info phase"));
        assertThat(createdInfoPhase.getTitle(), is("Title of info phase"));
        assertThat(createdInfoPhase.getPhaseType(), is(PhaseType.INFO));
        assertThat(createdInfoPhase.getOrder(), is(0));

        Optional<InfoPhase> createdInfoPhaseEntityOptional = infoPhaseRepository.findById(createdInfoPhase.getId());
        assertThat(createdInfoPhaseEntityOptional.orElse(null), notNullValue());
        InfoPhase createdInfoPhaseEntity = createdInfoPhaseEntityOptional.get();
        assertThat(createdInfoPhaseEntity.getContent(), is("Content of info phase"));
        assertThat(createdInfoPhaseEntity.getTitle(), is("Title of info phase"));
        assertThat(createdInfoPhaseEntity.getOrder(), is(0));
    }

    @Test
    void createTrainingPhase() throws Exception {
        TrainingPhaseDTO createdTrainingPhase = createPhaseAndDeserializeResponse(PhaseType.TRAINING, TrainingPhaseDTO.class);
        assertThat(createdTrainingPhase, notNullValue());
        assertThat(createdTrainingPhase.getTitle(), is("Title of training phase"));
        assertThat(createdTrainingPhase.getPhaseType(), is(PhaseType.TRAINING));
        assertThat(createdTrainingPhase.getOrder(), is(0));
        assertThat(createdTrainingPhase.getDecisionMatrix(), notNullValue());
        assertThat(createdTrainingPhase.getDecisionMatrix(), hasSize(1));
        assertThat(createdTrainingPhase.getTasks(), notNullValue());
        assertThat(createdTrainingPhase.getTasks(), is(empty()));

        Optional<TrainingPhase> createdTrainingPhaseEntityOptional = trainingPhaseRepository.findById(createdTrainingPhase.getId());
        assertThat(createdTrainingPhaseEntityOptional.orElse(null), notNullValue());
        TrainingPhase createdTrainingPhaseEntity = createdTrainingPhaseEntityOptional.get();
        assertThat(createdTrainingPhaseEntity.getTitle(), is("Title of training phase"));
        assertThat(createdTrainingPhaseEntity.getOrder(), is(0));
        assertThat(createdTrainingPhaseEntity.getDecisionMatrix(), hasSize(1));
        assertThat(createdTrainingPhaseEntity.getTasks(), is(empty()));
    }

    @Test
    void createAdaptiveQuestionnairePhase() throws Exception {
        QuestionnairePhaseDTO createdQuestionnairePhase = createQuestionnairePhaseAndDeserializeResponse(QuestionnaireType.ADAPTIVE);
        assertThat(createdQuestionnairePhase, notNullValue());
        assertThat(createdQuestionnairePhase.getTitle(), is("Title of questionnaire phase"));
        assertThat(createdQuestionnairePhase.getPhaseType(), is(PhaseType.QUESTIONNAIRE));
        assertThat(createdQuestionnairePhase.getQuestionnaireType(), is(QuestionnaireType.ADAPTIVE));
        assertThat(createdQuestionnairePhase.getOrder(), is(0));
        assertThat(createdQuestionnairePhase.getPhaseRelations(), notNullValue());
        assertThat(createdQuestionnairePhase.getPhaseRelations(), is(empty()));
        assertThat(createdQuestionnairePhase.getQuestions(), notNullValue());
        assertThat(createdQuestionnairePhase.getQuestions(), is(empty()));

        Optional<QuestionnairePhase> createdQuestionnairePhaseEntityOptional = questionnairePhaseRepository.findById(createdQuestionnairePhase.getId());
        assertThat(createdQuestionnairePhaseEntityOptional.orElse(null), notNullValue());
        QuestionnairePhase createdQuestionnairePhaseEntity = createdQuestionnairePhaseEntityOptional.get();
        assertThat(createdQuestionnairePhaseEntity.getTitle(), is("Title of questionnaire phase"));
        assertThat(createdQuestionnairePhaseEntity.getQuestionnaireType(), is(QuestionnaireType.ADAPTIVE));
        assertThat(createdQuestionnairePhaseEntity.getOrder(), is(0));
        assertThat(createdQuestionnairePhaseEntity.getQuestionPhaseRelations(), is(empty()));
        assertThat(createdQuestionnairePhaseEntity.getQuestions(), is(empty()));
    }

    @Test
    void createGeneralQuestionnairePhase() throws Exception {
        QuestionnairePhaseDTO createdQuestionnairePhase = createQuestionnairePhaseAndDeserializeResponse(QuestionnaireType.GENERAL);
        assertThat(createdQuestionnairePhase, notNullValue());
        assertThat(createdQuestionnairePhase.getTitle(), is("Title of questionnaire phase"));
        assertThat(createdQuestionnairePhase.getPhaseType(), is(PhaseType.QUESTIONNAIRE));
        assertThat(createdQuestionnairePhase.getQuestionnaireType(), is(QuestionnaireType.GENERAL));
        assertThat(createdQuestionnairePhase.getOrder(), is(0));
        assertThat(createdQuestionnairePhase.getPhaseRelations(), notNullValue());
        assertThat(createdQuestionnairePhase.getPhaseRelations(), is(empty()));
        assertThat(createdQuestionnairePhase.getQuestions(), notNullValue());
        assertThat(createdQuestionnairePhase.getQuestions(), is(empty()));

        Optional<QuestionnairePhase> createdQuestionnairePhaseEntityOptional = questionnairePhaseRepository.findById(createdQuestionnairePhase.getId());
        assertThat(createdQuestionnairePhaseEntityOptional.orElse(null), notNullValue());
        QuestionnairePhase createdQuestionnairePhaseEntity = createdQuestionnairePhaseEntityOptional.get();
        assertThat(createdQuestionnairePhaseEntity.getTitle(), is("Title of questionnaire phase"));
        assertThat(createdQuestionnairePhaseEntity.getQuestionnaireType(), is(QuestionnaireType.GENERAL));
        assertThat(createdQuestionnairePhaseEntity.getOrder(), is(0));
        assertThat(createdQuestionnairePhaseEntity.getQuestionPhaseRelations(), is(empty()));
        assertThat(createdQuestionnairePhaseEntity.getQuestions(), is(empty()));
    }

    @Test
    void findInfoPhaseById() throws Exception {
        InfoPhaseDTO createdInfoPhase = createPhaseAndDeserializeResponse(PhaseType.INFO, InfoPhaseDTO.class);

        InfoPhaseDTO returnedInfoPhase = findPhaseByIdAndDeserializeResponse(createdInfoPhase.getId(), InfoPhaseDTO.class);
        assertThat(returnedInfoPhase.getContent(), is("Content of info phase"));
        assertThat(returnedInfoPhase.getTitle(), is("Title of info phase"));
        assertThat(returnedInfoPhase.getPhaseType(), is(PhaseType.INFO));
        assertThat(returnedInfoPhase.getOrder(), is(0));
    }

    @Test
    void findTrainingPhaseById() throws Exception {
        TrainingPhaseDTO createdTrainingPhase = createPhaseAndDeserializeResponse(PhaseType.TRAINING, TrainingPhaseDTO.class);

        TrainingPhaseDTO returnedTrainingPhase = findPhaseByIdAndDeserializeResponse(createdTrainingPhase.getId(), TrainingPhaseDTO.class);
        assertThat(returnedTrainingPhase, notNullValue());
        assertThat(returnedTrainingPhase.getTitle(), is("Title of training phase"));
        assertThat(returnedTrainingPhase.getPhaseType(), is(PhaseType.TRAINING));
        assertThat(returnedTrainingPhase.getOrder(), is(0));
        assertThat(returnedTrainingPhase.getDecisionMatrix(), notNullValue());
        assertThat(returnedTrainingPhase.getDecisionMatrix(), hasSize(1));
        assertThat(returnedTrainingPhase.getTasks(), notNullValue());
        assertThat(returnedTrainingPhase.getTasks(), is(empty()));
    }

    @Test
    void findAdaptiveQuestionnairePhaseById() throws Exception {
        QuestionnairePhaseDTO createdQuestionnairePhase = createQuestionnairePhaseAndDeserializeResponse(QuestionnaireType.ADAPTIVE);

        QuestionnairePhaseDTO returnedQuestionnairePhase = findPhaseByIdAndDeserializeResponse(createdQuestionnairePhase.getId(), QuestionnairePhaseDTO.class);
        assertThat(returnedQuestionnairePhase, notNullValue());
        assertThat(returnedQuestionnairePhase.getTitle(), is("Title of questionnaire phase"));
        assertThat(returnedQuestionnairePhase.getPhaseType(), is(PhaseType.QUESTIONNAIRE));
        assertThat(returnedQuestionnairePhase.getQuestionnaireType(), is(QuestionnaireType.ADAPTIVE));
        assertThat(returnedQuestionnairePhase.getOrder(), is(0));
        assertThat(returnedQuestionnairePhase.getPhaseRelations(), notNullValue());
        assertThat(returnedQuestionnairePhase.getPhaseRelations(), is(empty()));
        assertThat(returnedQuestionnairePhase.getQuestions(), notNullValue());
        assertThat(returnedQuestionnairePhase.getQuestions(), is(empty()));
    }

    @Test
    void findGeneralQuestionnairePhaseById() throws Exception {
        QuestionnairePhaseDTO createdQuestionnairePhase = createQuestionnairePhaseAndDeserializeResponse(QuestionnaireType.GENERAL);

        QuestionnairePhaseDTO returnedQuestionnairePhase = findPhaseByIdAndDeserializeResponse(createdQuestionnairePhase.getId(), QuestionnairePhaseDTO.class);
        assertThat(returnedQuestionnairePhase, notNullValue());
        assertThat(returnedQuestionnairePhase.getTitle(), is("Title of questionnaire phase"));
        assertThat(returnedQuestionnairePhase.getPhaseType(), is(PhaseType.QUESTIONNAIRE));
        assertThat(returnedQuestionnairePhase.getQuestionnaireType(), is(QuestionnaireType.GENERAL));
        assertThat(returnedQuestionnairePhase.getOrder(), is(0));
        assertThat(returnedQuestionnairePhase.getPhaseRelations(), notNullValue());
        assertThat(returnedQuestionnairePhase.getPhaseRelations(), is(empty()));
        assertThat(returnedQuestionnairePhase.getQuestions(), notNullValue());
        assertThat(returnedQuestionnairePhase.getQuestions(), is(empty()));
    }

    @Test
    void findAllPhasesByTrainingDefinitionId() throws Exception {
        questionnairePhase.setOrder(1);
        trainingPhase.setOrder(2);
        entityManager.persist(infoPhase);
        entityManager.persist(questionnairePhase);
        entityManager.persist(trainingPhase);

        MockHttpServletResponse response = mvc.perform(get(URIPath.PHASES.getUri(), trainingDefinition.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        List<AbstractPhaseDTO> phasesDTO = convertJsonToObject(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(phasesDTO, notNullValue());
        assertThat(phasesDTO, hasSize(3));
        assertThat(phasesDTO.get(0), notNullValue());
        assertThat(phasesDTO.get(0).getPhaseType(), is(PhaseType.INFO));
        assertThat(phasesDTO.get(0).getId(), is(infoPhase.getId()));
        assertThat(phasesDTO.get(1), notNullValue());
        assertThat(phasesDTO.get(1).getPhaseType(), is(PhaseType.QUESTIONNAIRE));
        assertThat(phasesDTO.get(1).getId(), is(questionnairePhase.getId()));
        assertThat(phasesDTO.get(2), notNullValue());
        assertThat(phasesDTO.get(2).getPhaseType(), is(PhaseType.TRAINING));
        assertThat(phasesDTO.get(2).getId(), is(trainingPhase.getId()));
    }

    @Test
    void updateInfoPhase() throws Exception {
        entityManager.persist(infoPhase);
        InfoPhaseUpdateDTO infoPhaseUpdateDTO = testDataFactory.getInfoPhaseUpdateDTO();
        infoPhaseUpdateDTO.setId(infoPhase.getId());

        MockHttpServletResponse response = mvc.perform(put(URIPath.PHASES_ID_INFO.getUri(), trainingDefinition.getId(), infoPhase.getId())
                .content(convertObjectToJson(infoPhaseUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        InfoPhaseDTO updatedInfoPhase = convertJsonToObject(response.getContentAsString(), InfoPhaseDTO.class);
        assertThat(updatedInfoPhase, notNullValue());
        assertThat(updatedInfoPhase.getTitle(), is(infoPhaseUpdateDTO.getTitle()));
        assertThat(updatedInfoPhase.getContent(), is(infoPhaseUpdateDTO.getContent()));
        assertThat(updatedInfoPhase.getOrder(), is(0));
        assertThat(updatedInfoPhase.getPhaseType(), is(PhaseType.INFO));
    }

    @Test
    void updateTrainingPhase() throws Exception {
        entityManager.persist(trainingPhase);
        TrainingPhaseUpdateDTO trainingPhaseUpdateDTO = testDataFactory.getTrainingPhaseUpdateDTO();
        final DecisionMatrixRowDTO updatedDecisionMatrixRow = testDataFactory.getDecisionMatrixRowDTO1();
        trainingPhaseUpdateDTO.setDecisionMatrix(List.of(updatedDecisionMatrixRow));
        trainingPhaseUpdateDTO.setId(trainingPhase.getId());

        MockHttpServletResponse response = mvc.perform(put(URIPath.PHASES_ID_TRAINING.getUri(), trainingDefinition.getId(), trainingPhase.getId())
                .content(convertObjectToJson(trainingPhaseUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        TrainingPhaseDTO updatedTrainingPhase = convertJsonToObject(response.getContentAsString(), TrainingPhaseDTO.class);
        assertThat(updatedTrainingPhase, notNullValue());
        assertThat(updatedTrainingPhase.getTitle(), is(trainingPhaseUpdateDTO.getTitle()));
        assertThat(updatedTrainingPhase.getAllowedCommands(), is(trainingPhaseUpdateDTO.getAllowedCommands()));
        assertThat(updatedTrainingPhase.getAllowedWrongAnswers(), is(trainingPhaseUpdateDTO.getAllowedWrongAnswers()));
        assertThat(updatedTrainingPhase.getEstimatedDuration(), is(trainingPhaseUpdateDTO.getEstimatedDuration()));
        assertThat(updatedTrainingPhase.getOrder(), is(0));
        assertThat(updatedTrainingPhase.getPhaseType(), is(PhaseType.TRAINING));
        assertThat(updatedTrainingPhase.getDecisionMatrix(), hasSize(1));
        assertThat(updatedTrainingPhase.getDecisionMatrix().get(0).getCompletedInTime(), is(updatedDecisionMatrixRow.getCompletedInTime()));
        assertThat(updatedTrainingPhase.getDecisionMatrix().get(0).getKeywordUsed(), is(updatedDecisionMatrixRow.getKeywordUsed()));
        assertThat(updatedTrainingPhase.getDecisionMatrix().get(0).getQuestionnaireAnswered(), is(updatedDecisionMatrixRow.getQuestionnaireAnswered()));
        assertThat(updatedTrainingPhase.getDecisionMatrix().get(0).getSolutionDisplayed(), is(updatedDecisionMatrixRow.getSolutionDisplayed()));
        assertThat(updatedTrainingPhase.getDecisionMatrix().get(0).getWrongAnswers(), is(updatedDecisionMatrixRow.getWrongAnswers()));
        assertThat(updatedTrainingPhase.getDecisionMatrix().get(0).getOrder(), is(0));
    }

    @Test
    public void updateTrainingPhaseAddNewMitreTechniques() throws Exception {
        entityManager.persist(trainingDefinition);
        entityManager.persist(trainingPhase);
        trainingPhase.setTrainingDefinition(trainingDefinition);

        TrainingPhaseUpdateDTO trainingPhaseUpdateDTO = testDataFactory.getTrainingPhaseUpdateDTO();
        trainingPhaseUpdateDTO.setId(trainingPhase.getId());
        trainingPhaseUpdateDTO.setMitreTechniques(List.of(mitreTechniqueDTO1, mitreTechniqueDTO2));
        final DecisionMatrixRowDTO updatedDecisionMatrixRow = testDataFactory.getDecisionMatrixRowDTO1();
        trainingPhaseUpdateDTO.setDecisionMatrix(List.of(updatedDecisionMatrixRow));

        mvc.perform(put(URIPath.PHASES_ID_TRAINING.getUri(), trainingDefinition.getId(), trainingPhase.getId())
                .content(convertObjectToJson(trainingPhaseUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        Optional<TrainingPhase> updatedTrainingPhase = trainingPhaseRepository.findById(trainingPhase.getId());
        assertThat(updatedTrainingPhase.isPresent(), is(true));
        assertThat(updatedTrainingPhase.get().getMitreTechniques(), hasSize(2));
    }

    @Test
    public void updateTrainingPhaseUpdateMitreTechniques() throws Exception {
        entityManager.persist(trainingDefinition);
        trainingPhase.addMitreTechnique(mitreTechnique1);
        trainingPhase.addMitreTechnique(mitreTechnique2);
        entityManager.persist(trainingPhase);
        trainingPhase.setTrainingDefinition(trainingDefinition);

        TrainingPhaseUpdateDTO trainingPhaseUpdateDTO = testDataFactory.getTrainingPhaseUpdateDTO();
        trainingPhaseUpdateDTO.setId(trainingPhase.getId());

        MitreTechniqueDTO mitreTechniqueDTO = new MitreTechniqueDTO();
        mitreTechniqueDTO.setId(mitreTechnique1.getId());
        mitreTechniqueDTO.setTechniqueKey(mitreTechnique1.getTechniqueKey());
        trainingPhaseUpdateDTO.setMitreTechniques(List.of(mitreTechniqueDTO1, mitreTechniqueDTO2, mitreTechniqueDTO));
        final DecisionMatrixRowDTO updatedDecisionMatrixRow = testDataFactory.getDecisionMatrixRowDTO1();
        trainingPhaseUpdateDTO.setDecisionMatrix(List.of(updatedDecisionMatrixRow));

        mvc.perform(put(URIPath.PHASES_ID_TRAINING.getUri(), trainingDefinition.getId(), trainingPhase.getId())
                .content(convertObjectToJson(trainingPhaseUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Optional<TrainingPhase> updatedTrainingPhase = trainingPhaseRepository.findById(trainingPhase.getId());
        assertThat(updatedTrainingPhase.isPresent(), is(true));
        assertThat(updatedTrainingPhase.get().getMitreTechniques(), hasSize(3));
        assertThat(updatedTrainingPhase.get().getMitreTechniques(), not(contains(mitreTechnique2)));
        assertThat(mitreTechnique2.getTrainingPhases(), empty());
    }

    @Test
    public void updateTrainingPhaseRemoveAllMitreTechniques() throws Exception {
        entityManager.persist(trainingDefinition);
        trainingPhase.addMitreTechnique(mitreTechnique1);
        trainingPhase.addMitreTechnique(mitreTechnique2);
        entityManager.persist(trainingPhase);
        trainingPhase.setTrainingDefinition(trainingDefinition);

        TrainingPhaseUpdateDTO trainingPhaseUpdateDTO = testDataFactory.getTrainingPhaseUpdateDTO();
        trainingPhaseUpdateDTO.setId(trainingPhase.getId());
        final DecisionMatrixRowDTO updatedDecisionMatrixRow = testDataFactory.getDecisionMatrixRowDTO1();
        trainingPhaseUpdateDTO.setDecisionMatrix(List.of(updatedDecisionMatrixRow));

        mvc.perform(put(URIPath.PHASES_ID_TRAINING.getUri(), trainingDefinition.getId(), trainingPhase.getId())
                .content(convertObjectToJson(trainingPhaseUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        Optional<TrainingPhase> updatedTrainingPhase = trainingPhaseRepository.findById(trainingPhase.getId());
        assertThat(updatedTrainingPhase.isPresent(), is(true));
        assertThat(updatedTrainingPhase.get().getMitreTechniques(), hasSize(0));
        assertThat(updatedTrainingPhase.get().getMitreTechniques(), not(contains(mitreTechnique1)));
        assertThat(updatedTrainingPhase.get().getMitreTechniques(), not(contains(mitreTechnique2)));
        assertThat(mitreTechnique1.getTrainingPhases(), empty());
        assertThat(mitreTechnique2.getTrainingPhases(), empty());
    }

    @Test
    void updateQuestionnairePhase() throws Exception {
        entityManager.persist(questionnairePhase);
        QuestionnaireUpdateDTO questionnaireUpdateDTO = testDataFactory.getQuestionnaireUpdateDTO();
        QuestionDTO freeFormQuestion = testDataFactory.getFreeFormQuestionDTO();
        freeFormQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoiceDTO()));
        QuestionDTO multipleChoiceQuestion = testDataFactory.getMultipleChoiceQuestionDTO();
        multipleChoiceQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoiceDTO(), testDataFactory.getIncorrectQuestionChoiceDTO()));
        QuestionDTO ratingFormQuestion = testDataFactory.getRatingFormQuestionDTO();
        ratingFormQuestion.setChoices(List.of(testDataFactory.getCorrectQuestionChoiceDTO(), testDataFactory.getIncorrectQuestionChoiceDTO()));
        questionnaireUpdateDTO.setQuestions(List.of(freeFormQuestion, multipleChoiceQuestion, ratingFormQuestion));
        questionnaireUpdateDTO.setId(questionnairePhase.getId());

        MockHttpServletResponse response = mvc.perform(put(URIPath.PHASES_ID_QUESTIONNAIRE.getUri(), trainingDefinition.getId(), questionnairePhase.getId())
                .content(convertObjectToJson(questionnaireUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        QuestionnairePhaseDTO updatedQuestionnairePhase = convertJsonToObject(response.getContentAsString(), QuestionnairePhaseDTO.class);
        assertThat(updatedQuestionnairePhase, notNullValue());
        assertThat(updatedQuestionnairePhase.getTitle(), is(updatedQuestionnairePhase.getTitle()));
        assertThat(updatedQuestionnairePhase.getOrder(), is(0));
        assertThat(updatedQuestionnairePhase.getPhaseType(), is(PhaseType.QUESTIONNAIRE));
        assertThat(updatedQuestionnairePhase.getQuestionnaireType(), is(QuestionnaireType.ADAPTIVE));
        assertThat(updatedQuestionnairePhase.getQuestions(), hasSize(3));

        QuestionDTO freeFormQuestionUpdated = findQuestionByQuestionType(updatedQuestionnairePhase.getQuestions(), QuestionType.FFQ);
        assertThat(freeFormQuestionUpdated, notNullValue());
        assertThat(freeFormQuestionUpdated.getId(), notNullValue());
        assertThat(freeFormQuestionUpdated.getOrder(), is(freeFormQuestion.getOrder()));
        assertThat(freeFormQuestionUpdated.getText(), is(freeFormQuestion.getText()));
        assertThat(freeFormQuestionUpdated.getQuestionType(), is(freeFormQuestion.getQuestionType()));
        assertThat(freeFormQuestionUpdated.getChoices(), hasSize(1));
        assertThat(freeFormQuestionUpdated.getChoices().get(0).getId(), notNullValue());
        assertThat(freeFormQuestionUpdated.getChoices().get(0).getText(), is(freeFormQuestion.getChoices().get(0).getText()));
        assertThat(freeFormQuestionUpdated.getChoices().get(0).getOrder(), is(freeFormQuestion.getChoices().get(0).getOrder()));
        assertThat(freeFormQuestionUpdated.getChoices().get(0).isCorrect(), is(freeFormQuestion.getChoices().get(0).isCorrect()));

        QuestionDTO multipleChoiceQuestionUpdated = findQuestionByQuestionType(updatedQuestionnairePhase.getQuestions(), QuestionType.MCQ);
        assertThat(multipleChoiceQuestionUpdated, notNullValue());
        assertThat(multipleChoiceQuestionUpdated.getId(), notNullValue());
        assertThat(multipleChoiceQuestionUpdated.getOrder(), is(multipleChoiceQuestion.getOrder()));
        assertThat(multipleChoiceQuestionUpdated.getText(), is(multipleChoiceQuestion.getText()));
        assertThat(multipleChoiceQuestionUpdated.getQuestionType(), is(multipleChoiceQuestion.getQuestionType()));
        assertThat(multipleChoiceQuestionUpdated.getChoices(), hasSize(2));
        QuestionChoiceDTO correctChoice = findQuestionChoiceByText(multipleChoiceQuestionUpdated.getChoices(), CORRECT_QUESTION_CHOICE);
        assertThat(correctChoice.getId(), notNullValue());
        assertThat(correctChoice.getText(), is(CORRECT_QUESTION_CHOICE));
        assertThat(correctChoice.getOrder(), is(0));
        assertThat(correctChoice.isCorrect(), is(true));
        QuestionChoiceDTO wrongChoice = findQuestionChoiceByText(multipleChoiceQuestionUpdated.getChoices(), WRONG_QUESTION_CHOICE);
        assertThat(wrongChoice.getId(), notNullValue());
        assertThat(wrongChoice.getText(), is(WRONG_QUESTION_CHOICE));
        assertThat(wrongChoice.getOrder(), is(1));
        assertThat(wrongChoice.isCorrect(), is(false));

        QuestionDTO ratingFormQuestionUpdated = findQuestionByQuestionType(updatedQuestionnairePhase.getQuestions(), QuestionType.RFQ);
        assertThat(ratingFormQuestionUpdated, notNullValue());
        assertThat(ratingFormQuestionUpdated.getId(), notNullValue());
        assertThat(ratingFormQuestionUpdated.getOrder(), is(ratingFormQuestion.getOrder()));
        assertThat(ratingFormQuestionUpdated.getText(), is(ratingFormQuestion.getText()));
        assertThat(ratingFormQuestionUpdated.getQuestionType(), is(ratingFormQuestion.getQuestionType()));
        assertThat(ratingFormQuestionUpdated.getChoices(), hasSize(2));
        QuestionChoiceDTO correctChoiceRFQ = findQuestionChoiceByText(ratingFormQuestionUpdated.getChoices(), CORRECT_QUESTION_CHOICE);
        assertThat(correctChoiceRFQ.getId(), notNullValue());
        assertThat(correctChoiceRFQ.getText(), is(CORRECT_QUESTION_CHOICE));
        assertThat(correctChoiceRFQ.getOrder(), is(0));
        assertThat(correctChoiceRFQ.isCorrect(), is(true));
        QuestionChoiceDTO wrongChoiceRFQ = findQuestionChoiceByText(ratingFormQuestionUpdated.getChoices(), WRONG_QUESTION_CHOICE);
        assertThat(wrongChoiceRFQ.getId(), notNullValue());
        assertThat(wrongChoiceRFQ.getText(), is(WRONG_QUESTION_CHOICE));
        assertThat(wrongChoiceRFQ.getOrder(), is(1));
        assertThat(wrongChoiceRFQ.isCorrect(), is(false));
    }

    @Test
    void deleteInfoPhase() throws Exception {
        entityManager.persist(infoPhase);
        callRestForPhaseDeletion(infoPhase.getId());
        findPhaseByIdAndExpect404(infoPhase.getId());
        assertThat(infoPhaseRepository.findById(infoPhase.getId()).isPresent(), is(false));
    }

    @Test
    void deleteTrainingPhase() throws Exception {
        entityManager.persist(trainingPhase);
        callRestForPhaseDeletion(trainingPhase.getId());
        findPhaseByIdAndExpect404(trainingPhase.getId());
        assertThat(trainingPhaseRepository.findById(trainingPhase.getId()).isPresent(), is(false));
    }

    @Test
    void deleteQuestionnairePhase() throws Exception {
        entityManager.persist(questionnairePhase);
        callRestForPhaseDeletion(questionnairePhase.getId());
        findPhaseByIdAndExpect404(questionnairePhase.getId());
        assertThat(questionnairePhaseRepository.findById(questionnairePhase.getId()).isPresent(), is(false));
    }

    @Disabled("There are ongoing problems with transactions in junit env. Can be used for debugging though")
    @Test
    void moveTrainingPhase() throws Exception {
        TrainingPhaseDTO trainingPhaseDTO1 = createPhaseAndDeserializeResponse(PhaseType.TRAINING, TrainingPhaseDTO.class);
        TrainingPhaseDTO trainingPhaseDTO2 = createPhaseAndDeserializeResponse(PhaseType.TRAINING, TrainingPhaseDTO.class);
        TrainingPhaseDTO trainingPhaseDTO3 = createPhaseAndDeserializeResponse(PhaseType.TRAINING, TrainingPhaseDTO.class);

        assertThat(trainingPhaseDTO1.getOrder(), is(0));
        assertThat(trainingPhaseDTO1.getDecisionMatrix(), hasSize(1));
        assertThat(trainingPhaseDTO2.getOrder(), is(1));
        assertThat(trainingPhaseDTO2.getDecisionMatrix(), hasSize(2));
        assertThat(trainingPhaseDTO3.getOrder(), is(2));
        assertThat(trainingPhaseDTO3.getDecisionMatrix(), hasSize(3));

        mvc.perform(put(URIPath.PHASES_ID_MOVE_TO.getUri(), trainingDefinition.getId(), trainingPhaseDTO3.getId(), trainingPhaseDTO1.getOrder())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        TrainingPhaseDTO returnedTrainingPhase1 = findPhaseByIdAndDeserializeResponse(trainingPhaseDTO1.getId(), TrainingPhaseDTO.class);
        TrainingPhaseDTO returnedTrainingPhase2 = findPhaseByIdAndDeserializeResponse(trainingPhaseDTO2.getId(), TrainingPhaseDTO.class);
        TrainingPhaseDTO returnedTrainingPhase3 = findPhaseByIdAndDeserializeResponse(trainingPhaseDTO3.getId(), TrainingPhaseDTO.class);

        assertThat(returnedTrainingPhase1, notNullValue());
        assertThat(returnedTrainingPhase1.getOrder(), is(1));
        assertThat(returnedTrainingPhase1.getDecisionMatrix(), hasSize(2));
        assertThat(returnedTrainingPhase2, notNullValue());
        assertThat(returnedTrainingPhase2.getOrder(), is(2));
        assertThat(returnedTrainingPhase2.getDecisionMatrix(), hasSize(3));
        assertThat(returnedTrainingPhase3, notNullValue());
        assertThat(returnedTrainingPhase3.getOrder(), is(0));
        assertThat(returnedTrainingPhase3.getDecisionMatrix(), hasSize(1));
    }

    private MockHttpServletResponse callRestForPhaseCreation(PhaseCreateDTO phaseCreateDTO) throws Exception {
        return mvc.perform(post(URIPath.PHASES.getUri(), trainingDefinition.getId())
                .content(convertObjectToJson(phaseCreateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
    }

    private <T> T createPhaseAndDeserializeResponse(PhaseType phaseType, Class<T> returnType) throws Exception {
        PhaseCreateDTO phaseCreateDTO = new PhaseCreateDTO();
        phaseCreateDTO.setPhaseType(phaseType);
        MockHttpServletResponse response = callRestForPhaseCreation(phaseCreateDTO);
        return convertJsonToObject(response.getContentAsString(), returnType);
    }

    private QuestionnairePhaseDTO createQuestionnairePhaseAndDeserializeResponse(QuestionnaireType questionnaireType) throws Exception {
        PhaseCreateDTO phaseCreateDTO = new PhaseCreateDTO();
        phaseCreateDTO.setPhaseType(PhaseType.QUESTIONNAIRE);
        phaseCreateDTO.setQuestionnaireType(questionnaireType);
        MockHttpServletResponse response = callRestForPhaseCreation(phaseCreateDTO);
        return convertJsonToObject(response.getContentAsString(), QuestionnairePhaseDTO.class);
    }

    private <T> T findPhaseByIdAndDeserializeResponse(Long phaseId, Class<T> returnType) throws Exception {
        MockHttpServletResponse response = mvc.perform(get(URIPath.PHASES_ID.getUri(), trainingDefinition.getId(), phaseId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        return convertJsonToObject(response.getContentAsString(), returnType);
    }

    private void callRestForPhaseDeletion(Long phaseId) throws Exception {
        mvc.perform(delete(URIPath.PHASES_ID.getUri(), trainingDefinition.getId(), phaseId)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    private void findPhaseByIdAndExpect404(Long phaseId) throws Exception {
        mvc.perform(get(URIPath.PHASES_ID.getUri(), trainingDefinition.getId(), phaseId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private QuestionDTO findQuestionByQuestionType(List<QuestionDTO> questions, QuestionType questionType) {
        return questions.stream()
                .filter(q -> q.getQuestionType().equals(questionType))
                .findFirst()
                .orElse(null);
    }

    private QuestionChoiceDTO findQuestionChoiceByText(List<QuestionChoiceDTO> choices, String text) {
        return choices.stream()
                .filter(c -> text.equals(c.getText()))
                .findFirst()
                .orElse(null);
    }
}
