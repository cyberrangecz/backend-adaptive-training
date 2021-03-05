package cz.muni.ics.kypo.training.adaptive.controller;


import cz.muni.ics.kypo.training.adaptive.dto.sankeygraph.SankeyGraphDTO;
import cz.muni.ics.kypo.training.adaptive.exceptions.errors.ApiError;
import cz.muni.ics.kypo.training.adaptive.facade.VisualizationFacade;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "/visualizations", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisualizationRestController {

    private final VisualizationFacade visualizationFacade;

    @Autowired
    public VisualizationRestController(VisualizationFacade visualizationFacade) {
        this.visualizationFacade = visualizationFacade;
    }

    /**
     * Get data for sankey graph visualization.
     *
     * @param trainingInstanceId id of training instance.
     * @return data for sankey visualizations.
     */
    @ApiOperation(httpMethod = "GET",
            value = "Get necessary visualization info for training instance.",
            response = SankeyGraphDTO.class,
            nickname = "getSankeyGraphVisualization",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data for visualization found.", response = SankeyGraphDTO.class),
            @ApiResponse(code = 404, message = "Training instance with given id not found.", response = ApiError.class),
            @ApiResponse(code = 500, message = "Unexpected condition was encountered.", response = ApiError.class)
    })
    @GetMapping(path = "/training-instances/{instanceId}/sankey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SankeyGraphDTO> getSankeyGraphVisualization(@ApiParam(value = "Training instance ID", required = true)
                                                                      @PathVariable("instanceId") Long trainingInstanceId) {
        SankeyGraphDTO clusteringVisualizationDTO = visualizationFacade.getSankeyGraph(trainingInstanceId);
        return ResponseEntity.ok(clusteringVisualizationDTO);
    }

}
