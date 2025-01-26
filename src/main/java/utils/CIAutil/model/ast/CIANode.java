package utils.CIAutil.model.ast;

import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.List;

public class CIANode {
    private Integer id;

    private String methodSimpleName;

    private Integer weight;

    public CIANode() {
    }

    public CIANode(Integer node, Integer weight, List<JavaNode> allJavaNodes) {
        this.id = node;
        this.weight = weight;
        for (JavaNode javaNode : allJavaNodes) {
            if (javaNode.getEntityClass().equals("JavaMethodNode") && javaNode.getId().equals(node)) {
                methodSimpleName = javaNode.getSimpleName();
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getMethodSimpleName() {
        return methodSimpleName;
    }

    public void setMethodSimpleName(String methodSimpleName) {
        this.methodSimpleName = methodSimpleName;
    }
}
