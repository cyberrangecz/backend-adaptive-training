package com.example.demo.controller;

import com.example.demo.dto.input.GameDefinitionCreateDto;
import com.example.demo.service.GameDefinitionService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game-definition")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/game-definition", tags = {"Game Definition"})
public class GameDefinitionController {

    private static final Logger LOG = LoggerFactory.getLogger(GameDefinitionController.class);

    private final GameDefinitionService gameDefinitionService;

    @Autowired
    public GameDefinitionController(GameDefinitionService gameDefinitionService) {
        this.gameDefinitionService = gameDefinitionService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new game definition")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "New game definition created"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public GameDefinitionCreateDto createGameLevel(@ApiParam(value = "Game definition", required = true) @RequestBody(required = true)
                                                       List<GameDefinitionCreateDto> gameDefinition) {
        return gameDefinitionService.createGameDefinition(gameDefinition);
    }
}
