package com.example.demo.controller;

import com.example.demo.dto.InfoLevelCreateDto;
import com.example.demo.dto.InfoLevelDto;
import com.example.demo.dto.InfoLevelUpdateDto;
import com.example.demo.service.InfoLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@RequestMapping("/info-level")
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*",
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Api(value = "/info-level", tags = {"Info Level"})
public class InfoLevelController {

    private final InfoLevelService infoLevelService;

    @Autowired
    public InfoLevelController(InfoLevelService infoLevelService) {
        this.infoLevelService = infoLevelService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a new info level")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "New info level created"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public InfoLevelDto createInfoLevel(@ApiParam(value = "Game level", required = true) @RequestBody(required = true)
                                            InfoLevelCreateDto infoLevelCreateDto) {
        return infoLevelService.createInfoLevel(infoLevelCreateDto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return info levels")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return info levels"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public List<InfoLevelDto> findInfoLevels() {
        return infoLevelService.findAllInfoLevels();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get info level detail")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game level detail"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public InfoLevelDto getInfoLevel(
        @ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id) {
        return infoLevelService.getInfoLevel(id);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update info level")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Updated info level"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public InfoLevelDto updateInfoLevel(
        @ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id,
        @ApiParam(value = "Update data", required = true) @RequestBody(required = true)
            InfoLevelUpdateDto infoLevelUpdateDto) {
        return infoLevelService.updateInfoLevel(id, infoLevelUpdateDto);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remove info level")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Game level removed"),
                           @ApiResponse(code = 500, message = "Unexpected application error")})
    public void removeInfoLevel(@ApiParam(value = "Game Level ID", required = true) @PathVariable("id") final Long id) {
        infoLevelService.removeInfoLevel(id);
    }
}
