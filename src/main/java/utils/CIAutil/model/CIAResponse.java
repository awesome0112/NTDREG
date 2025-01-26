package utils.CIAutil.model;



import utils.CIAutil.model.ast.CIANode;

import java.util.List;

public class CIAResponse {

    List<CIANode> nodes;

    public CIAResponse() {
    }

    public CIAResponse(List<CIANode> nodes) {
        this.nodes = nodes;
    }

    public List<CIANode> getNodes() {
        return nodes;
    }

    public void setNodes(List<CIANode> nodes) {
        this.nodes = nodes;
    }
}
