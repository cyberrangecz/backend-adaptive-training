package cz.cyberrange.platform.training.adaptive.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cyberrange.platform.training.adaptive.URIPath;
import cz.cyberrange.platform.training.adaptive.util.TestDataFactory;
import cz.cyberrange.platform.training.adaptive.rest.controller.TasksController;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.UserRefDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskCopyDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskUpdateDTO;
import cz.cyberrange.platform.training.adaptive.rest.handler.CustomRestExceptionHandler;
import cz.cyberrange.platform.training.adaptive.persistence.repository.phases.TaskRepository;
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
import java.util.Optional;

import static cz.cyberrange.platform.training.adaptive.util.ObjectConverter.convertJsonToObject;
import static cz.cyberrange.platform.training.adaptive.util.ObjectConverter.convertObjectToJson;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        IntegrationTestApplication.class,
        TasksController.class,
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
class TasksControllerIT {

    private MockMvc mvc;
    @Autowired
    private TestDataFactory testDataFactory;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private TasksController tasksController;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserManagementServiceApi userManagementServiceApi;
    @Autowired
    @Qualifier("objMapperRESTApi")
    private ObjectMapper mapper;

    private TrainingDefinition trainingDefinition;
    private TrainingPhase trainingPhase;
    private Task task, task2, task3;
    private UserRefDTO designer;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(tasksController)
                .setControllerAdvice(new CustomRestExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();

        trainingDefinition = testDataFactory.getUnreleasedDefinition();
        trainingPhase = testDataFactory.getTrainingPhase1();
        trainingPhase.setOrder(0);
        trainingPhase.setTrainingDefinition(trainingDefinition);

        designer = testDataFactory.getUserRefDTO1();

        task = testDataFactory.getTask11();
        task.setTrainingPhase(trainingPhase);
        task2 = testDataFactory.getTask12();
        task2.setTrainingPhase(trainingPhase);
        task3 = testDataFactory.getTask13();
        task3.setTrainingPhase(trainingPhase);

        entityManager.persist(trainingDefinition);
        entityManager.persist(trainingPhase);

        doReturn(designer).when(userManagementServiceApi).getUserRefDTO();
    }

