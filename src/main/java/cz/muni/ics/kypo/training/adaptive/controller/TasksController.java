package cz.muni.ics.kypo.training.adaptive.controller;

import cz.muni.ics.kypo.training.adaptive.dto.training.TaskCopyDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TaskUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.facade.TaskFacade;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/training-definitions/{definitionId}/phases/{phaseId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/training-definitions/{definitionId}/tasks",
        tags = "Tasks",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class TasksController {

    private final TaskFacade taskFacade;

    @Autowired
    public TasksController(TaskFacade taskFacade) {
        this.taskFacade = taskFacade;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create a new task in a phase",
            notes = "Creates a new default task in a specified training phase",
            response = TaskDTO.class,
            nickname = "createTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @ApiParam(value = "Training phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId) {
        TaskDTO createdTask = taskFacade.createDefaultTask(phaseId);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Clone task inside of the training phase",
            notes = "Creates a new task with the same properties as the specified task (pattern)",
            response = TaskDTO.class,
            nickname = "cloneTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task cloned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/{taskId}")
    public ResponseEntity<TaskDTO> cloneTask(
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId,
            @ApiParam(value = "Training phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId,
            @RequestBody @Valid TaskCopyDTO taskCopyDTO) {
        TaskDTO createdTask = taskFacade.createTask(phaseId, taskCopyDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get task",
            notes = "Get task detail associated with the specified training phase",
            response = TaskDTO.class,
            nickname = "getTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task returned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @GetMapping(path = "/{taskId}")
    public ResponseEntity<TaskDTO> getTask(
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId) {
        TaskDTO createdTask = taskFacade.getTask(taskId);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update task",
            notes = "Update the specified task",
            nickname = "updateTask",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId,
            @ApiParam(value = "Task to be updated")
            @RequestBody @Valid TaskUpdateDTO taskUpdateDto) {
        TaskDTO updatedTask = taskFacade.updateTask(taskId, taskUpdateDto);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Remove a task",
            notes = "Remove the specified task",
            response = TaskDTO.class,
            nickname = "removeTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task removed"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(path = "/{taskId}")
    public ResponseEntity<Void> removeTask(
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId) {
        taskFacade.removeTask(taskId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Move task to specified order",
            nickname = "moveTaskToSpecifiedOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task moved to specified order"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(value = "/{taskIdFrom}/move-to/{newPosition}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> moveTaskToSpecifiedOrder(
            @ApiParam(value = "Task ID - from", required = true) @PathVariable(name = "taskIdFrom") Long taskIdFrom,
            @ApiParam(value = "Position (order) to which the task should be moved", required = true) @PathVariable(name = "newPosition") int newPosition) {
        taskFacade.moveTaskToSpecifiedOrder(taskIdFrom, newPosition);
        return ResponseEntity.ok().build();
    }
}
