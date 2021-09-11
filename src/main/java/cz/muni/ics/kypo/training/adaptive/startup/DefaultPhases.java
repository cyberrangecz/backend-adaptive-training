package cz.muni.ics.kypo.training.adaptive.startup;

import javax.validation.Valid;

public class DefaultPhases {
    @Valid
    private IntroInfoPhase introInfoPhase;
    @Valid
    private GetAccessPhase getAccessPhase;

    public IntroInfoPhase getIntroInfoPhase() {
        return introInfoPhase;
    }

    public void setIntroInfoPhase(IntroInfoPhase introInfoPhase) {
        this.introInfoPhase = introInfoPhase;
    }

    public GetAccessPhase getGetAccessPhase() {
        return getAccessPhase;
    }

    public void setGetAccessPhase(GetAccessPhase getAccessPhase) {
        this.getAccessPhase = getAccessPhase;
    }
}
