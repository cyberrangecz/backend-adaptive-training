package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private TaskService taskService;

    @ApiOperation(httpMethod = "POST",
            value = "Create a new task in a phase",
            notes = "Creates a new default task in a specified game phase",
            response = TaskDto.class,
            nickname = "createTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Game phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId) {

        TaskDto createdTask = taskService.createDefaultTask(definitionId, phaseId);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Clone task inside of the game phase",
            notes = "Creates a new task with the same properties as the specified task (pattern)",
            response = TaskDto.class,
            nickname = "cloneTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task cloned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/{taskId}")
    public ResponseEntity<TaskDto> cloneTask(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Game phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId,
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId) {

        TaskDto createdTask = taskService.cloneTask(definitionId, phaseId, taskId);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get tasks",
            notes = "Get tasks detail associated with the specified game phase",
            response = TaskDto.class,
            nickname = "getTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task returned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @GetMapping(path = "/{taskId}")
    public ResponseEntity<TaskDto> getTask(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Game phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId,
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId) {

        TaskDto createdTask = taskService.getTask(definitionId, phaseId, taskId);

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
    @PutMapping
    public ResponseEntity<TaskDto> updateTask(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Game phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId,
            @ApiParam(value = "Task to be updated")
            @RequestBody @Valid TaskUpdateDto taskUpdateDto) {

        TaskDto updatedTask = taskService.updateTask(definitionId, phaseId, taskUpdateDto);

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Remove a task",
            notes = "Remove the specified task",
            response = TaskDto.class,
            nickname = "removeTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task removed"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(path = "/{taskId}")
    public ResponseEntity<Void> removeTask(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Game phase ID", required = true)
            @PathVariable(name = "phaseId") Long phaseId,
            @ApiParam(value = "Task ID", required = true)
            @PathVariable(name = "taskId") Long taskId) {

        taskService.removeTask(definitionId, phaseId, taskId);

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

        taskService.moveTaskToSpecifiedOrder(taskIdFrom, newPosition);

        return ResponseEntity.ok().build();
    }
}
