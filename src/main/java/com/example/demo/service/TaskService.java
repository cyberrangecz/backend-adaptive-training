package com.example.demo.service;

import com.example.demo.domain.PhaseLevel;
import com.example.demo.domain.Task;
import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.mapper.BeanMapper;
import com.example.demo.repository.PhaseLevelRepository;
import com.example.demo.repository.TaskRepository;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final PhaseLevelRepository phaseLevelRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, PhaseLevelRepository phaseLevelRepository) {
        this.taskRepository = taskRepository;
        this.phaseLevelRepository = phaseLevelRepository;
    }

    public TaskDto createDefaultTask(Long trainingDefinitionId, Long phaseId) {
        PhaseLevel gamePhase = phaseLevelRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Game phase was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        // TODO add check to trainingDefinitionId (field structure will be probably changed)

        Task task = new Task();
        task.setTitle("Title of task");
        task.setPhaseLevel(gamePhase);
        task.setOrder(taskRepository.getCurrentMaxOrder(phaseId) + 1);

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public TaskDto cloneTask(Long trainingDefinitionId, Long phaseId, Long taskId) {
        Task taskToBeCloned = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task was not found"));
        // TODO throw proper exception once kypo2-training is migrated

        PhaseLevel gamePhase = phaseLevelRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Game phase was not found"));

        // TODO add check to trainingDefinitionId (field structure will be probably changed)

        Task task = new Task();
        BeanUtils.copyProperties(taskToBeCloned, task);

        task.setId(null);
        task.setPhaseLevel(gamePhase);
        task.setOrder(taskRepository.getCurrentMaxOrder(phaseId) + 1);

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }


    public TaskDto createTask(TaskCreateDto taskCreateDto) {
        Task task = BeanMapper.INSTANCE.toEntity(taskCreateDto);

        Task persistedEntity = taskRepository.save(task);

        return BeanMapper.INSTANCE.toDto(persistedEntity);
    }

    public List<TaskDto> findAllTasks() {

        Iterable<Task> allTasks = taskRepository.findAll();

        List<TaskDto> result = new ArrayList<>();

        if (!IterableUtils.isEmpty(allTasks)) {
            for (Task task : allTasks) {
                result.add(BeanMapper.INSTANCE.toDto(task));
            }
        }

        return result;
    }

    public TaskDto getTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty()) {
            LOG.error("No task found with ID {}.", id);
            return new TaskDto();
        }

        return BeanMapper.INSTANCE.toDto(task.get());
    }

    public TaskDto updateTask(Task taskUpdate) {
        Optional<Task> persistedTask = taskRepository.findById(taskUpdate.getId());

        if (persistedTask.isEmpty()) {
            // TODO return 404
            LOG.error("No task found with ID {}.", taskUpdate.getId());
            return new TaskDto();
        }

        taskUpdate.setPhaseLevel(persistedTask.get().getPhaseLevel());
        taskUpdate.setTrainingDefinitionId(persistedTask.get().getTrainingDefinitionId());
        taskUpdate.setOrder(persistedTask.get().getOrder());

        Task savedEntity = taskRepository.save(taskUpdate);

        return BeanMapper.INSTANCE.toDto(savedEntity);
    }

    public void removeTaskLevel(Long id) {
        taskRepository.deleteById(id);
    }
}
