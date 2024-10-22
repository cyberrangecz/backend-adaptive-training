package cz.cyberrange.platform.training.adaptive.api.dto.imports.phases.training;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = "DecisionMatrixRowImportDTO"
)
@JsonIgnoreProperties({"id"})
public class DecisionMatrixRowImportDTO {
    @ApiModelProperty(value = "Order of row in a decision matrix", required = true, example = "1")
    private int order;
    @ApiModelProperty(value = "It determines how important the answers of the questions in questionnaires are", required = true, example = "0.5")
    private double questionnaireAnswered;
    @ApiModelProperty(value = "It determines how important it is whether the player used the keyword", required = true, example = "0.5")
    private double keywordUsed;
    @ApiModelProperty(value = "It determines how important it is whether the player completed the task in time", required = true, example = "0.5")
    private double completedInTime;
    @ApiModelProperty(value = "It determines how important it is whether the player displayed the solution of the task they were solving", required = true, example = "0.5")
    private double solutionDisplayed;
    @ApiModelProperty(value = "It determines how important the number of wrong answers are", required = true, example = "0.5")
    private double wrongAnswers;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getQuestionnaireAnswered() {
        return questionnaireAnswered;
    }

    public void setQuestionnaireAnswered(double questionnaireAnswered) {
        this.questionnaireAnswered = questionnaireAnswered;
    }

    public double getKeywordUsed() {
        return keywordUsed;
    }

    public void setKeywordUsed(double keywordUsed) {
        this.keywordUsed = keywordUsed;
    }

    public double getCompletedInTime() {
        return completedInTime;
    }

    public void setCompletedInTime(double completedInTime) {
        this.completedInTime = completedInTime;
    }

    public double getSolutionDisplayed() {
        return solutionDisplayed;
    }

    public void setSolutionDisplayed(double solutionDisplayed) {
        this.solutionDisplayed = solutionDisplayed;
    }

    public double getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(double wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    @Override
    public String toString() {
        return "DecisionMatrixRow{" +
                "order=" + order +
                ", questionnaireAnswered=" + questionnaireAnswered +
                ", keywordUsed=" + keywordUsed +
                ", completedInTime=" + completedInTime +
                ", solutionDisplayed=" + solutionDisplayed +
                ", wrongAnswers=" + wrongAnswers +
                '}';
    }
}
