package cz.muni.ics.kypo.training.adaptive;

public enum URIPath {
    QUESTIONNAIRE_EVALUATION("/training-runs/{runId}/questionnaire-evaluation");

    private final String uri;

    URIPath(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
