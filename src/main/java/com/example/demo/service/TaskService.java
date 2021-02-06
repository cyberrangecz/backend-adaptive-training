package com.example.demo.service;

import com.example.demo.domain.Task;
import com.example.demo.domain.TrainingPhase;
import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.TrainingPhaseRepository;
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

    public TaskDto createDefaultTask(Long trainingDefinitionId, Long phaseId) {
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

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public TaskDto cloneTask(Long trainingDefinitionId, Long phaseId, Long taskId) {
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


    public TaskDto createTask(TaskCreateDto taskCreateDto) {
        Task task = BeanMapper.INSTANCE.toEntity(taskCreateDto);

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public TaskDto getTask(Long trainingDefinitionId, Long phaseId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId and phaseId (field structure will be probably changed)

        return BeanMapper.INSTANCE.toDto(task);
    }

    public TaskDto updateTask(Long trainingDefinitionId, Long phaseId, Long taskId, TaskUpdateDto taskUpdateDto) {
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
