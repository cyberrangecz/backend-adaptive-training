package cz.muni.ics.kypo.training.adaptive.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.training.adaptive.config.RestConfigTest;
import cz.muni.ics.kypo.training.adaptive.controller.VisualizationRestController;
import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.LinkDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.NodeDTO;
import cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.muni.ics.kypo.training.adaptive.enums.TRState;
import cz.muni.ics.kypo.training.adaptive.facade.VisualizationFacade;
import cz.muni.ics.kypo.training.adaptive.handler.CustomRestExceptionHandler;
import cz.muni.ics.kypo.training.adaptive.service.VisualizationService;
import cz.muni.ics.kypo.training.adaptive.service.api.UserManagementServiceApi;
import cz.muni.ics.kypo.training.adaptive.service.training.TrainingInstanceService;
import cz.muni.ics.kypo.training.adaptive.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {
        VisualizationRestController.class,
        TestDataFactory.class,
        VisualizationFacade.class,
        VisualizationService.class,
        TrainingInstanceService.class,
        UserManagementServiceApi.class
})
@DataJpaTest
@Import(RestConfigTest.class)
public class VisualizationIT {

    private MockMvc mvc;

    @Autowired
    private VisualizationRestController visualizationRestController;
    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    @Qualifier("objMapperRESTApi")
    private ObjectMapper mapper;

    private TrainingDefinition trainingDefinition;
    private TrainingInstance trainingInstance;
    private TrainingRun trainingRun1, trainingRun2, trainingRun3, trainingRun4;
    private User participant1, participant2, participant3, participant4;
    private TrainingPhase trainingPhase1, trainingPhase2, trainingPhase3;

