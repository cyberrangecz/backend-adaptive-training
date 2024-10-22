package cz.cyberrange.platform.training.adaptive.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.rest.facade.VisualizationFacade;
import cz.cyberrange.platform.training.adaptive.util.TestDataFactory;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingInstance;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingRun;
import cz.cyberrange.platform.training.adaptive.api.dto.CommandDTO;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityNotFoundException;
import cz.cyberrange.platform.training.adaptive.api.mapping.PhaseMapper;
import cz.cyberrange.platform.training.adaptive.service.VisualizationService;
import cz.cyberrange.platform.training.adaptive.service.api.UserManagementServiceApi;
import cz.cyberrange.platform.training.adaptive.service.phases.PhaseService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingDefinitionService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingInstanceService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingRunService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {
        TestDataFactory.class,
        ObjectMapper.class
})
class VisualizationFacadeTest {

    private VisualizationFacade visualizationFacade;

    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VisualizationService visualizationService;
    @MockBean
    private TrainingDefinitionService trainingDefinitionService;
    @MockBean
    private TrainingInstanceService trainingInstanceService;
    @MockBean
    private TrainingRunService trainingRunService;
    @MockBean
    private UserManagementServiceApi userManagementServiceApi;
    @MockBean
    private PhaseService phaseService;
    @MockBean
    private PhaseMapper phaseMapper;

    private List<Map<String, Object>> elasticCommands;
    private TrainingRun trainingRun;
    private TrainingInstance trainingInstance;
    private List<CommandDTO> expected;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        visualizationFacade = new VisualizationFacade(visualizationService, trainingDefinitionService,
                trainingInstanceService, trainingRunService, userManagementServiceApi, phaseService, phaseMapper, objectMapper);

        elasticCommands = List.of(
                Map.of("hostname","attacker","ip","10.1.26.23","timestamp_str","2022-07-21T13:16:41.435559Z","sandbox_id","1",
                        "cmd","sudo ls","pool_id","1","wd","/home/user","cmd_type","bash-command","username","user"),
                Map.of("hostname","attacker","ip","10.1.26.23","timestamp_str","2022-07-21T13:16:42.276428Z","sandbox_id","1",
                        "cmd","cat","pool_id","1","wd","/home/user","cmd_type","bash-command","username","user"),
                Map.of("hostname","attacker","ip","10.1.26.23","timestamp_str","2022-07-21T13:16:52.658178Z","sandbox_id","1",
                        "cmd","echo f > a","pool_id","1","wd","/home/user","cmd_type","bash-command","username","user"),
                Map.of("hostname","attacker","ip","10.1.26.23","timestamp_str","2022-07-21T13:17:09.732708Z","sandbox_id","1","cmd",
                        "sudo nmap -v","pool_id","1","wd","/home/user","cmd_type","bash-command","username","user")
        );

        expected = List.of(
                new CommandDTO("bash-command", "sudo ls", LocalDateTime.parse("2022-07-21T13:16:41.435559"),
                        Duration.parse("PT1.435559S"), "10.1.26.23", null),
                new CommandDTO("bash-command", "cat", LocalDateTime.parse("2022-07-21T13:16:42.276428"),
                        Duration.parse("PT2.276428S"), "10.1.26.23", null),
                new CommandDTO("bash-command", "echo", LocalDateTime.parse("2022-07-21T13:16:52.658178"),
                        Duration.parse("PT12.658178S"), "10.1.26.23", "f > a"),
                new CommandDTO("bash-command", "sudo nmap", LocalDateTime.parse("2022-07-21T13:17:09.732708"),
                        Duration.parse("PT29.732708S"), "10.1.26.23", "-v")
        );

        LocalDateTime startTime = LocalDateTime.parse("2022-07-21T13:16:40");

        trainingRun = testDataFactory.getFinishedRun();
        trainingInstance = testDataFactory.getConcludedInstance();
        trainingRun.setStartTime(startTime);
        trainingRun.setTrainingInstance(trainingInstance);
        trainingInstance.setLocalEnvironment(false);
    }

    @Test
    void getAllCommandsByTrainingRun() {
        given(trainingRunService.findById(trainingRun.getId())).willReturn(trainingRun);
        given(trainingRunService.getCommandsByTrainingRun(trainingRun.getId())).willReturn(elasticCommands);
        List<CommandDTO> received = visualizationFacade.getAllCommandsInTrainingRun(trainingRun.getId());
        assertEquals(expected.size(), received.size());
        compareCommandDTOLists(expected, received);
    }

    @Test
    void trainingRunDoesNotExist() {
        given(trainingRunService.findById(anyLong())).willThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> visualizationFacade.getAllCommandsInTrainingRun(anyLong()));
    }

    @Test
    void noCommandsFound() {
        given(trainingRunService.findById(anyLong())).willReturn(trainingRun);
        trainingInstance.setLocalEnvironment(false);
        given(trainingRunService.getCommandsByTrainingRun(trainingRun.getId())).willReturn(Collections.emptyList());
        List<CommandDTO> received = visualizationFacade.getAllCommandsInTrainingRun(anyLong());
        assertEquals(0, received.size());
    }

    private void compareCommandDTOLists(List<CommandDTO> list1, List<CommandDTO> list2) {
        if (list1.size() != list2.size()) {
            fail();
        }

        for (int i = 0; i < list1.size(); i++) {
            compareCommands(list1.get(i), list2.get(i));
        }
    }

    private void compareCommands(CommandDTO cmd1, CommandDTO cmd2) {
        assertEquals(cmd1.getCmd(), cmd2.getCmd());
        assertEquals(cmd1.getOptions(), cmd2.getOptions());
        assertEquals(cmd1.getTimestamp(), cmd2.getTimestamp());
        assertEquals(cmd1.getTrainingTime(), cmd2.getTrainingTime());
        assertEquals(cmd1.getCommandType(), cmd2.getCommandType());
        assertEquals(cmd1.getFromHostIp(), cmd2.getFromHostIp());
    }
}