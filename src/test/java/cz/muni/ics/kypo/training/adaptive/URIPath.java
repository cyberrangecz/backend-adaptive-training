package cz.muni.ics.kypo.training.adaptive;

public enum URIPath {
    QUESTIONNAIRE_EVALUATION("/training-runs/{runId}/questionnaire-evaluation"),
    PHASES("/training-definitions/{definitionId}/phases"),
    PHASES_ID("/training-definitions/{definitionId}/phases/{phaseId}"),
    PHASES_ID_INFO("/training-definitions/{definitionId}/phases/{phaseId}/info"),
    PHASES_ID_TRAINING("/training-definitions/{definitionId}/phases/{phaseId}/training"),
    PHASES_ID_QUESTIONNAIRE("/training-definitions/{definitionId}/phases/{phaseId}/questionnaire"),
    PHASES_ID_MOVE_TO("/training-definitions/{definitionId}/phases/{phaseIdFrom}/move-to/{newPosition}"),
    TASKS("/training-definitions/{definitionId}/phases/{phaseId}/tasks"),
    TASKS_ID("/training-definitions/{definitionId}/phases/{phaseId}/tasks/{taskId}"),
    TASKS_ID_MOVE_TO("/training-definitions/{definitionId}/phases/{phaseId}/tasks/{taskIdFrom}/move-to/{newPosition}");

    private final String uri;

    URIPath(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
