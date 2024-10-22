package cz.cyberrange.platform.training.adaptive.persistence.entity.simulator;

import cz.cyberrange.platform.training.adaptive.persistence.entity.simulator.imports.AbstractPhaseImport;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InstanceModelUpdate {
    private String cacheId;
    private List<AbstractPhaseImport> phases = new ArrayList<>();
}
