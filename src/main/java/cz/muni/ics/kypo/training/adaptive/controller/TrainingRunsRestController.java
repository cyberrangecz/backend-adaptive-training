package cz.muni.ics.kypo.training.adaptive.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.util.SquigglyUtils;
import com.querydsl.core.types.Predicate;
import cz.muni.ics.kypo.training.adaptive.annotations.swagger.ApiPageableSwagger;
import cz.muni.ics.kypo.training.adaptive.domain.training.TrainingRun;
import cz.muni.ics.kypo.training.adaptive.dto.AbstractPhaseDTO;
import cz.muni.ics.kypo.training.adaptive.dto.IsCorrectAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.SubmissionDTO;
import cz.muni.ics.kypo.training.adaptive.dto.UserRefDTO;
import cz.muni.ics.kypo.training.adaptive.dto.access.ValidatePasskeyDTO;
import cz.muni.ics.kypo.training.adaptive.dto.questionnaire.QuestionnairePhaseAnswersDTO;
import cz.muni.ics.kypo.training.adaptive.dto.responses.PageResultResource;
import cz.muni.ics.kypo.training.adaptive.dto.training.ValidateAnswerDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.AccessedTrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunByIdDTO;
import cz.muni.ics.kypo.training.adaptive.dto.trainingrun.TrainingRunDTO;
import cz.muni.ics.kypo.training.adaptive.exceptions.errors.ApiError;
import cz.muni.ics.kypo.training.adaptive.facade.TrainingRunFacade;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The rest controller for Training runs.
 */
