package com.example.demo.controller;

import com.example.demo.dto.GameLevelDto;
import com.example.demo.service.GameLevelService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game-level")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/game-level", tags = {"Game Level"})
public class GameLevelController {

    private static final Logger LOG = LoggerFactory.getLogger(GameLevelController.class);

    private final GameLevelService gameLevelService;

    @Autowired
    public GameLevelController(GameLevelService gameLevelService) {
        this.gameLevelService = gameLevelService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return game levels")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return game levels"),
            @ApiResponse(code = 500, message = "Unexpected application error")})
    public List<GameLevelDto> findGameLevels() {
        return gameLevelService.findAllGameLevels();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return game levels")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return game levels"),
        @ApiResponse(code = 500, message = "Unexpected application error")})
    public List<GameLevelDto> updateGameLevel(@ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id,
        @ApiParam(value = "Update data", required = true) @RequestBody(required = true) GameLevelDto gameLevelDto) {
        return gameLevelService.findAllGameLevels();
    }
}
