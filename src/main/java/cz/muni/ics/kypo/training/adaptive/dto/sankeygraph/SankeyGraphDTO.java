package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

import java.util.ArrayList;
import java.util.List;

public class SankeyGraphDTO {

    private List<NodeDTO> nodes = new ArrayList<>();
    private List<LinksDTO> links = new ArrayList<>();

    public SankeyGraphDTO() {
    }

    public SankeyGraphDTO(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }


}
