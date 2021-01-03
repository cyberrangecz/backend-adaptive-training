package com.example.demo.controller;

import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.input.LevelType;
import com.example.demo.service.LevelOperationsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/",
        tags = "Adaptive training definitions",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class AdaptiveTrainingDefinitionsRestController {

    private final LevelOperationsService levelOperationsService;

    @Autowired
    public AdaptiveTrainingDefinitionsRestController(LevelOperationsService levelOperationsService) {
        this.levelOperationsService = levelOperationsService;
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Swap levels' order",
            nickname = "swapLevelsOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Level orders changed"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(value = "/levels/{levelIdFrom}/swap-with/{levelIdTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void swapLevelsOrder(
            @ApiParam(value = "Level ID - from", required = true) @PathVariable(name = "levelIdFrom") Long levelIdFrom,
            @ApiParam(value = "Level ID - to", required = true) @PathVariable(name = "levelIdTo") Long levelIdTo) {

        levelOperationsService.swapLevelsOrder(levelIdFrom, levelIdTo);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete a specified level",
            nickname = "deleteLevel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Level deleted"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(value = "/levels/{levelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteLevel(
            @ApiParam(value = "Level ID", required = true) @PathVariable(name = "levelId") Long levelId) {
        levelOperationsService.deleteLevel(levelId);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create a new level",
            notes = "Creates only default level with a specified type",
            response = BaseLevelDto.class,
            nickname = "createLevel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Level created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/levels/{levelType}")
    public BaseLevelDto createLevel(
            @ApiParam(value = "Training definition ID", required = true) @RequestParam(name = "definitionId") Long definitionId,
            @ApiParam(value = "Level type", allowableValues = "game, assessment, info, phase", required = true)
            @PathVariable("levelType") LevelType levelType) {

        return levelOperationsService.createLevel(definitionId, levelType);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get level by ID",
            response = BaseLevelDto.class,
            nickname = "getLevel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Level returned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @GetMapping(path = "/levels/{levelId}")
    public BaseLevelDto getLevel(
            @ApiParam(value = "Level ID", required = true) @PathVariable("levelId") Long levelId) {

        return levelOperationsService.getLevel(levelId);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update info level",
            nickname = "updateInfoLevel",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Info level updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/info-levels/{levelId}")
    public void updateInfoLevel(
            @ApiParam(value = "Level ID", required = true) @PathVariable("levelId") Long levelId,
            @ApiParam(value = "Info level to be updated") @RequestBody InfoLevelUpdateDto infoLevelUpdateDto) {

        levelOperationsService.updateInfoLevel(levelId, infoLevelUpdateDto);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update task",
            nickname = "updateTask",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/tasks/{taskId}")
    public void updateTask(
            @ApiParam(value = "Task ID", required = true) @PathVariable("taskId") Long taskId,
            @ApiParam(value = "Task to be updated") @RequestBody GameLevelUpdateDto gameLevelUpdateDto) {

        levelOperationsService.updateTask(taskId, gameLevelUpdateDto);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create a new task",
            response = BaseLevelDto.class,
            nickname = "createTask",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/phases/{phaseId}")
    public BaseLevelDto createTask(
            @ApiParam(value = "Phase ID", required = true) @PathVariable(name = "phaseId") Long phaseId) {

        return levelOperationsService.createTask(phaseId);
    }
}
