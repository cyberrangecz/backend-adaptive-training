package cz.muni.ics.kypo.training.adaptive.dto;

public class DecisionMatrixRowDTO {
    private long id;
    private int order;
    private double assessmentAnswered;
    private double keywordUsed;
    private double completedInTime;
    private double solutionDisplayed;
    private double wrongFlags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getAssessmentAnswered() {
        return assessmentAnswered;
    }

    public void setAssessmentAnswered(double assessmentAnswered) {
        this.assessmentAnswered = assessmentAnswered;
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

    public double getWrongFlags() {
        return wrongFlags;
    }

    public void setWrongFlags(double wrongFlags) {
        this.wrongFlags = wrongFlags;
    }

    @Override
    public String toString() {
        return "DecisionMatrixRow{" +
                "id=" + id +
                ", order=" + order +
                ", assessmentAnswered=" + assessmentAnswered +
                ", keywordUsed=" + keywordUsed +
                ", completedInTime=" + completedInTime +
                ", solutionDisplayed=" + solutionDisplayed +
                ", wrongFlags=" + wrongFlags +
                '}';
    }
}
