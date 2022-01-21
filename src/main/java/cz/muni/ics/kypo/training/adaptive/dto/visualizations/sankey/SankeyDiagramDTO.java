package cz.muni.ics.kypo.training.adaptive.dto.visualizations.sankey;

import java.util.ArrayList;
import java.util.List;

public class SankeyDiagramDTO {

    private List<NodeDTO> nodes = new ArrayList<>();
    private List<LinkDTO> links = new ArrayList<>();

    public SankeyDiagramDTO() {
    }

    public SankeyDiagramDTO(List<NodeDTO> nodes, List<LinkDTO> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public List<NodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<LinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDTO> links) {
        this.links = links;
    }
}
