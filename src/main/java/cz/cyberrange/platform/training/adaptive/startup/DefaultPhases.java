package cz.cyberrange.platform.training.adaptive.startup;

import javax.validation.Valid;

public class DefaultPhases {
    @Valid
    private DefaultInfoPhase defaultInfoPhase;
    @Valid
    private DefaultAccessPhase defaultAccessPhase;

    public DefaultInfoPhase getDefaultInfoPhase() {
        return defaultInfoPhase;
    }

    public void setDefaultInfoPhase(DefaultInfoPhase defaultInfoPhase) {
        this.defaultInfoPhase = defaultInfoPhase;
    }

    public DefaultAccessPhase getDefaultAccessPhase() {
        return defaultAccessPhase;
    }

    public void setDefaultAccessPhase(DefaultAccessPhase defaultAccessPhase) {
        this.defaultAccessPhase = defaultAccessPhase;
    }
}
