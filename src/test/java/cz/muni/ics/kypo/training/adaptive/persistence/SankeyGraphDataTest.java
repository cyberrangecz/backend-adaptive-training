package cz.muni.ics.kypo.training.adaptive.persistence;

import cz.muni.ics.kypo.training.adaptive.config.PersistenceConfigTest;
import cz.muni.ics.kypo.training.adaptive.domain.ParticipantTaskAssignment;
import cz.muni.ics.kypo.training.adaptive.domain.User;
import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingDefinition;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingInstance;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.NodeDTO;
import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.PreProcessLink;
import cz.muni.ics.kypo.training.adaptive.enums.TRState;
import cz.muni.ics.kypo.training.adaptive.repository.ParticipantTaskAssignmentRepository;
import cz.muni.ics.kypo.training.adaptive.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@Import(PersistenceConfigTest.class)
@ComponentScan(basePackages = "cz.muni.ics.kypo.training.adaptive.util")
public class SankeyGraphDataTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private ParticipantTaskAssignmentRepository participantTaskAssignmentRepository;

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

    private Pageable pageable;

    @SpringBootApplication
    static class TestConfiguration {
    }

    @BeforeEach
    public void init() {
        trainingDefinition = testDataFactory.getReleasedDefinition();
        trainingInstance = testDataFactory.getOngoingInstance();

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
        trainingRun1.setCurrentTask(task31);

        trainingRun2 = testDataFactory.getRunningRun();
        trainingRun2.setParticipantRef(participant2);
        trainingRun2.setCurrentPhase(trainingPhase3);
        trainingRun2.setTrainingInstance(trainingInstance);
        trainingRun2.setCurrentTask(task33);

        trainingRun3 = testDataFactory.getRunningRun();
        trainingRun3.setParticipantRef(participant3);
        trainingRun3.setCurrentPhase(trainingPhase3);
        trainingRun3.setTrainingInstance(trainingInstance);
        trainingRun3.setCurrentTask(task32);

        trainingRun4 = testDataFactory.getRunningRun();
        trainingRun4.setParticipantRef(participant4);
        trainingRun4.setCurrentPhase(trainingPhase3);
        trainingRun4.setTrainingInstance(trainingInstance);
        trainingRun4.setCurrentTask(task33);

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

        pageable = PageRequest.of(0, 10);
    }

    @Test
    public void testNodes() {
        List<NodeDTO> nodes = this.participantTaskAssignmentRepository.findAllVisitedTasks(trainingInstance.getId());
        List<ParticipantTaskAssignment> assignments = this.participantTaskAssignmentRepository.findAllByTrainingRunTrainingInstanceId(trainingInstance.getId());
        List<NodeDTO> sortedNodes = assignments.stream()
                .sorted((assignment1, assignment2) -> {
                    int result = assignment1.getAbstractPhase().getOrder().compareTo(assignment2.getAbstractPhase().getOrder());
                    if (result == 0) {
                        return assignment1.getTask().getOrder().compareTo(assignment2.getTask().getOrder());
                    }
                    return result;
                })
                .map(this::mapToNodeDTO)
                .distinct()
                .collect(Collectors.toList());
        assertEquals(sortedNodes, nodes);
    }

    @Test
    public void testLinks() {
        List<PreProcessLink> links = this.participantTaskAssignmentRepository.findTaskTransitionsBetweenTwoPhases(trainingDefinition.getId(),
                trainingInstance.getId(), trainingPhase1.getId(), trainingPhase2.getId());
        links.addAll(this.participantTaskAssignmentRepository.findTaskTransitionsBetweenTwoPhases(trainingDefinition.getId(),
                trainingInstance.getId(), trainingPhase2.getId(), trainingPhase3.getId()));
        List<PreProcessLink> expectedLinks = List.of(
                new PreProcessLink(null,null, task11.getId(), task22.getId(), trainingPhase1.getOrder(), trainingPhase2.getOrder(), 1L),
                new PreProcessLink(null,null, task12.getId(), task21.getId(), trainingPhase1.getOrder(), trainingPhase2.getOrder(), 2L),
                new PreProcessLink(null,null, task13.getId(), task23.getId(), trainingPhase1.getOrder(), trainingPhase2.getOrder(), 1L),
                new PreProcessLink(4,7, task21.getId(), task31.getId(), trainingPhase2.getOrder(), trainingPhase3.getOrder(), 1L),
                new PreProcessLink(4,9, task21.getId(), task33.getId(), trainingPhase2.getOrder(), trainingPhase3.getOrder(), 1L),
                new PreProcessLink(5,7, task22.getId(), task31.getId(), trainingPhase2.getOrder(), trainingPhase3.getOrder(), 1L),
                new PreProcessLink(6,8, task23.getId(), task32.getId(), trainingPhase2.getOrder(), trainingPhase3.getOrder(), 1L)
        );
        assertTrue(links.containsAll(expectedLinks));
    }

    @Test
    public void testNumberOfParticipantsInTasks() {
        trainingRun1.setState(TRState.FINISHED);
        entityManager.persist(trainingRun1);
        Map<Long, Long> mapping = this.participantTaskAssignmentRepository.findNumberOfParticipantsInTasksOfPhase(trainingPhase3.getId());
        assertEquals(2, mapping.size());
        assertEquals(new HashMap<>(Map.of(task32.getId(),1L, task33.getId(), 2L)), mapping);
    }

    private NodeDTO mapToNodeDTO(ParticipantTaskAssignment participantTaskAssignment) {
        NodeDTO nodeDTO = new NodeDTO();
        nodeDTO.setTaskId(participantTaskAssignment.getTask().getId());
        nodeDTO.setTaskOrder(participantTaskAssignment.getTask().getOrder());
        nodeDTO.setTaskTitle(participantTaskAssignment.getTask().getTitle());
        nodeDTO.setPhaseId(participantTaskAssignment.getAbstractPhase().getId());
        nodeDTO.setPhaseOrder(participantTaskAssignment.getAbstractPhase().getOrder());
        nodeDTO.setPhaseTitle(participantTaskAssignment.getAbstractPhase().getTitle());
        return nodeDTO;
    }

    private ParticipantTaskAssignment getNewParticipantTaskAssignment(TrainingPhase trainingPhase, Task task, TrainingRun trainingRun) {
        ParticipantTaskAssignment participantTaskAssignment = new ParticipantTaskAssignment();
        participantTaskAssignment.setAbstractPhase(trainingPhase);
        participantTaskAssignment.setTask(task);
        participantTaskAssignment.setTrainingRun(trainingRun);
        return participantTaskAssignment;
    }
}
