package cz.cyberrange.platform.training.adaptive.rest.facade;

import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalRO;
import cz.cyberrange.platform.training.adaptive.rest.facade.annotations.transaction.TransactionalWO;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.AbstractPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.Task;
import cz.cyberrange.platform.training.adaptive.persistence.entity.phase.TrainingPhase;
import cz.cyberrange.platform.training.adaptive.persistence.entity.training.TrainingDefinition;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskCopyDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.training.TaskUpdateDTO;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityConflictException;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.EntityErrorDetail;
import cz.cyberrange.platform.training.adaptive.api.mapping.TaskMapper;
import cz.cyberrange.platform.training.adaptive.service.phases.PhaseService;
import cz.cyberrange.platform.training.adaptive.service.phases.TaskService;
import cz.cyberrange.platform.training.adaptive.service.training.TrainingDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TaskFacade {

    private final TaskService taskService;
    private final PhaseService phaseService;
    private final TrainingDefinitionService trainingDefinitionService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskFacade(TaskService taskService,
                      PhaseService phaseService,
                      TrainingDefinitionService trainingDefinitionService,
                      TaskMapper taskMapper) {
        this.taskService = taskService;
        this.phaseService = phaseService;
        this.trainingDefinitionService = trainingDefinitionService;
        this.taskMapper = taskMapper;
    }

    /**
     * Creates new task in phase
     *
     * @param phaseId - id of phase in which task will be created
     * @return {@link TaskDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public TaskDTO createDefaultTask(Long phaseId) {
        AbstractPhase trainingPhase = phaseService.getPhase(phaseId);
        trainingDefinitionService.checkIfCanBeUpdated(trainingPhase.getTrainingDefinition());
        if (!(trainingPhase instanceof TrainingPhase)) {
            throw new EntityConflictException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, "The specified phase isn't training phase."));
        }
        Task task = this.taskService.createDefaultTask((TrainingPhase) trainingPhase);
        trainingDefinitionService.auditAndSave(trainingPhase.getTrainingDefinition());
        return taskMapper.mapToTaskDTO(task);
    }

    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public TaskDTO createTask(Long phaseId, TaskCopyDTO taskCopyDTO) {
        AbstractPhase trainingPhase = this.phaseService.getPhase(phaseId);
        trainingDefinitionService.checkIfCanBeUpdated(trainingPhase.getTrainingDefinition());
        if (!(trainingPhase instanceof TrainingPhase)) {
            throw new EntityConflictException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, "The specified phase isn't training phase."));
        }
        Task taskToCreate = this.taskMapper.mapToEntity(taskCopyDTO);
        taskToCreate.setTrainingPhase((TrainingPhase) trainingPhase);
        Task createdTask = this.taskService.createTask(taskToCreate);
        trainingDefinitionService.auditAndSave(trainingPhase.getTrainingDefinition());
        return this.taskMapper.mapToTaskDTO(createdTask);
    }

    /**
     * Finds specific task by id
     *
     * @param taskId - id of wanted task
     * @return wanted {@link TaskDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskId)")
    @TransactionalRO
    public TaskDTO getTask(Long taskId) {
        return taskMapper.mapToTaskDTO(taskService.getTask(taskId));
    }

    /**
     * Updates info phase from training definition
     *
     * @param taskId        - id of task to be updated
     * @param taskUpdateDto task with the updated values
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskId)")
    @TransactionalWO
    public TaskDTO updateTask(Long taskId, TaskUpdateDTO taskUpdateDto) {
        TrainingDefinition trainingDefinition = this.taskService.getTask(taskId).getTrainingPhase().getTrainingDefinition();
        trainingDefinitionService.checkIfCanBeUpdated(trainingDefinition);
        Task updatedTask = this.taskService.updateTask(taskId, this.taskMapper.mapToEntity(taskUpdateDto));
        trainingDefinitionService.auditAndSave(updatedTask.getTrainingPhase().getTrainingDefinition());
        return this.taskMapper.mapToTaskDTO(updatedTask);
    }

    /**
     * Deletes specific task by id
     *
     * @param taskId - id of task to be deleted
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskId)")
    @TransactionalWO
    public void removeTask(Long taskId) {
        Task taskToRemove = this.taskService.getTask(taskId);
        TrainingDefinition relatedTrainingDefinition = taskToRemove.getTrainingPhase().getTrainingDefinition();
        trainingDefinitionService.checkIfCanBeUpdated(relatedTrainingDefinition);
        this.taskService.removeTask(taskToRemove);
        trainingDefinitionService.auditAndSave(relatedTrainingDefinition);
    }

    /**
     * Move task to the different position and modify orders of task between moved task and new position.
     *
     * @param taskIdFrom  - id of the task to be moved to the new position
     * @param newPosition - position where task will be moved
     */
    @PreAuthorize("hasAuthority(T(cz.cyberrange.platform.training.adaptive.persistence.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskIdFrom)")
    @TransactionalWO
    public void moveTaskToSpecifiedOrder(Long taskIdFrom, int newPosition) {
        Task task = taskService.moveTaskToSpecifiedOrder(taskIdFrom, newPosition);
        trainingDefinitionService.auditAndSave(task.getTrainingPhase().getTrainingDefinition());
    }
}
