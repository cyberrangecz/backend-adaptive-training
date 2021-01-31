package com.example.demo.controller;

import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.PhaseCreateDTO;
import com.example.demo.dto.PhaseLevelDto;
import com.example.demo.dto.PhaseLevelUpdateDto;
import com.example.demo.facade.TrainingPhaseFacade;
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
import java.util.List;

@RestController
@RequestMapping(value = "/training-definitions/{definitionId}/phases", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/training-definitions/{definitionId}/phases",
        tags = "Phases",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class PhasesController {

    @Autowired
    private TrainingPhaseFacade trainingPhaseFacade;

    @ApiOperation(httpMethod = "POST",
            value = "Create a new phase",
            notes = "Creates a new default phase with a specified type",
            response = BaseLevelDto.class,
            nickname = "createPhase",
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

        BaseLevelDto createdPhase = trainingPhaseFacade.createPhase(definitionId, phaseCreateDTO);

        return new ResponseEntity<>(createdPhase, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get all phases",
            notes = "Get all phases associated with specified training definition",
            response = Object.class,
            nickname = "getPhases",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phases returned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @GetMapping
    public ResponseEntity<List<BaseLevelDto>> getPhases(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId) {

        List<BaseLevelDto> phases = trainingPhaseFacade.getPhases(definitionId);

        return new ResponseEntity<>(phases, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get phase by ID",
            response = BaseLevelDto.class,
            nickname = "getPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phase returned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @GetMapping(path = "/{phaseId}")
    public ResponseEntity<BaseLevelDto> getPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Level ID", required = true)
            @PathVariable("phaseId") Long phaseId) {

        BaseLevelDto phase = trainingPhaseFacade.getPhase(definitionId, phaseId);

        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Remove phase by ID",
            response = BaseLevelDto.class,
            nickname = "getPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phase removed"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(path = "/{phaseId}")
    public ResponseEntity<List<BaseLevelDto>> removePhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Level ID", required = true)
            @PathVariable("phaseId") Long phaseId) {

        List<BaseLevelDto> remainingPhases = trainingPhaseFacade.deletePhase(definitionId, phaseId);

        return new ResponseEntity<>(remainingPhases, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update info phase",
            nickname = "updateInfoPhase",
            response = InfoLevelDto.class,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Info phase updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/{phaseId}/info")
    public ResponseEntity<InfoLevelDto> updateInfoPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Level ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Info phase to be updated")
            @RequestBody @Valid InfoLevelUpdateDto infoLevelUpdateDto) {

        InfoLevelDto updatedInfoPhase = trainingPhaseFacade.updateInfoPhase(definitionId, phaseId, infoLevelUpdateDto);

        return new ResponseEntity<>(updatedInfoPhase, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update training phase",
            nickname = "updateTrainingPhase",
            response = PhaseLevelDto.class,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training phase updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/{phaseId}/training")
    public ResponseEntity<PhaseLevelDto> updateTrainingPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Training phase to be updated")
            @RequestBody @Valid PhaseLevelUpdateDto phaseLevelUpdateDto) {

        PhaseLevelDto updatedTrainingPhase = trainingPhaseFacade.updateTrainingPhase(definitionId, phaseId, phaseLevelUpdateDto);

        return new ResponseEntity<>(updatedTrainingPhase, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Move phase to specified order",
            nickname = "movePhaseToSpecifiedOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phase moved to specified order"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(value = "/{phaseIdFrom}/move-to/{newPosition}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> movePhaseToSpecifiedOrder(
            @ApiParam(value = "Phase ID - from", required = true) @PathVariable(name = "phaseIdFrom") Long phaseIdFrom,
            @ApiParam(value = "Position (order) to which the phase should be moved", required = true) @PathVariable(name = "newPosition") int newPosition) {

        trainingPhaseFacade.movePhaseToSpecifiedOrder(phaseIdFrom, newPosition);

        return ResponseEntity.ok().build();
    }
}
