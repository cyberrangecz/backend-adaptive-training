package cz.muni.ics.kypo.training.adaptive.domain.simulator;

import cz.muni.ics.kypo.training.adaptive.domain.simulator.imports.AbstractPhaseImport;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class InstanceModelUpdate {
    private String cacheId;
    private List<AbstractPhaseImport> phases = new ArrayList<>();
}