    private ParticipantTaskAssignment participant1Task11, participant1Task22, participant1Task31;
    private ParticipantTaskAssignment participant2Task12, participant2Task21, participant2Task33;
    private ParticipantTaskAssignment participant3Task13, participant3Task23, participant3Task32;
    private ParticipantTaskAssignment participant4Task12, participant4Task21, participant4Task31;
    private Task task11, task12, task13;
    private Task task21, task22, task23;
    private  Task task31, task32, task33;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(visualizationRestController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                        new QuerydslPredicateArgumentResolver(
                                new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE), Optional.empty()))
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new CustomRestExceptionHandler())
                .build();

        trainingDefinition = testDataFactory.getReleasedDefinition();
        trainingInstance = testDataFactory.getOngoingInstance();
        trainingInstance.setTrainingDefinition(trainingDefinition);

        trainingPhase1 = testDataFactory.getTrainingPhase1();
        trainingPhase1.setTrainingDefinition(trainingDefinition);
        task11 = testDataFactory.getTask11();
        task11.setTrainingPhase(trainingPhase1);

        task12 = testDataFactory.getTask12();
        task12.setTrainingPhase(trainingPhase1);

        task13 = testDataFactory.getTask13();
        task13.setTrainingPhase(trainingPhase1);

        trainingPhase2 = testDataFactory.getTrainingPhase2();
        trainingPhase2.setTrainingDefinition(trainingDefinition);
        task21 = testDataFactory.getTask21();
        task21.setTrainingPhase(trainingPhase2);

        task22 = testDataFactory.getTask22();
        task22.setTrainingPhase(trainingPhase2);

        task23 = testDataFactory.getTask23();
        task23.setTrainingPhase(trainingPhase2);

        trainingPhase3 = testDataFactory.getTrainingPhase3();
        trainingPhase3.setTrainingDefinition(trainingDefinition);
        task31 = testDataFactory.getTask31();
        task31.setTrainingPhase(trainingPhase3);

        task32 = testDataFactory.getTask32();
        task32.setTrainingPhase(trainingPhase3);

        task33 = testDataFactory.getTask33();
        task33.setTrainingPhase(trainingPhase3);

        participant1 = testDataFactory.getUser1();
        participant2 = testDataFactory.getUser2();
        participant3 = testDataFactory.getUser3();
        participant4 = testDataFactory.getUser4();

        trainingRun1 = testDataFactory.getRunningRun();
        trainingRun1.setParticipantRef(participant1);
        trainingRun1.setCurrentPhase(trainingPhase3);
        trainingRun1.setTrainingInstance(trainingInstance);

        trainingRun2 = testDataFactory.getRunningRun();
        trainingRun2.setParticipantRef(participant2);
        trainingRun2.setCurrentPhase(trainingPhase3);
        trainingRun2.setTrainingInstance(trainingInstance);

        trainingRun3 = testDataFactory.getRunningRun();
        trainingRun3.setParticipantRef(participant3);
        trainingRun3.setCurrentPhase(trainingPhase3);
        trainingRun3.setTrainingInstance(trainingInstance);

        trainingRun4 = testDataFactory.getRunningRun();
        trainingRun4.setParticipantRef(participant4);
        trainingRun4.setCurrentPhase(trainingPhase3);
        trainingRun4.setTrainingInstance(trainingInstance);

        participant1Task11 = getNewParticipantTaskAssignment(trainingPhase1, task11, trainingRun1);
        participant1Task22 = getNewParticipantTaskAssignment(trainingPhase2, task22, trainingRun1);
        participant1Task31 = getNewParticipantTaskAssignment(trainingPhase3, task31, trainingRun1);

        participant2Task12 = getNewParticipantTaskAssignment(trainingPhase1, task12, trainingRun2);
        participant2Task21 = getNewParticipantTaskAssignment(trainingPhase2, task21, trainingRun2);
        participant2Task33 = getNewParticipantTaskAssignment(trainingPhase3, task33, trainingRun2);

        participant3Task13 = getNewParticipantTaskAssignment(trainingPhase1, task13, trainingRun3);
        participant3Task23 = getNewParticipantTaskAssignment(trainingPhase2, task23, trainingRun3);
        participant3Task32 = getNewParticipantTaskAssignment(trainingPhase3, task32, trainingRun3);

        participant4Task12 = getNewParticipantTaskAssignment(trainingPhase1, task12, trainingRun4);
        participant4Task21 = getNewParticipantTaskAssignment(trainingPhase2, task21, trainingRun4);
        participant4Task31 = getNewParticipantTaskAssignment(trainingPhase3, task31, trainingRun4);

        trainingRun1.setCurrentTask(participant1Task31.getTask());
        trainingRun2.setCurrentTask(participant2Task33.getTask());
        trainingRun3.setCurrentTask(participant3Task32.getTask());
        trainingRun4.setCurrentTask(participant4Task31.getTask());

        entityManager.persist(trainingDefinition);
        entityManager.persist(trainingInstance);
        entityManager.persist(trainingPhase1);
        entityManager.persist(trainingPhase2);
        entityManager.persist(trainingPhase3);
        entityManager.persist(task31);
        entityManager.persist(task32);
        entityManager.persist(task33);
        entityManager.persist(task21);
        entityManager.persist(task22);
        entityManager.persist(task23);
        entityManager.persist(task12);
        entityManager.persist(task13);
        entityManager.persist(task11);

        entityManager.persist(participant1);
        entityManager.persist(participant2);
        entityManager.persist(participant3);
        entityManager.persist(participant4);

        entityManager.persist(trainingPhase1);
        entityManager.persist(trainingPhase2);
        entityManager.persist(trainingPhase3);

        entityManager.persist(trainingRun1);
        entityManager.persist(trainingRun2);
        entityManager.persist(trainingRun3);
        entityManager.persist(trainingRun4);
    }

    @Test
    public void getDataForSankeyDiagramAllFinished() throws Exception {
        entityManager.persist(participant1Task11);
        entityManager.persist(participant1Task22);
        entityManager.persist(participant1Task31);

        entityManager.persist(participant2Task12);
        entityManager.persist(participant2Task21);
        entityManager.persist(participant2Task33);

        entityManager.persist(participant3Task13);
        entityManager.persist(participant3Task23);
        entityManager.persist(participant3Task32);

        entityManager.persist(participant4Task12);
        entityManager.persist(participant4Task21);
        entityManager.persist(participant4Task31);

        trainingRun1.setState(TRState.FINISHED);
        trainingRun2.setState(TRState.FINISHED);
        trainingRun3.setState(TRState.FINISHED);
        trainingRun4.setState(TRState.FINISHED);
        entityManager.persist(trainingRun1);
        entityManager.persist(trainingRun2);
        entityManager.persist(trainingRun3);
        entityManager.persist(trainingRun4);
        MockHttpServletResponse result =  mvc.perform(get("/visualizations/training-instances/{instanceId}/sankey", trainingInstance.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        SankeyDiagramDTO sankeyData = mapper.readValue(result.getContentAsString(), SankeyDiagramDTO.class);
        NodeDTO startNode = new NodeDTO(null, null, null, null, -1, null);
        NodeDTO finishNode = new NodeDTO(null, null, null, null, -2, null);
        List<LinkDTO> linksFromStartToFirstPhase = List.of(
                new LinkDTO(0, 1, 1L),
                new LinkDTO(0, 2, 2L),
                new LinkDTO(0, 3, 1L)
        );
        List<LinkDTO> linksFromLastPhaseToFinish = List.of(
                new LinkDTO(7, 10, 2L),
                new LinkDTO(8, 10, 1L),
                new LinkDTO(9, 10, 1L)
        );
        assertTrue(sankeyData.getNodes().contains(startNode));
        assertTrue(sankeyData.getNodes().contains(finishNode));
        assertEquals(0, sankeyData.getNodes().indexOf(startNode));
        assertEquals(10, sankeyData.getNodes().indexOf(finishNode));
        assertTrue(sankeyData.getLinks().containsAll(linksFromStartToFirstPhase));
        assertTrue(sankeyData.getLinks().containsAll(linksFromLastPhaseToFinish));
    }

    @Test
    public void getDataForSankeyDiagramOneFinished() throws Exception {
        entityManager.persist(participant1Task11);
        entityManager.persist(participant1Task22);
        entityManager.persist(participant1Task31);

        entityManager.persist(participant2Task12);
        entityManager.persist(participant2Task21);
        participant2Task12.getTrainingRun().setCurrentTask(participant2Task21.getTask());

        entityManager.persist(participant3Task13);
        entityManager.persist(participant3Task23);
        participant3Task23.getTrainingRun().setCurrentTask(participant3Task23.getTask());

        entityManager.persist(participant4Task12);
        entityManager.persist(participant4Task21);
        participant4Task21.getTrainingRun().setCurrentTask(participant4Task21.getTask());

        trainingRun1.setState(TRState.FINISHED);
        entityManager.persist(trainingRun1);
        entityManager.persist(participant2Task12.getTrainingRun());
        entityManager.persist(participant3Task23.getTrainingRun());
        MockHttpServletResponse result =  mvc.perform(get("/visualizations/training-instances/{instanceId}/sankey", trainingInstance.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        SankeyDiagramDTO sankeyData = mapper.readValue(result.getContentAsString(), SankeyDiagramDTO.class);
        NodeDTO startNode = new NodeDTO(null, null, null, null, -1, null);
        NodeDTO finishNode = new NodeDTO(null, null, null, null, -2, null);
        List<LinkDTO> linksFromStartToFirstPhase = List.of(
                new LinkDTO(0, 1, 1L),
                new LinkDTO(0, 2, 2L),
                new LinkDTO(0, 3, 1L)
        );
        List<LinkDTO> linksFromLastPhaseToFinish = List.of(
                new LinkDTO(7, 8, 1L)
        );
        assertTrue(sankeyData.getNodes().contains(startNode));
        assertTrue(sankeyData.getNodes().contains(finishNode));
        assertEquals(0, sankeyData.getNodes().indexOf(startNode));
        assertEquals(8, sankeyData.getNodes().indexOf(finishNode));
        assertTrue(sankeyData.getLinks().containsAll(linksFromStartToFirstPhase));
        assertTrue(sankeyData.getLinks().containsAll(linksFromLastPhaseToFinish));
    }

    @Test
    public void getDataForSankeyDiagramOneNotFinishedLastPhase() throws Exception {
        entityManager.persist(participant1Task11);
        entityManager.persist(participant1Task22);
        entityManager.persist(participant1Task31);

        entityManager.persist(participant2Task12);
        entityManager.persist(participant2Task21);
        participant2Task12.getTrainingRun().setCurrentTask(participant2Task21.getTask());

        entityManager.persist(participant3Task13);
        entityManager.persist(participant3Task23);
        participant3Task23.getTrainingRun().setCurrentTask(participant3Task23.getTask());

        entityManager.persist(participant4Task12);
        entityManager.persist(participant4Task21);
        entityManager.persist(participant4Task31);

        trainingRun1.setState(TRState.FINISHED);
        entityManager.persist(trainingRun1);
        entityManager.persist(participant2Task12.getTrainingRun());
        entityManager.persist(participant3Task23.getTrainingRun());
        MockHttpServletResponse result =  mvc.perform(get("/visualizations/training-instances/{instanceId}/sankey", trainingInstance.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        SankeyDiagramDTO sankeyData = mapper.readValue(result.getContentAsString(), SankeyDiagramDTO.class);
        NodeDTO startNode = new NodeDTO(null, null, null, null, -1, null);
        NodeDTO finishNode = new NodeDTO(null, null, null, null, -2, null);
        List<LinkDTO> linksFromStartToFirstPhase = List.of(
                new LinkDTO(0, 1, 1L),
                new LinkDTO(0, 2, 2L),
                new LinkDTO(0, 3, 1L)
        );
        List<LinkDTO> linksFromLastPhaseToFinish = List.of(
                new LinkDTO(7, 8, 1L)
        );
        assertTrue(sankeyData.getNodes().contains(startNode));
        assertTrue(sankeyData.getNodes().contains(finishNode));
        assertEquals(0, sankeyData.getNodes().indexOf(startNode));
        assertEquals(8, sankeyData.getNodes().indexOf(finishNode));
        assertTrue(sankeyData.getLinks().containsAll(linksFromStartToFirstPhase));
        assertTrue(sankeyData.getLinks().containsAll(linksFromLastPhaseToFinish));
    }

    private ParticipantTaskAssignment getNewParticipantTaskAssignment(TrainingPhase trainingPhase, Task task, TrainingRun trainingRun) {
        ParticipantTaskAssignment participantTaskAssignment = new ParticipantTaskAssignment();
        participantTaskAssignment.setAbstractPhase(trainingPhase);
        participantTaskAssignment.setTask(task);
        participantTaskAssignment.setTrainingRun(trainingRun);
        return participantTaskAssignment;
    }
}
