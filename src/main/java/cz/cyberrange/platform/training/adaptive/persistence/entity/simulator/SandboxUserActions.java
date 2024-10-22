package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SandboxUserActions {
    private Map<Long, Map<Long, List<PhaseUserActions>>> phaseUserActions = new HashMap<>();
}