    @Test
    void createTask() throws Exception {
        MockHttpServletResponse response = mvc.perform(post(URIPath.TASKS.getUri(), trainingDefinition.getId(), trainingPhase.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        TaskDTO createdTask = convertJsonToObject(response.getContentAsString(), TaskDTO.class);

        assertThat(createdTask, notNullValue());
        assertThat(createdTask.getTitle(), is("Title of a new task"));
        assertThat(createdTask.getOrder(), is(0));
        assertThat(createdTask.getContent(), is("Task content ..."));
        assertThat(createdTask.getAnswer(), is("Secret flag"));
        assertThat(createdTask.getSolution(), is("Task solution ..."));
        assertThat(createdTask.getIncorrectAnswerLimit(), is(10));
        assertThat(createdTask.isModifySandbox(), is(false));
        assertThat(createdTask.getSandboxChangeExpectedDuration(), is(0));

        Optional<Task> createdTaskEntityOptional = taskRepository.findById(createdTask.getId());
        assertThat(createdTaskEntityOptional.orElse(null), notNullValue());
        Task createdTaskEntity = createdTaskEntityOptional.get();
        assertThat(createdTaskEntity.getTitle(), is("Title of a new task"));
        assertThat(createdTaskEntity.getOrder(), is(0));
        assertThat(createdTaskEntity.getContent(), is("Task content ..."));
        assertThat(createdTaskEntity.getAnswer(), is("Secret flag"));
        assertThat(createdTaskEntity.getSolution(), is("Task solution ..."));
        assertThat(createdTaskEntity.isModifySandbox(), is(false));
        assertThat(createdTaskEntity.getSandboxChangeExpectedDuration(), is(0));
    }

    @Test
    void findTaskById() throws Exception {
        entityManager.persist(task);
        TaskDTO returnedTask = findTaskByIdAndDeserializeResponse(task.getId());

        assertThat(returnedTask, notNullValue());
        assertThat(returnedTask.getTitle(), is("Task11"));
        assertThat(returnedTask.getOrder(), is(0));
        assertThat(returnedTask.getContent(), is("Content of task11"));
        assertThat(returnedTask.getAnswer(), is("answer11"));
        assertThat(returnedTask.getSolution(), is("solution11"));
        assertThat(returnedTask.getIncorrectAnswerLimit(), is(2));
        assertThat(returnedTask.isModifySandbox(), is(true));
        assertThat(returnedTask.getSandboxChangeExpectedDuration(), is(20));
    }

    @Test
    void updateTask() throws Exception {
        entityManager.persist(task);
        TaskUpdateDTO taskUpdateDTO = testDataFactory.getTaskUpdateDTO();
        taskUpdateDTO.setId(task.getId());

        MockHttpServletResponse response = mvc.perform(put(URIPath.TASKS_ID.getUri(), trainingDefinition.getId(), trainingPhase.getId(), task.getId())
                .content(convertObjectToJson(taskUpdateDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        TaskDTO updatedTask = convertJsonToObject(response.getContentAsString(), TaskDTO.class);
        assertThat(updatedTask, notNullValue());
        assertThat(updatedTask.getTitle(), is(taskUpdateDTO.getTitle()));
        assertThat(updatedTask.getOrder(), is(0));
        assertThat(updatedTask.getContent(), is(taskUpdateDTO.getContent()));
        assertThat(updatedTask.getAnswer(), is(taskUpdateDTO.getAnswer()));
        assertThat(updatedTask.getSolution(), is(taskUpdateDTO.getSolution()));
        assertThat(updatedTask.getIncorrectAnswerLimit(), is(taskUpdateDTO.getIncorrectAnswerLimit()));
        assertThat(updatedTask.isModifySandbox(), is(taskUpdateDTO.isModifySandbox()));
        assertThat(updatedTask.getSandboxChangeExpectedDuration(), is(taskUpdateDTO.getSandboxChangeExpectedDuration()));
    }

    @Test
    void deleteTask() throws Exception {
        entityManager.persist(task);
        mvc.perform(delete(URIPath.TASKS_ID.getUri(), trainingDefinition.getId(), trainingPhase.getId(), task.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        mvc.perform(get(URIPath.TASKS_ID.getUri(), trainingDefinition.getId(), trainingPhase.getId(), task.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        assertThat(taskRepository.findById(task.getId()).isPresent(), is(false));
    }

    @Test
    void cloneTask() throws Exception {
        entityManager.persist(task);
        TaskCopyDTO taskCopyDTO = testDataFactory.getTaskCopyDTO();
        MockHttpServletResponse response = mvc.perform(post(URIPath.TASKS_ID.getUri(), trainingDefinition.getId(), trainingPhase.getId(), task.getId())
                .content(convertObjectToJson(taskCopyDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        TaskDTO copiedTask = convertJsonToObject(response.getContentAsString(), TaskDTO.class);
        assertThat(copiedTask, notNullValue());
        assertThat(copiedTask.getTitle(), is(taskCopyDTO.getTitle()));
        assertThat(copiedTask.getOrder(), is(1));
        assertThat(copiedTask.getContent(), is(taskCopyDTO.getContent()));
        assertThat(copiedTask.getAnswer(), is(taskCopyDTO.getAnswer()));
        assertThat(copiedTask.getSolution(), is(taskCopyDTO.getSolution()));
        assertThat(copiedTask.getIncorrectAnswerLimit(), is(taskCopyDTO.getIncorrectAnswerLimit()));
        assertThat(copiedTask.isModifySandbox(), is(taskCopyDTO.isModifySandbox()));
        assertThat(copiedTask.getSandboxChangeExpectedDuration(), is(taskCopyDTO.getSandboxChangeExpectedDuration()));
    }

    @Disabled("There are ongoing problems with transactions in junit env. Can be used for debugging though")
    @Test
    void moveTask() throws Exception {
        entityManager.persist(task);
        entityManager.persist(task2);
        entityManager.persist(task3);

        assertThat(task.getOrder(), is(0));
        assertThat(task2.getOrder(), is(1));
        assertThat(task3.getOrder(), is(2));

        mvc.perform(put(URIPath.TASKS_ID_MOVE_TO.getUri(), trainingDefinition.getId(), trainingPhase.getId(), task3.getId(), task.getOrder())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        TaskDTO returnedTask1 = findTaskByIdAndDeserializeResponse(task.getId());
        TaskDTO returnedTask2 = findTaskByIdAndDeserializeResponse(task2.getId());
        TaskDTO returnedTask3 = findTaskByIdAndDeserializeResponse(task3.getId());

        assertThat(returnedTask1, notNullValue());
        assertThat(returnedTask1.getOrder(), is(1));
        assertThat(returnedTask2, notNullValue());
        assertThat(returnedTask2.getOrder(), is(2));
        assertThat(returnedTask3, notNullValue());
        assertThat(returnedTask3.getOrder(), is(0));
    }

    private TaskDTO findTaskByIdAndDeserializeResponse(long taskId) throws Exception {
        MockHttpServletResponse response = mvc.perform(get(URIPath.TASKS_ID.getUri(), trainingDefinition.getId(), trainingPhase.getId(), taskId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        return convertJsonToObject(response.getContentAsString(), TaskDTO.class);
    }

}
