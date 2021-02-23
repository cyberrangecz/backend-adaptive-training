package cz.muni.ics.kypo.training.adaptive.controller;

import cz.muni.ics.kypo.training.adaptive.dto.run.QuestionnairePhaseAnswersDTO;
import cz.muni.ics.kypo.training.adaptive.facade.QuestionnaireEvaluationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/training-runs", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/training-runs",
        tags = "Training runs",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class QuestionnaireEvaluationController {

    private final QuestionnaireEvaluationFacade questionnaireEvaluationFacade;

    @Autowired
    public QuestionnaireEvaluationController(QuestionnaireEvaluationFacade questionnaireEvaluationFacade) {
        this.questionnaireEvaluationFacade = questionnaireEvaluationFacade;
    }

    @ApiOperation(httpMethod = "POST",
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
        questionnaireEvaluationFacade.evaluateAnswersToQuestionnaire(runId, questionnairePhaseAnswersDTO);
        return ResponseEntity.noContent().build();
    }
}
