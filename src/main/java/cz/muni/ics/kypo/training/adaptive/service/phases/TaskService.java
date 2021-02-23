package cz.muni.ics.kypo.training.adaptive.service.phases;

import cz.muni.ics.kypo.training.adaptive.domain.phase.AbstractPhase;
import cz.muni.ics.kypo.training.adaptive.domain.phase.Task;
import cz.muni.ics.kypo.training.adaptive.domain.phase.TrainingPhase;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityErrorDetail;
import cz.muni.ics.kypo.training.adaptive.exceptions.EntityNotFoundException;
import cz.muni.ics.kypo.training.adaptive.repository.phases.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public static final String TASK_NOT_FOUND = "Task not found.";
    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createDefaultTask(TrainingPhase trainingPhase) {
        Task task = new Task();
        task.setTitle("Title of a new task");
        task.setTrainingPhase(trainingPhase);
        task.setOrder(taskRepository.getCurrentMaxOrder(trainingPhase.getId()) + 1);
        task.setAnswer("Secret flag");
        task.setContent("Task content ...");
        task.setSolution("Task solution ...");
        task.setIncorrectAnswerLimit(1);
        return taskRepository.save(task);
    }

    public Task createTask(Task task) {
        task.setOrder(taskRepository.getCurrentMaxOrder(task.getTrainingPhase().getId()) + 1);
        return taskRepository.save(task);
    }

    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(new EntityErrorDetail(AbstractPhase.class, "id", taskId.getClass(), taskId, TASK_NOT_FOUND)));
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task persistedTask = this.getTask(taskId);
        updatedTask.setId(taskId);
        updatedTask.setTrainingPhase(persistedTask.getTrainingPhase());
        updatedTask.setOrder(persistedTask.getOrder());
        return taskRepository.save(updatedTask);
    }

    public void removeTask(Task task) {
        taskRepository.decreaseOrderAfterTaskWasDeleted(task.getTrainingPhase().getId(), task.getOrder());
        taskRepository.delete(task);
    }

    public void moveTaskToSpecifiedOrder(Long taskIdFrom, int newPosition) {
        Task task = this.getTask(taskIdFrom);
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
