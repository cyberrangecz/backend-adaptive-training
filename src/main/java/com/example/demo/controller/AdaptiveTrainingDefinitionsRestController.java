package com.example.demo.controller;

import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.dto.PhaseLevelUpdateDto;
import com.example.demo.dto.QuestionChoiceDto;
import com.example.demo.dto.QuestionChoiceUpdateDto;
import com.example.demo.dto.QuestionDto;
import com.example.demo.dto.QuestionUpdateDto;
import com.example.demo.dto.QuestionnaireUpdateDto;
import com.example.demo.dto.TaskUpdateDto;
import com.example.demo.enums.QuestionType;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            value = "Move level to specified order",
            nickname = "moveLevelToSpecifiedOrder",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Level moved to specified order"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(value = "/levels/{levelIdFrom}/move-to/{newPosition}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void moveLevelToSpecifiedOrder(
            @ApiParam(value = "Level ID - from", required = true) @PathVariable(name = "levelIdFrom") Long levelIdFrom,
            @ApiParam(value = "Position (order) to which the level should be moved", required = true) @PathVariable(name = "newPosition") int newPosition) {

        levelOperationsService.moveLevelToSpecifiedOrder(levelIdFrom, newPosition);
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

//    @ApiOperation(httpMethod = "POST",
//            value = "Create a new level",
//            notes = "Creates only default level with a specified type",
//            response = BaseLevelDto.class,
//            nickname = "createLevel",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Level created"),
//            @ApiResponse(code = 500, message = "Unexpected application error")
//    })
//    @PostMapping(path = "/levels/{levelType}")
//    public BaseLevelDto createLevel(
//            @ApiParam(value = "Training definition ID", required = true) @RequestParam(name = "definitionId") Long definitionId,
//            @ApiParam(value = "Level type", allowableValues = "questionnaire, assessment, info, phase", required = true)
//            @PathVariable("levelType") LevelType levelType) {
//
//        return levelOperationsService.createLevel(definitionId, levelType);
//    }

//    @ApiOperation(httpMethod = "GET",
//            value = "Get level by ID",
//            response = BaseLevelDto.class,
//            nickname = "getLevel",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Level returned"),
//            @ApiResponse(code = 500, message = "Unexpected application error")
//    })
//    @GetMapping(path = "/levels/{levelId}")
//    public BaseLevelDto getLevel(
//            @ApiParam(value = "Level ID", required = true) @PathVariable("levelId") Long levelId) {
//
//        return levelOperationsService.getLevel(levelId);
//    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update info level",
            nickname = "updateInfoLevel",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Info level updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/info-levels")
    public void updateInfoLevel(
            @ApiParam(value = "Info level to be updated") @RequestBody InfoLevelUpdateDto infoLevelUpdateDto) {

        levelOperationsService.updateInfoLevel(infoLevelUpdateDto);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update phase",
            nickname = "updatePhaseLevel",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Phase level"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/phases")
    public void updatePhaseLevel(
            @ApiParam(value = "Info level to be updated") @RequestBody PhaseLevelUpdateDto phaseLevelUpdateDto) {

        levelOperationsService.updatePhaseLevel(phaseLevelUpdateDto);
    }

//    @ApiOperation(httpMethod = "PUT",
//            value = "Update task",
//            nickname = "updateTask",
//            consumes = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Task updated"),
//            @ApiResponse(code = 500, message = "Unexpected application error")
//    })
//    @PutMapping(path = "/tasks")
//    public void updateTask(
//            @ApiParam(value = "Task to be updated") @RequestBody TaskUpdateDto taskUpdateDto) {
//
//        levelOperationsService.updateTask(taskUpdateDto);
//    }

//    @ApiOperation(httpMethod = "POST",
//            value = "Create a new task",
//            response = BaseLevelDto.class,
//            nickname = "createTask",
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Task created"),
//            @ApiResponse(code = 500, message = "Unexpected application error")
//    })
//    @PostMapping(path = "/phases/{phaseId}")
//    public BaseLevelDto createTask(
//            @ApiParam(value = "Phase ID", required = true) @PathVariable(name = "phaseId") Long phaseId) {
//
//        return levelOperationsService.createTask(phaseId);
//    }

    @ApiOperation(httpMethod = "POST",
            value = "Create a new question in questionnaire",
            response = BaseLevelDto.class,
            nickname = "createQuestion",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/questionnaires/{questionnaireId}/{questionType}")
    public QuestionDto createQuestion(
            @ApiParam(value = "Questionnaire ID", required = true) @PathVariable(name = "questionnaireId") Long questionnaireId,
            @ApiParam(value = "Questionnaire Type", allowableValues = "FFQ, MCQ, EMI", required = true) @PathVariable(name = "questionType") QuestionType questionType) {

        return levelOperationsService.createQuestion(questionnaireId, questionType);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Create a new choice in question",
            response = BaseLevelDto.class,
            nickname = "createQuestionChoice",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question choice created"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/questions/{questionId}")
    public QuestionChoiceDto createQuestionChoice(
            @ApiParam(value = "Question ID", required = true) @PathVariable(name = "questionId") Long questionId) {

        return levelOperationsService.createQuestionChoice(questionId);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update questionnaire",
            nickname = "updateQuestionnaire",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Questionnaire updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/questionnaires")
    public void updateQuestionnaire(
            @ApiParam(value = "Question to be updated") @RequestBody QuestionnaireUpdateDto questionUpdateDto) {

        levelOperationsService.updateQuestionnaire(questionUpdateDto);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update question",
            nickname = "updateQuestion",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/questions")
    public void updateQuestion(
            @ApiParam(value = "Question to be updated") @RequestBody QuestionUpdateDto questionUpdateDto) {

        levelOperationsService.updateQuestion(questionUpdateDto);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete a specified question",
            nickname = "deleteQuestion",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question deleted"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuestion(
            @ApiParam(value = "Question ID", required = true) @PathVariable(name = "questionId") Long questionId) {
        levelOperationsService.deleteQuestion(questionId);
    }

    @ApiOperation(httpMethod = "PUT",
            value = "Update question choice",
            nickname = "updateQuestionChoice",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question choice updated"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PutMapping(path = "/question-choices")
    public void updateQuestionChoice(
            @ApiParam(value = "Question choice to be updated") @RequestBody QuestionChoiceUpdateDto questionChoiceUpdateDto) {

        levelOperationsService.updateQuestionChoice(questionChoiceUpdateDto);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "Delete a specified question choice",
            nickname = "deleteQuestionChoice",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question choice deleted"),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @DeleteMapping(value = "/question-choices/{questionChoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteQuestionChoice(
            @ApiParam(value = "Question choice ID", required = true) @PathVariable(name = "questionChoiceId") Long questionChoiceId) {
        levelOperationsService.deleteQuestionChoice(questionChoiceId);
    }
}
