package cz.muni.ics.kypo.training.adaptive.service;

import cz.muni.ics.kypo.training.adaptive.domain.Task;
import cz.muni.ics.kypo.training.adaptive.domain.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.dto.TaskDTO;
import cz.muni.ics.kypo.training.adaptive.dto.TaskUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.mapper.BeanMapper;
import cz.muni.ics.kypo.training.adaptive.repository.TaskRepository;
import cz.muni.ics.kypo.training.adaptive.repository.TrainingPhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TrainingPhaseRepository trainingPhaseRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TrainingPhaseRepository trainingPhaseRepository) {
        this.taskRepository = taskRepository;
        this.trainingPhaseRepository = trainingPhaseRepository;
    }

    public TaskDTO createDefaultTask(Long trainingDefinitionId, Long phaseId) {
        TrainingPhase trainingPhase = trainingPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Game phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId (field structure will be probably changed)

        Task task = new Task();
        task.setTitle("Title of a new task");
        task.setTrainingPhase(trainingPhase);
        task.setOrder(taskRepository.getCurrentMaxOrder(phaseId) + 1);
        task.setFlag("Secret flag");
        task.setContent("Task content ...");
        task.setSolution("Task solution ...");
        task.setIncorrectFlagLimit(1);

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public TaskDTO cloneTask(Long trainingDefinitionId, Long phaseId, Long taskId) {
        Task taskToBeCloned = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        TrainingPhase trainingPhase = trainingPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Game phase was not found"));

        // TODO add check to trainingDefinitionId (field structure will be probably changed)

        Task task = new Task();
        BeanUtils.copyProperties(taskToBeCloned, task);

        task.setId(null);
        task.setTrainingPhase(trainingPhase);
        task.setOrder(taskRepository.getCurrentMaxOrder(phaseId) + 1);

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public TaskDTO getTask(Long trainingDefinitionId, Long phaseId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        return BeanMapper.INSTANCE.toDto(task);
    }

    public TaskDTO updateTask(Long trainingDefinitionId, Long phaseId, Long taskId, TaskUpdateDTO taskUpdateDto) {
        Task taskUpdate = BeanMapper.INSTANCE.toEntity(taskUpdateDto);
        taskUpdate.setId(taskId);

        Task persistedTask = taskRepository.findById(taskUpdate.getId())
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        taskUpdate.setTrainingPhase(persistedTask.getTrainingPhase());
        taskUpdate.setOrder(persistedTask.getOrder());

        Task savedEntity = taskRepository.save(taskUpdate);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    @Transactional
    public void removeTask(Long trainingDefinitionId, Long phaseId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        taskRepository.decreaseOrderAfterTaskWasDeleted(phaseId, task.getOrder());

        taskRepository.delete(task);
    }

    @Transactional
    public void moveTaskToSpecifiedOrder(Long taskIdFrom, int newPosition) {
        Task task = taskRepository.findById(taskIdFrom)
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        int fromOrder = task.getOrder();

        if (fromOrder < newPosition) {
            taskRepository.decreaseOrderOfTasksOnInterval(task.getTrainingPhase().getId(), fromOrder, newPosition);
        } else if (fromOrder > newPosition) {
            taskRepository.increaseOrderOfTasksOnInterval(task.getTrainingPhase().getId(), newPosition, fromOrder);
        } else {
            // nothing should be changed, no further actions needed
            return;
        }

        task.setOrder(newPosition);
        taskRepository.save(task);
    }
}
