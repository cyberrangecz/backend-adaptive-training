package cz.muni.ics.kypo.training.adaptive.controller;

import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.PhaseCreateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.info.InfoPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnaireUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.training.TrainingPhaseUpdateDTO;
import cz.muni.ics.kypo.training.adaptive.facade.PhaseFacade;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/training-definitions/{definitionId}/phases", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/training-definitions/{definitionId}/phase",
        tags = "Phases",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class PhasesController {

    private final PhaseFacade phaseFacade;

    @Autowired
    public PhasesController(PhaseFacade phaseFacade) {
        this.phaseFacade = phaseFacade;
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
        AbstractPhaseDTO createdPhase = phaseFacade.createPhase(definitionId, phaseCreateDTO);
        return new ResponseEntity<>(createdPhase, HttpStatus.CREATED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get all phase",
            notes = "Get all phase associated with specified training definition",
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
        List<AbstractPhaseDTO> phases = phaseFacade.getPhases(definitionId);
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
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId) {
        AbstractPhaseDTO phase = phaseFacade.getPhase(phaseId);
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
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId) {
        List<AbstractPhaseDTO> remainingPhases = phaseFacade.deletePhase(phaseId);
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
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Info phase to be updated")
            @RequestBody @Valid InfoPhaseUpdateDTO infoPhaseUpdateDto) {
        InfoPhaseDTO updatedInfoPhase = phaseFacade.updateInfoPhase(phaseId, infoPhaseUpdateDto);
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
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Training phase to be updated")
            @RequestBody @Valid TrainingPhaseUpdateDTO trainingPhaseUpdateDto) {
        TrainingPhaseDTO updatedTrainingPhase = phaseFacade.updateTrainingPhase(phaseId, trainingPhaseUpdateDto);
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
            @ApiParam(value = "Phase ID", required = true)
            @PathVariable("phaseId") Long phaseId,
            @ApiParam(value = "Questionnaire to be updated")
            @RequestBody @Valid QuestionnaireUpdateDTO questionnaireUpdateDto) {
        QuestionnairePhaseDTO updatedQuestionnairePhase = phaseFacade.updateQuestionnairePhase(phaseId, questionnaireUpdateDto);
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
            @ApiParam(value = "Training definition ID", required = true)
            @PathVariable(name = "definitionId") Long definitionId,
            @ApiParam(value = "Phase ID - from", required = true)
            @PathVariable(name = "phaseIdFrom") Long phaseIdFrom,
            @ApiParam(value = "Position (order) to which the phase should be moved", required = true)
            @PathVariable(name = "newPosition") int newPosition) {
        phaseFacade.movePhaseToSpecifiedOrder(phaseIdFrom, newPosition);
        return ResponseEntity.ok().build();
    }
}
