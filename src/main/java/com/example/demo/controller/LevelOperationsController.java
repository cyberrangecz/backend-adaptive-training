package com.example.demo.controller;

import com.example.demo.dto.InfoLevelDto;
import com.example.demo.service.LevelOperationsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{definitionId}/levels")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/{}/levels", tags = {"Level operations"})
public class LevelOperationsController {

    private final LevelOperationsService levelOperationsService;

    @Autowired
    public LevelOperationsController(LevelOperationsService levelOperationsService) {
        this.levelOperationsService = levelOperationsService;
    }

    @PutMapping(value = "/{levelIdFrom}/swap-with/{levelIdTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Swap levels' order")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Level orders changed"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public void createInfoLevel(
        @ApiParam(value = "Training definition ID", required = true) @PathVariable(name = "definitionId")
            Long definitionId,
        @ApiParam(value = "Level ID - from", required = true) @PathVariable(name = "levelIdFrom") Long levelIdFrom,
        @ApiParam(value = "Level ID - to", required = true) @PathVariable(name = "levelIdTo") Long levelIdTo) {

        levelOperationsService.swapLevelsOrder(definitionId, levelIdFrom, levelIdTo);
    }
}
