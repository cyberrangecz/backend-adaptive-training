package cz.muni.ics.kypo.training.adaptive.dto.export.phases.training;

public class DecisionMatrixRowExportDTO {
    private int order;
    private double questionnaireAnswered;
    private double keywordUsed;
    private double completedInTime;
    private double solutionDisplayed;
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
