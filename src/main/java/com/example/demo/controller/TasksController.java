package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/training-definitions/{definitionId}/phases/{phaseId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/training-definitions/{definitionId}/phases",
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
}
