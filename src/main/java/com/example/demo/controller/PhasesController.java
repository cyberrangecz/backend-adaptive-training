package com.example.demo.controller;

import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.PhaseCreateDTO;
import com.example.demo.service.LevelOperationsService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/training-definitions/{definitionId}/phases", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/training-definitions/{definitionId}/phases",
        tags = "Phases",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class PhasesController {

    @Autowired
    private LevelOperationsService levelOperationsService;

    @ApiOperation(httpMethod = "POST",
            value = "Create a new phase",
            notes = "Creates a new default phase with a specified type",
            response = BaseLevelDto.class,
            nickname = "createLevel",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Phase created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping
    public ResponseEntity<BaseLevelDto> createPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Level type", allowableValues = "questionnaire, info, game", required = true)
            @RequestBody @Valid PhaseCreateDTO phaseCreateDTO) {

        BaseLevelDto createdPhase = levelOperationsService.createLevel(definitionId, phaseCreateDTO);

        return new ResponseEntity<>(createdPhase, HttpStatus.CREATED);
    }
}