@Api(value = "/training-runs",
        tags = "Training runs",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Full authentication is required to access this resource.", response = ApiError.class),
        @ApiResponse(code = 403, message = "The necessary permissions are required for a resource.", response = ApiError.class)
})
@RestController
@RequestMapping(value = "/training-runs", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class TrainingRunsRestController {

    private final TrainingRunFacade trainingRunFacade;
    private final ObjectMapper objectMapper;

    /**
     * Instantiates a new Training runs rest controller.
     *
     * @param trainingRunFacade the training run facade
     * @param objectMapper      the object mapper
     */
    @Autowired
    public TrainingRunsRestController(TrainingRunFacade trainingRunFacade,
                                      ObjectMapper objectMapper) {
        this.trainingRunFacade = trainingRunFacade;
        this.objectMapper = objectMapper;
    }

    /**
     * Delete training runs.
     *
     * @param trainingRunIds the training run ids
     * @param forceDelete    the force delete
     * @return the response entity
     */
    @ApiOperation(httpMethod = "DELETE",
            value = "Delete training runs",
            nickname = "deleteTrainingRuns")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training runs have been deleted."),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered", response = ApiError.class)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteTrainingRuns(
            @ApiParam(value = "Ids of training runs that will be deleted", required = true)
            @RequestParam(value = "trainingRunIds", required = true) List<Long> trainingRunIds,
            @ApiParam(value = "Indication if this training run must be deleted no matter of any check (force it)", required = false)
            @RequestParam(value = "forceDelete", required = false) boolean forceDelete) {
        trainingRunFacade.deleteTrainingRuns(trainingRunIds, forceDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a given training run.
     *
     * @param runId       the training run id
     * @param forceDelete the force delete
     * @return the response entity
     */
    @ApiOperation(httpMethod = "DELETE",
            value = "Delete training run",
            nickname = "deleteTrainingRun")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training run has been deleted."),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 409, message = "The training run is still running.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered", response = ApiError.class)
    })
    @DeleteMapping(path = "/{runId}")
    public ResponseEntity<Void> deleteTrainingRun(
            @ApiParam(value = "Id of training run that will be deleted", required = true)
            @PathVariable("runId") Long runId,
            @ApiParam(value = "Indication if this training run must be deleted no matter of any check (force it)", required = false)
            @RequestParam(value = "forceDelete", required = false) boolean forceDelete) {
        trainingRunFacade.deleteTrainingRun(runId, forceDelete);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get requested Training Run by id.
     *
     * @param runId  of Training Run to return.
     * @param fields attributes of the object to be returned as the result.
     * @return Requested Training Run by id.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get training run by ID.",
            response = TrainingRunByIdDTO.class,
            nickname = "findTrainingRunById",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training run has been found.", response = TrainingRunDTO.class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{runId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findTrainingRunById(@ApiParam(value = "Id of training run", required = true)
                                                      @PathVariable("runId") Long runId,
                                                      @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                      @RequestParam(value = "fields", required = false) String fields) {
        TrainingRunByIdDTO trainingRunResource = trainingRunFacade.findById(runId);
        Squiggly.init(objectMapper, fields);
        return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, trainingRunResource), HttpStatus.OK);
    }

    /**
     * Get all Training Runs.
     *
     * @param predicate specifies query to database.
     * @param pageable  pageable parameter with information about pagination.
     * @param fields    attributes of the object to be returned as the result.
     * @return all Training Runs.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get all training runs.",
            response = TrainingRunRestResource.class,
            nickname = "findAllTrainingRuns",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training runs have been found.", response = TrainingRunRestResource.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @ApiPageableSwagger
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> findAllTrainingRuns(@QuerydslPredicate(root = TrainingRun.class) Predicate predicate,
                                                      @ApiParam(value = "Pagination support.", required = false) Pageable pageable,
                                                      @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                      @RequestParam(value = "fields", required = false) String fields) {
        PageResultResource<TrainingRunDTO> trainingRunResource = trainingRunFacade.findAll(predicate, pageable);
        Squiggly.init(objectMapper, fields);
        return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, trainingRunResource), HttpStatus.OK);
    }

    /**
     * Access training run.
     *
     * @param accessToken the access token
     * @return first phase of training run.
     */
    @ApiOperation(httpMethod = "POST",
            value = "Access training run.",
            response = AccessTrainingRunDTO.class,
            nickname = "createTrainingRun",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training run has been accessed.", response = AccessTrainingRunDTO.class),
            @ApiResponse(code = 404, message = "There is no training instance with given accessToken or first phase not found in database.", response = ApiError.class),
            @ApiResponse(code = 409, message = "No assigned pool to the training instance.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Some error occurred during getting info about sandboxes.", response = ApiError.class),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTrainingRunDTO> accessTrainingRun(@ApiParam(value = "accessToken", required = true)
                                                                  @RequestParam(value = "accessToken", required = true) String accessToken) {
        AccessTrainingRunDTO accessTrainingRunDTO = trainingRunFacade.accessTrainingRun(accessToken);
        return ResponseEntity.ok(accessTrainingRunDTO);
    }

    /**
     * Get all accessed Training Runs.
     *
     * @param pageable    pageable parameter with information about pagination.
     * @param fields      attributes of the object to be returned as the result.
     * @param sortByTitle "asc" for ascending alphabetical sort by title, "desc" for descending
     * @return all accessed Training Runs.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get all accessed training runs.",
            notes = "Returns training run which was accessed by logged in user",
            response = AccessedTrainingRunRestResource.class,
            nickname = "getAllAccessedTrainingRuns",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The accessed training runs have been found.", response = AccessedTrainingRunDTO.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @ApiPageableSwagger
    @GetMapping(path = "/accessible", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllAccessedTrainingRuns(@ApiParam(value = "Pagination support.", required = false) Pageable pageable,
                                                             @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                                             @RequestParam(value = "fields", required = false) String fields,
                                                             @ApiParam(value = "Sort by title attribute. As values us asc|desc", required = false, example = "asc")
                                                             @RequestParam(value = "sortByTitle", required = false) String sortByTitle) {
        PageResultResource<AccessedTrainingRunDTO> accessedTrainingRunDTOS = trainingRunFacade.findAllAccessedTrainingRuns(pageable, sortByTitle);
        Squiggly.init(objectMapper, fields);
        return new ResponseEntity<>(SquigglyUtils.stringify(objectMapper, accessedTrainingRunDTOS), HttpStatus.OK);
    }

    /**
     * Get next phase of given Training Run.
     *
     * @param runId  of Training Run for which to get next phase.
     * @param fields attributes of the object to be returned as the result.
     * @return Requested next phase.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get phase of given training run.",
            notes = "Returns (questionnaire, training, info) phase if any next phase exists and training run as well",
            response = AbstractPhaseDTO.class,
            nickname = "getNextPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The next phase has been found.", response = AbstractPhaseDTO.class),
            @ApiResponse(code = 404, message = "The next phase has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{runId}/next-phases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNextPhase(@ApiParam(value = "Training run ID", required = true)
                                               @PathVariable("runId") Long runId,
                                               @ApiParam(value = "Fields which should be returned in REST API response", required = false)
                                               @RequestParam(value = "fields", required = false) String fields) {
        AbstractPhaseDTO phaseDTO = trainingRunFacade.getNextPhase(runId);
        Squiggly.init(objectMapper, fields);
        return ResponseEntity.ok(SquigglyUtils.stringify(objectMapper, phaseDTO));
    }

    /**
     * Get solution of current training phase.
     *
     * @param runId of Training Run for which to get solution.
     * @return Requested solution of training phase.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get solution of training phase.",
            notes = "Returns solution if given training runs exists and current phase is training phase",
            response = String.class,
            nickname = "getSolution",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The solution has been found.", response = String.class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 400, message = "Current phase is not training phase and does not have solution.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{runId}/solutions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSolution(@ApiParam(value = "Training run ID", required = true)
                                              @PathVariable("runId") Long runId) {
        return ResponseEntity.ok(trainingRunFacade.getSolution(runId));
    }

    /**
     * Check if submitted answer is correct.
     *
     * @param runId             the run id
     * @param validateAnswerDTO submitted string.
     * @return True if answer is correct, false if answer is wrong.
     */
    @ApiOperation(httpMethod = "POST",
            value = "Check answer of training phase",
            notes = "Current phase of given training run must be training phase",
            response = Boolean.class,
            nickname = "isCorrectAnswer",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The answer has been checked.", response = IsCorrectAnswerDTO.class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 400, message = "Current phase is not training phase and does not have answer.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PostMapping(path = "/{runId}/is-correct-answer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IsCorrectAnswerDTO> isCorrectAnswer(@ApiParam(value = "Training run ID", required = true)
                                                              @PathVariable("runId") Long runId,
                                                              @ApiParam(value = "Submitted answer", required = true)
                                                              @RequestBody @Valid ValidateAnswerDTO validateAnswerDTO) {
        return ResponseEntity.ok(trainingRunFacade.isCorrectAnswer(runId, validateAnswerDTO.getAnswer()));
    }

    /**
     * Check if submitted passkey is correct.
     *
     * @param runId             the run id
     * @param validatePasskeyDTO submitted string.
     * @return True if passkey is correct, false if passkey is wrong.
     */
    @ApiOperation(httpMethod = "POST",
            value = "Check passkey of access phase",
            notes = "Current phase of given training run must be acess phase",
            response = Boolean.class,
            nickname = "isCorrectPasskey",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The passkey has been checked.", response = Boolean.class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 400, message = "Current phase is not training phase and does not have answer.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PostMapping(path = "/{runId}/is-correct-passkey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> isCorrectPasskey(@ApiParam(value = "Training run ID", required = true)
                                                              @PathVariable("runId") Long runId,
                                                               @ApiParam(value = "Submitted passkey", required = true)
                                                              @RequestBody @Valid ValidatePasskeyDTO validatePasskeyDTO) {
        return ResponseEntity.ok(trainingRunFacade.isCorrectPasskey(runId, validatePasskeyDTO.getPasskey()));
    }

    /**
     * Resume paused training run.
     *
     * @param runId id of training run.
     * @return current phase of training run.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get current phase of resumed training run",
            response = AccessTrainingRunDTO.class,
            nickname = "resumeTrainingRun",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training run has been resumed.", response = AccessTrainingRunDTO.class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 409, message = "Cannot resume finished training run.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{runId}/resumption", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessTrainingRunDTO> resumeTrainingRun(@ApiParam(value = "Training run ID", required = true)
                                                                  @PathVariable("runId") Long runId) {
        AccessTrainingRunDTO resumedTrainingRunDTO = trainingRunFacade.resumeTrainingRun(runId);
        return ResponseEntity.ok(resumedTrainingRunDTO);
    }

    /**
     * Finish training run.
     *
     * @param runId id of training run.
     * @return the response entity
     */
    @ApiOperation(httpMethod = "PUT",
            value = "Finish training run",
            nickname = "finishTrainingRun",
            notes = "Training run will be finished if the current phase is the last phase and it is answered.",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training run has been finished."),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 409, message = "Cannot finish training run because of the current state.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PutMapping(path = "/{runId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> finishTrainingRun(@ApiParam(value = "Training run ID", required = true)
                                                  @PathVariable("runId") Long runId) {
        trainingRunFacade.finishTrainingRun(runId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Evaluate answers to a questionnaire phase",
            nickname = "evaluateAnswersToQuestionnaire",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Answers evaluated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(value = "/{runId}/questionnaire-evaluation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> evaluateAnswersToQuestionnaire(@ApiParam(value = "Training run ID", required = true)
                                                               @PathVariable("runId") Long runId,
                                                               @ApiParam(value = "Responses to questionnaire", required = true)
                                                               @Valid @RequestBody QuestionnairePhaseAnswersDTO questionnairePhaseAnswersDTO) {
        trainingRunFacade.evaluateAnswersToQuestionnaire(runId, questionnairePhaseAnswersDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get requested participant of the given training run.
     *
     * @param trainingRunId id of training run for which to get participant
     * @return Participant of specific training run.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get participant.",
            response = UserRefDTO.class,
            nickname = "getParticipant",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The participant has been found.", response = UserRefDTO.class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{runId}/participant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getParticipant(
            @ApiParam(value = "Get participant for the given runId.")
            @PathVariable("runId") Long trainingRunId) {
        UserRefDTO participant = trainingRunFacade.getParticipant(trainingRunId);
        return ResponseEntity.ok(SquigglyUtils.stringify(objectMapper, participant));
    }

    /**
     * Archive training run.
     *
     * @param runId id of training run.
     * @return the response entity
     */
    @ApiOperation(httpMethod = "PATCH",
            value = "Archive training run",
            nickname = "archiveTrainingRun",
            notes = "The state of the Training run will be change to archived.",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The training run has been archived."),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PatchMapping(path = "/{runId}/archive", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> archiveTrainingRun(@ApiParam(value = "Training run ID", required = true)
                                                   @PathVariable("runId") Long runId) {
        trainingRunFacade.archiveTrainingRun(runId);
        return ResponseEntity.ok().build();
    }

    /**
     * Get trainees submissions.
     *
     * @param runId ID of training run for which to get submissions
     * @param phaseId ID of the phase to specify subset of submissions
     * @return Submissions submitted during the training run of the trainee.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get trainees submissions.",
            response = SubmissionDTO[].class,
            nickname = "getTraineesSubmissions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The submissions has been found.", response = SubmissionDTO[].class),
            @ApiResponse(code = 404, message = "The training run has not been found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/{runId}/submissions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTraineesSubmissions(
            @ApiParam(value = "Training run ID") @PathVariable Long runId,
            @ApiParam(value = "Training phase ID.") @RequestParam(required = false) Long phaseId) {
        List<SubmissionDTO> traineeSubmissions = trainingRunFacade.getTraineesSubmissions(runId, phaseId);
        return ResponseEntity.ok(SquigglyUtils.stringify(objectMapper, traineeSubmissions));
    }

    /**
     * The type Training run rest resource.
     */
    @ApiModel(value = "TrainingRunRestResource",
            description = "Content (Retrieved data) and meta information about REST API result page. Including page number, number of elements in page, size of elements, total number of elements and total number of pages")
    public static class TrainingRunRestResource extends PageResultResource<TrainingRunDTO> {
        @JsonProperty(required = true)
        @ApiModelProperty(value = "Retrieved Training Runs from databases.")
        private List<TrainingRunDTO> content;
        @JsonProperty(required = true)
        @ApiModelProperty(value = "Pagination including: page number, number of elements in page, size, total elements and total pages.")
        private Pagination pagination;
    }

    @ApiModel(description = "Content (Retrieved data) and meta information about REST API result page. Including page number, number of elements in page, size of elements, total number of elements and total number of pages")
    private static class AccessedTrainingRunRestResource extends PageResultResource<AccessedTrainingRunDTO> {
        @JsonProperty(required = true)
        @ApiModelProperty(value = "Retrieved Accessed Training Runs from databases.")
        private List<AccessedTrainingRunDTO> content;
        @JsonProperty(required = true)
        @ApiModelProperty(value = "Pagination including: page number, number of elements in page, size, total elements and total pages.")
        private Pagination pagination;
    }


}
