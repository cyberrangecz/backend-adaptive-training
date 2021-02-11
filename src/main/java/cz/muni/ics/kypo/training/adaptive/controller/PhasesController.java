package cz.muni.ics.kypo.training.adaptive.controller;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.facade.TrainingPhaseFacade;
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

    private final TrainingPhaseFacade trainingPhaseFacade;

    @Autowired
    public PhasesController(TrainingPhaseFacade trainingPhaseFacade) {
        this.trainingPhaseFacade = trainingPhaseFacade;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create a new phase",
            notes = "Creates a new default phase with a specified type",
            response = AbstractPhaseDTO.class,
            nickname = "createPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Phase created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping
    public ResponseEntity<AbstractPhaseDTO> createPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase type", allowableValues = "questionnaire, info, game", required = true)
            @RequestBody @Valid PhaseCreateDTO phaseCreateDTO) {
        AbstractPhaseDTO createdPhase = trainingPhaseFacade.createPhase(definitionId, phaseCreateDTO);
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
    public ResponseEntity<List<AbstractPhaseDTO>> getPhases(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId) {
        List<AbstractPhaseDTO> phases = trainingPhaseFacade.getPhases(definitionId);
        return new ResponseEntity<>(phases, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get phase by ID",
            response = AbstractPhaseDTO.class,
            nickname = "getPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phase returned"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @GetMapping(path = "/{phaseId}")
    public ResponseEntity<AbstractPhaseDTO> getPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId) {
        AbstractPhaseDTO phase = trainingPhaseFacade.getPhase(definitionId, phaseId);
        return new ResponseEntity<>(phase, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Remove phase by ID",
            response = AbstractPhaseDTO.class,
            nickname = "getPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phase removed"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(path = "/{phaseId}")
    public ResponseEntity<List<AbstractPhaseDTO>> removePhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId) {
        List<AbstractPhaseDTO> remainingPhases = trainingPhaseFacade.deletePhase(definitionId, phaseId);
        return new ResponseEntity<>(remainingPhases, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update info phase",
            nickname = "updateInfoPhase",
            response = InfoPhaseDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Info phase updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/{phaseId}/info")
    public ResponseEntity<InfoPhaseDTO> updateInfoPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Info phase to be updated")
            @RequestBody @Valid InfoPhaseUpdateDTO infoPhaseUpdateDto) {
        InfoPhaseDTO updatedInfoPhase = trainingPhaseFacade.updateInfoPhase(definitionId, phaseId, infoPhaseUpdateDto);
        return new ResponseEntity<>(updatedInfoPhase, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update training phase",
            nickname = "updateTrainingPhase",
            response = TrainingPhaseDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training phase updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/{phaseId}/training")
    public ResponseEntity<TrainingPhaseDTO> updateTrainingPhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Training phase to be updated")
            @RequestBody @Valid TrainingPhaseUpdateDTO trainingPhaseUpdateDto) {
        TrainingPhaseDTO updatedTrainingPhase = trainingPhaseFacade.updateTrainingPhase(definitionId, phaseId, trainingPhaseUpdateDto);
        return new ResponseEntity<>(updatedTrainingPhase, HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update questionnaire phase",
            nickname = "updateQuestion",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Questionnaire phase updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/{phaseId}/questionnaire")
    public ResponseEntity<QuestionnairePhaseDTO> updateQuestionnairePhase(
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Questionnaire to be updated")
            @RequestBody @Valid QuestionnaireUpdateDTO questionnaireUpdateDto) {
        QuestionnairePhaseDTO updatedQuestionnairePhase = trainingPhaseFacade.updateQuestionnairePhase(definitionId, phaseId, questionnaireUpdateDto);
        return new ResponseEntity<>(updatedQuestionnairePhase, HttpStatus.OK);
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
