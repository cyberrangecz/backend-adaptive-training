package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import lombok.Data;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SandboxUserActions {
    private Map<Long, Map<Long, List<PhaseUserActions>>> phaseUserActions = new HashMap<>();
}
