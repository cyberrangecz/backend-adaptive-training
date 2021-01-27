package com.example.demo.controller;

import com.example.demo.dto.TaskCreateDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/task", tags = {"Task"})
public class TaskController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

//    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Create a new task")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "New game level created"),
//                           @ApiResponse(code = 500, message = "Unexpected application error")})
//    public TaskDto createGameLevel(@ApiParam(value = "Game level", required = true) @RequestBody(required = true)
//                                           TaskCreateDto taskCreateDto) {
//        return taskService.createTask(taskCreateDto);
//    }
//
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Return game levels")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return game levels"),
//                           @ApiResponse(code = 500, message = "Unexpected application error")})
//    public List<TaskDto> findGameLevels() {
//        return taskService.findAllTasks();
//    }
//
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get game level detail")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game level detail"),
//                           @ApiResponse(code = 500, message = "Unexpected application error")})
//    public TaskDto getGameLevel(
//        @ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id) {
//        return taskService.getTask(id);
//    }

    // TODO this will be probably removed. This operation is implemented in AdaptiveTrainingDefinitionsRestController
//    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Update game level")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Updated game level"),
//                           @ApiResponse(code = 500, message = "Unexpected application error")})
//    public GameLevelDto updateGameLevel(
//        @ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id,
//        @ApiParam(value = "Update data", required = true) @RequestBody(required = true)
//            GameLevelUpdateDto gameLevelUpdateDto) {
//        return gameLevelService.updateGameLevel(id, gameLevelUpdateDto);
//    }

//    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Remove game level")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game level removed"),
//                           @ApiResponse(code = 500, message = "Unexpected application error")})
//    public void removeGameLevel(@ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id) {
//        taskService.removeTaskLevel(id);
//    }
}
