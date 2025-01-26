package utils.versionCompareUtil.model.ast.node;

public class NodeWeight {
    private Integer node;

    private Integer weight;

    public NodeWeight() {
    }

    public NodeWeight(Integer node, Integer weight) {
        this.node = node;
        this.weight = weight;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
