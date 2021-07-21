package cz.muni.ics.kypo.training.adaptive.facade;

import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalRO;
import cz.muni.ics.kypo.training.adaptive.annotations.transactions.TransactionalWO;
import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.dto.BasicPhaseInfoDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskCopyDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityConflictException;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.mapping.TaskMapper;
import cz.muni.ics.kypo.training.adaptive.service.phases.PhaseService;
import cz.muni.ics.kypo.training.adaptive.service.phases.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;


@Service
@Transactional
public class TaskFacade {

    private final TaskService taskService;
    private final PhaseService phaseService;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskFacade(TaskService taskService,
                      PhaseService phaseService,
                      TaskMapper taskMapper) {
        this.taskService = taskService;
        this.phaseService = phaseService;
        this.taskMapper = taskMapper;
    }

    /**
     * Creates new task in phase
     *
     * @param phaseId - id of phase in which task will be created
     * @return {@link BasicPhaseInfoDTO} of new questionnaire phase
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public TaskDTO createDefaultTask(Long phaseId) {
        AbstractPhase trainingPhase = phaseService.getPhase(phaseId);
        if (!(trainingPhase instanceof TrainingPhase)) {
            throw new EntityConflictException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, "The specified phase isn't training phase."));
        }
        return taskMapper.mapToTaskDTO(this.taskService.createDefaultTask((TrainingPhase) trainingPhase));
    }

    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenPhase(#phaseId)")
    @TransactionalWO
    public TaskDTO createTask(Long phaseId, TaskCopyDTO taskCopyDTO) {
        AbstractPhase trainingPhase = this.phaseService.getPhase(phaseId);
        if (!(trainingPhase instanceof TrainingPhase)) {
            throw new EntityConflictException(new EntityErrorDetail(AbstractPhase.class, "id", phaseId.getClass(), phaseId, "The specified phase isn't training phase."));
        }
        Task taskToCreate = this.taskMapper.mapToEntity(taskCopyDTO);
        taskToCreate.setTrainingPhase((TrainingPhase) trainingPhase);
        return this.taskMapper.mapToTaskDTO(this.taskService.createTask(taskToCreate));
    }

    /**
     * Finds specific task by id
     *
     * @param taskId - id of wanted task
     * @return wanted {@link TaskDTO}
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
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
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskId)")
    @TransactionalWO
    public TaskDTO updateTask(Long taskId, TaskUpdateDTO taskUpdateDto) {
        return this.taskMapper.mapToTaskDTO(this.taskService.updateTask(taskId, this.taskMapper.mapToEntity(taskUpdateDto)));
    }

    /**
     * Deletes specific task by id
     *
     * @param taskId - id of task to be deleted
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskId)")
    @TransactionalWO
    public void removeTask(Long taskId) {
        Task taskToRemove = this.taskService.getTask(taskId);
        this.taskService.removeTask(taskToRemove);
    }

    /**
     * Move task to the different position and modify orders of task between moved task and new position.
     *
     * @param taskIdFrom  - id of the task to be moved to the new position
     * @param newPosition - position where task will be moved
     */
    @PreAuthorize("hasAuthority(T(cz.muni.ics.kypo.training.adaptive.enums.RoleTypeSecurity).ROLE_ADAPTIVE_TRAINING_ADMINISTRATOR)" +
            "or @securityService.isDesignerOfGivenTask(#taskIdFrom)")
    @TransactionalWO
    public void moveTaskToSpecifiedOrder(Long taskIdFrom, int newPosition) {
        taskService.moveTaskToSpecifiedOrder(taskIdFrom, newPosition);
    }
}
