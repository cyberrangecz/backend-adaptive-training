package cz.muni.ics.kypo.training.adaptive.dto.sankeygraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SankeyGraphDTO {

    private List<NodeDTO> nodes = new ArrayList<>();
    private Set<LinkDTO> links = new HashSet<>();

    public SankeyGraphDTO() {
    }

    public SankeyGraphDTO(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public Set<LinkDTO> getLinks() {
        return links;
    }

    public void setLinks(Set<LinkDTO> links) {
        this.links = links;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }
}
