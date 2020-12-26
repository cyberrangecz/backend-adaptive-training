package com.example.demo.controller;

import com.example.demo.dto.BaseLevelDto;
import com.example.demo.dto.input.LevelType;
import com.example.demo.service.LevelOperationsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phases")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/phases", tags = {"Phases operations"})
public class PhaseOperationsController {

    @Autowired
    private LevelOperationsService levelOperationsService;

    @PostMapping(path = "/{phaseId}")
    public BaseLevelDto createLevel(
        @ApiParam(value = "Phase ID", required = true) @PathVariable(name = "phaseId") Long phaseId) {

        return levelOperationsService.createTask(phaseId);
    }
}
