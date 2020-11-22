package com.example.demo.controller;

import com.example.demo.dto.TrainingDefinitionDto;
import com.example.demo.dto.input.GameDefinitionCreateDto;
import com.example.demo.service.TrainingDefinitionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/training-definition")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/training-definition", tags = {"Training Definition"})
public class TrainingDefinitionController {

    private static final Logger LOG = LoggerFactory.getLogger(TrainingDefinitionController.class);

    private final TrainingDefinitionService trainingDefinitionService;

    @Autowired
    public TrainingDefinitionController(TrainingDefinitionService trainingDefinitionService) {
        this.trainingDefinitionService = trainingDefinitionService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new training definition")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "New training definition created"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public GameDefinitionCreateDto createTrainingDefinition(
        @ApiParam(value = "Training definition", required = true) @RequestBody(required = true)
            TrainingDefinitionDto trainingDefinitionDto) {
        return trainingDefinitionService.createTrainingDefinition(trainingDefinitionDto);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return specified training definition")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Training definition returned"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public TrainingDefinitionDto getTrainingDefinition(
        @ApiParam(value = "Game definition", required = true) @PathVariable(required = true) Long id) {
        return trainingDefinitionService.getTrainingDefinition(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return all training definitions")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Training definitions returned"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public List<TrainingDefinitionDto> getAllTrainingDefinitions() {
        return trainingDefinitionService.getAllTrainingDefinitions();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update specified training definition")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Training definition was updated"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public GameDefinitionCreateDto updateTrainingDefinition(
        @ApiParam(value = "Game definition", required = true) @PathVariable(required = true) Long id,
        @ApiParam(value = "Training definition", required = true) @RequestBody(required = true)
            TrainingDefinitionDto trainingDefinitionDto) {
        return trainingDefinitionService.updateTrainingDefinition(id, trainingDefinitionDto);
    }
}
