package com.example.demo.controller;

import com.example.demo.dto.GameLevelCreateDto;
import com.example.demo.dto.GameLevelDto;
import com.example.demo.dto.GameLevelUpdateDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/game-level")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/game-level", tags = {"Game Level"})
public class GameLevelController {

    private static final Logger LOG = LoggerFactory.getLogger(GameLevelController.class);

    private final GameLevelService gameLevelService;

    @Autowired
    public GameLevelController(GameLevelService gameLevelService) {
        this.gameLevelService = gameLevelService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new game level")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "New game level created"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public GameLevelDto createGameLevel(@ApiParam(value = "Game level", required = true) @RequestBody(required = true)
                                            GameLevelCreateDto gameLevelCreateDto) {
        return gameLevelService.createGameLevel(gameLevelCreateDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return game levels")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return game levels"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public List<GameLevelDto> findGameLevels() {
        return gameLevelService.findAllGameLevels();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get game level detail")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game level detail"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public GameLevelDto getGameLevel(
        @ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id) {
        return gameLevelService.getGameLevel(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update game level")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Updated game level"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public GameLevelDto updateGameLevel(
        @ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id,
        @ApiParam(value = "Update data", required = true) @RequestBody(required = true)
            GameLevelUpdateDto gameLevelUpdateDto) {
        return gameLevelService.updateGameLevel(id, gameLevelUpdateDto);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove game level")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game level removed"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public void removeGameLevel(@ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id) {
        gameLevelService.removeGameLevel(id);
    }
}
