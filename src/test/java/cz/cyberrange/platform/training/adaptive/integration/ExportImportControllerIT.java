package cz.cyberrange.platform.training.adaptive.integration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import cz.cyberrange.platform.training.adaptive.util.TestDataFactory;
import cz.cyberrange.platform.training.adaptive.rest.controller.ExportImportRestController;
import cz.cyberrange.platform.training.adaptive.persistence.entity.User;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AccessPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.DecisionMatrixRow;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.InfoPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.QuestionnairePhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.Question;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.questions.QuestionPhaseRelation;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import cz.cyberrange.platform.training.adaptive.rest.handler.CustomRestExceptionHandler;
import cz.cyberrange.platform.training.adaptive.persistence.repository.UserRefRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.AccessPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.InfoPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.QuestionnairePhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.TrainingPhaseRepository;
import cz.cyberrange.platform.training.adaptive.persistence.repository.training.TrainingDefinitionRepository;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {
		ExportImportRestController.class,
        IntegrationTestApplication.class,
		TestDataFactory.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class ExportImportControllerIT {

	@Autowired
	private TestDataFactory testDataFactory;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ExportImportRestController exportImportRestController;
	@Autowired
	private TrainingDefinitionRepository trainingDefinitionRepository;
	@Autowired
	private InfoPhaseRepository infoPhaseRepository;
	@Autowired
	private AccessPhaseRepository accessPhaseRepository;
	@Autowired
	private TrainingPhaseRepository trainingPhaseRepository;
	@Autowired
	private QuestionnairePhaseRepository questionnairePhaseRepository;
	@Autowired
	private UserRefRepository userRefRepository;
	@MockBean
	private UserManagementServiceApi userManagementServiceApi;


	private MockMvc mvc;

	private TrainingDefinition trainingDefinition;
	private InfoPhase infoPhase;
	private AccessPhase accessPhase;
	private QuestionnairePhase questionnairePhaseAdaptive, questionnairePhaseGeneral;
	private TrainingPhase trainingPhase1, trainingPhase2;
	private User user;
	private UserRefDTO userRefDTO;

	@BeforeEach
	public void init(){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
		objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

		this.mvc = MockMvcBuilders.standaloneSetup(exportImportRestController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
						new QuerydslPredicateArgumentResolver(
								new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE), Optional.empty()))
				.setMessageConverters(new MappingJackson2HttpMessageConverter(mapper), new ByteArrayHttpMessageConverter())
				.setControllerAdvice(new CustomRestExceptionHandler())
				.build();

		trainingDefinition = testDataFactory.getReleasedDefinition();

		infoPhase = testDataFactory.getInfoPhase1();
		infoPhase.setTrainingDefinition(trainingDefinition);
		infoPhase.setOrder(0);

		accessPhase = testDataFactory.getAccessPhase();
		accessPhase.setTrainingDefinition(trainingDefinition);
		accessPhase.setOrder(1);

		questionnairePhaseAdaptive = testDataFactory.getAdaptive();
		questionnairePhaseAdaptive.setOrder(2);
		questionnairePhaseAdaptive.setTrainingDefinition(trainingDefinition);

		Question mcq = testDataFactory.getMultipleChoiceQuestion();
		mcq.setOrder(0);
		mcq.setQuestionnairePhase(questionnairePhaseAdaptive);
		mcq.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));

		Question ffq = testDataFactory.getFreeFormQuestion();
		ffq.setOrder(1);
		ffq.setQuestionnairePhase(questionnairePhaseAdaptive);
		ffq.setChoices(List.of(testDataFactory.getAnotherCorrectQuestionChoice()));

		Question rfq = testDataFactory.getRatingFormQuestion();
		rfq.setOrder(2);
		rfq.setQuestionnairePhase(questionnairePhaseAdaptive);
		rfq.setChoices(List.of(testDataFactory.getCorrectQuestionChoice(), testDataFactory.getIncorrectQuestionChoice()));

		questionnairePhaseAdaptive.setQuestions(new ArrayList<>(List.of(mcq, ffq, rfq)));

		trainingPhase1 = testDataFactory.getTrainingPhase1();
		trainingPhase1.setTrainingDefinition(trainingDefinition);
		trainingPhase1.setOrder(3);
		DecisionMatrixRow decisionMatrixRow1 = new DecisionMatrixRow();
		decisionMatrixRow1.setOrder(0);
		decisionMatrixRow1.setTrainingPhase(trainingPhase1);
		trainingPhase1.setDecisionMatrix(List.of(decisionMatrixRow1));

		trainingPhase2 = testDataFactory.getTrainingPhase2();
		trainingPhase2.setTrainingDefinition(trainingDefinition);
		trainingPhase2.setOrder(4);
		DecisionMatrixRow decisionMatrixRow2 = new DecisionMatrixRow();
		decisionMatrixRow2.setOrder(0);
		decisionMatrixRow2.setTrainingPhase(trainingPhase2);
		DecisionMatrixRow decisionMatrixRow3 = new DecisionMatrixRow();
		decisionMatrixRow3.setOrder(1);
		decisionMatrixRow3.setTrainingPhase(trainingPhase2);
		trainingPhase2.setDecisionMatrix(List.of(decisionMatrixRow2, decisionMatrixRow3));

		trainingDefinitionRepository.save(trainingDefinition);
		infoPhaseRepository.save(infoPhase);
		trainingPhaseRepository.save(trainingPhase1);
		trainingPhaseRepository.save(trainingPhase2);
		accessPhaseRepository.save(accessPhase);

		QuestionPhaseRelation qprTrainingPhase1 = new QuestionPhaseRelation();
		qprTrainingPhase1.setQuestions(Set.of(rfq, mcq));
		qprTrainingPhase1.setRelatedTrainingPhase(trainingPhase1);
		qprTrainingPhase1.setOrder(0);
		qprTrainingPhase1.setQuestionnairePhase(questionnairePhaseAdaptive);
		qprTrainingPhase1.setSuccessRate(20);

		QuestionPhaseRelation qprTrainingPhase2 = new QuestionPhaseRelation();
		qprTrainingPhase2.setQuestions(Set.of(rfq, mcq));
		qprTrainingPhase2.setRelatedTrainingPhase(trainingPhase2);
		qprTrainingPhase2.setOrder(1);
		qprTrainingPhase2.setQuestionnairePhase(questionnairePhaseAdaptive);
		qprTrainingPhase2.setSuccessRate(46);

		questionnairePhaseAdaptive.setQuestionPhaseRelations(List.of(qprTrainingPhase1, qprTrainingPhase2));
		questionnairePhaseRepository.save(questionnairePhaseAdaptive);

		user = testDataFactory.getUser1();
		userRefRepository.save(user);

		userRefDTO = testDataFactory.getUserRefDTO1();
	}

	@Test
	public void exportAndImportTrainingDefinition() throws Exception{
		when(userManagementServiceApi.getLoggedInUserRefId()).thenReturn(userRefDTO.getUserRefId());
		when(userManagementServiceApi.getUserRefDTO()).thenReturn(userRefDTO);
		MockHttpServletResponse response = mvc.perform(get("/exports/training-definitions/{id}", trainingDefinition.getId()))
				.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
				.andExpect(status().isOk())
				.andReturn().getResponse();
		mvc.perform(post("/imports/training-definitions")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(response.getContentAsString()))
				.andExpect(status().isOk());
		assertEquals(2, questionnairePhaseRepository.findAll().size());
		assertEquals(2, accessPhaseRepository.findAll().size());
		assertEquals(2, infoPhaseRepository.findAll().size());
		assertEquals(4, trainingPhaseRepository.findAll().size());
		assertEquals(2, trainingDefinitionRepository.findAll().size());
	}
}
