package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SankeyGraphDTO {

    private List<NodeDTO> nodes = new ArrayList<>();
    private List<LinkDTO> links = new ArrayList<>();

    public SankeyGraphDTO() {
    }

    public SankeyGraphDTO(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public List<LinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDTO> links) {
        this.links = links;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }
}
