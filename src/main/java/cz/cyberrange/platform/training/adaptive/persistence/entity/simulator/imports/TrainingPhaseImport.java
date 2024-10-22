package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ApiModel(
        value = "TrainingPhaseImport",
        description = "Imported training phase.",
        parent = AbstractPhaseImport.class
)
@Data
public class TrainingPhaseImport extends AbstractPhaseImport {

    @ApiModelProperty(value = "Estimated time it takes to finish the phase (in minutes).", required = true, example = "50")
    private int estimatedDuration;
    @ApiModelProperty(value = "Number of allowed commands that can be used to solve the task (used for data analysis).", required = true, example = "10", position = 1)
    private int allowedCommands;
    @ApiModelProperty(value = "How many times player can submit incorrect answer before displaying solution.", required = true, example = "4", position = 2)
    private int allowedWrongAnswers;
    @ApiModelProperty(value = "Set of the expected commands to be executed during the training phase.")
    private Set<String> expectedCommands;
    @ApiModelProperty(value = "Tasks associated with the training phase", required = true, position = 3)
    private List<TaskImport> tasks = new ArrayList<>();
    @ApiModelProperty(value = "Decision matrix associated with the training phase", required = true, position = 4)
    private List<DecisionMatrixRowImport> decisionMatrix;
    @ApiModelProperty(value = "Questions related to the training phase.", required = true)
    private List<AbstractQuestionImport> relatedQuestions;
    @ApiModelProperty(value = "Mitre techniques specified by the instructor")
    private List<MitreTechniqueImport> mitreTechniques;
}
