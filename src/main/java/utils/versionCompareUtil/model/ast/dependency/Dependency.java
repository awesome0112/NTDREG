package utils.versionCompareUtil.model.ast.dependency;

public class Dependency {

    private Integer callerNode;

    private Integer calleeNode;

    private DependencyCountTable type;

    public Dependency() {
    }

    public Dependency(Integer callerNode, Integer calleeNode) {
        this.callerNode = callerNode;
        this.calleeNode = calleeNode;
    }

    public Dependency(Integer callerNode, Integer calleeNode, DependencyCountTable javaDependency) {
        this.callerNode = callerNode;
        this.calleeNode = calleeNode;
        this.type = javaDependency;
    }

    public Integer getCallerNode() {
        return callerNode;
    }

    public void setCallerNode(Integer callerNode) {
        this.callerNode = callerNode;
    }

    public Integer getCalleeNode() {
        return calleeNode;
    }

    public void setCalleeNode(Integer calleeNode) {
        this.calleeNode = calleeNode;
    }

    public DependencyCountTable getType() {
        return type;
    }

    public void setType(DependencyCountTable type) {
        this.type = type;
    }
}
