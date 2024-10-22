package cz.cyberrange.platform.training.adaptive.rest.controller;


import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.InstanceModelUpdate;
import cz.cyberrange.platform.training.adaptive.api.dto.CommandDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.trainingdefinition.TrainingDefinitionMitreTechniquesDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.sankey.SankeyDiagramDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.simulator.InstanceSimulatorDTO;
import cz.cyberrange.platform.training.adaptive.api.dto.visualizations.transitions.TransitionsDataDTO;
import cz.cyberrange.platform.training.adaptive.definition.exceptions.errors.ApiError;
import cz.cyberrange.platform.training.adaptive.rest.facade.SankeySimulatorFacade;
import cz.cyberrange.platform.training.adaptive.rest.facade.VisualizationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * The rest controller for Visualizations.
 */
@Api(value = "/visualizations",
        tags = "Visualizations",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "Full authentication is required to access this resource.", response = ApiError.class),
        @ApiResponse(code = 403, message = "The necessary permissions are required for a resource.", response = ApiError.class)
})
@RestController
@Validated
@RequestMapping(value = "/visualizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisualizationRestController {

    private final VisualizationFacade visualizationFacade;
    private final SankeySimulatorFacade sankeySimulatorFacade;

    @Autowired
    public VisualizationRestController(VisualizationFacade visualizationFacade, SankeySimulatorFacade sankeySimulatorFacade) {
        this.visualizationFacade = visualizationFacade;
        this.sankeySimulatorFacade = sankeySimulatorFacade;

    }

    /**
     * Get data for sankey graph visualization.
     *
     * @param trainingInstanceId id of training instance.
     * @return data for sankey visualizations.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get necessary visualization info for training instance.",
            response = SankeyDiagramDTO.class,
            nickname = "getSankeyDiagramVisualization",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data for visualization found.", response = SankeyDiagramDTO.class),
            @ApiResponse(code = 404, message = "Training instance with given id not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/training-instances/{instanceId}/sankey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SankeyDiagramDTO> getSankeyDiagramVisualization(@ApiParam(value = "Training instance ID", required = true)
                                                                          @PathVariable("instanceId") Long trainingInstanceId) {
        SankeyDiagramDTO clusteringVisualizationDTO = visualizationFacade.getSankeyDiagram(trainingInstanceId);
        return ResponseEntity.ok(clusteringVisualizationDTO);
    }

    /**
     * Process exported training instance data.
     *
     * @param zipFile exported training instance data.
     * @return data for sankey visualizations and training definition.
     */
    @ApiOperation(httpMethod = "POST",
            value = "Get necessary visualization info and training definition for training instance simulator.",
            response = InstanceSimulatorDTO.class,
            nickname = "uploadTrainingInstance",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data for visualization and definition uploaded.", response = InstanceSimulatorDTO.class),
            @ApiResponse(code = 400, message = "The provided exported data is not valid", response = ApiError.class),
            @ApiResponse(code = 404, message = "Training instance not found.", response = ApiError.class),
            @ApiResponse(code = 415, message = "File type not supported.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PostMapping(path = "/training-instances/simulator", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstanceSimulatorDTO> uploadTrainingInstance(@ApiParam(value = "Exported training instance data", required = true)
                                                                       @Valid @RequestBody byte[] zipFile) {
        return new ResponseEntity<>(sankeySimulatorFacade.processTrainingInstance(zipFile), HttpStatus.OK);
    }

    /**
     * Regenerate sankey diagram.
     *
     * @param instanceModelUpdate update data TODo
     * @return data for sankey visualizations.
     */
    @ApiOperation(httpMethod = "POST",
            value = "Get necessary visualization info for training instance simulator.",
            response = SankeyDiagramDTO.class,
            nickname = "uploadTrainingInstance",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data for visualization and definition uploaded.", response = SankeyDiagramDTO.class),
            @ApiResponse(code = 404, message = "Training instance not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @PostMapping(path = "/training-instances/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SankeyDiagramDTO> uploadTrainingInstance(@ApiParam(value = "Cache id", required = true)
                                                                       @Valid @RequestBody InstanceModelUpdate instanceModelUpdate) {
        return new ResponseEntity<>(sankeySimulatorFacade.generateSankeyDiagram(instanceModelUpdate), HttpStatus.OK);
    }

    /**
     * Get data for the transitions graph of all finished training runs in the given training instance.
     *
     * @param instanceId id of training instance.
     * @return data for the transitions graph.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get data for the transitions graph of the given training instance.",
            response = SankeyDiagramDTO.class,
            nickname = "getTransitionGraphDataForOrganizer",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data for the transitions graph found.", response = SankeyDiagramDTO.class),
            @ApiResponse(code = 404, message = "Training instance with given id not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/training-instances/{instanceId}/transitions-graph", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransitionsDataDTO> getTransitionGraphDataForOrganizer(
            @ApiParam(value = "Training Instance ID", required = true) @PathVariable("instanceId") Long instanceId
    ) {
        return ResponseEntity.ok(visualizationFacade.getTransitionsGraphForOrganizer(instanceId));
    }

    /**
     * Get data for the transitions graph of the finished training run.
     *
     * @param runId id of training run.
     * @return data for the transitions graph.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get data for the transitions graph of the given training run.",
            response = SankeyDiagramDTO.class,
            nickname = "getTransitionGraphDataForTrainee",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data for the transitions graph found.", response = SankeyDiagramDTO.class),
            @ApiResponse(code = 404, message = "Training run with given id not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/training-runs/{runId}/transitions-graph", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransitionsDataDTO> getTransitionGraphDataForTrainee(
            @ApiParam(value = "Training Run ID", required = true) @PathVariable("runId") Long runId
    ) {
        return ResponseEntity.ok(visualizationFacade.getTransitionsGraphForTrainee(runId));
    }

    /**
     * Gather all mitre techniques of the training definitions with indication if the definition has been played by user.
     *
     * @return summarized mitre techniques from all training definitions
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get summarized mitre techniques.",
            response = TrainingDefinitionMitreTechniquesDTO[].class,
            nickname = "getSummarizedMitreTechniques",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mitre techniques found.", response = TrainingDefinitionMitreTechniquesDTO[].class),
            @ApiResponse(code = 404, message = "Training instance with given id not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/training-definitions/mitre-techniques", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTrainingDefinitionsWithMitreTechniques() {
        List<TrainingDefinitionMitreTechniquesDTO> trainingDefinitionMitreTechniquesDTOS = visualizationFacade.getTrainingDefinitionsWithMitreTechniques();
        return ResponseEntity.ok(trainingDefinitionMitreTechniquesDTOS);
    }

    /**
     * Gather all commands in a training run.
     *
     * @param runId id of training run.
     * @return the list of commands including its timestamp.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get all commands in a training run.",
            response = List.class,
            nickname = "getAllCommandsInTrainingRun",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Commands in training run.", response = List.class),
            @ApiResponse(code = 404, message = "Training run with given id not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/commands/training-runs/{runId}/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommandDTO>> getAllCommandsInTrainingRun(
            @ApiParam(value = "Training run ID", required = true) @PathVariable("runId") Long runId) {
        return ResponseEntity.ok(visualizationFacade.getAllCommandsInTrainingRun(runId));
    }

}
