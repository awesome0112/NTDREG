package utils.CIAutil.Utility;

import utils.CIAutil.model.ast.CIANode;
import utils.versionCompareUtil.model.ast.dependency.Pair;
import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.List;
import java.util.Set;

public class Getter {

    public static Set<CIANode> gatherImpactFromDependencies(List<CIANode> nodes,
                                                            List<JavaNode> javaNodes,
                                                            Integer totalNodes,
                                                            JavaNode changedNode,
                                                            Set<CIANode> affectedNodes,
                                                            Integer depth) {
//        System.out.println(depth);
//        System.out.println();

        if(depth > 0) {

            for(Pair dependency : changedNode.getDependencyFrom()) {
                if(dependency.getDependency().getMEMBER() > -1) {
                    System.out.println("Caused impact: " + changedNode.getId());
                    System.out.println("-> " + dependency.getNode().getId() + " - depth: " + depth);
                    gatherImpact(nodes, javaNodes, totalNodes, affectedNodes, dependency, depth - 1);
                }
            }

            for(Pair dependency : changedNode.getDependencyTo()) {
                if(dependency.getDependency().getOVERRIDE() > 0) {
                    System.out.println("Caused impact: " + changedNode.getId());
                    System.out.println("-> " + dependency.getNode().getId() + " - depth: " + depth);
                    gatherImpact(nodes, javaNodes, totalNodes, affectedNodes, dependency, depth - 1);
                }
            }
        }

        return affectedNodes;
    }

    private static void gatherImpact(List<CIANode> nodes,
                                     List<JavaNode> javaNodes,
                                     Integer totalNodes,
                                     Set<CIANode> affectedNodes,
                                     Pair dependency,
                                     Integer depth) {
        Integer nodeId = dependency.getNode().getId();
        JavaNode javaNode = Searcher.findJavaNode(javaNodes, nodeId);
        CIANode node = Searcher.findNode(nodes, nodeId);
        if(!affectedNodes.contains(node) || depth > 0){
            affectedNodes.add(Searcher.findNode(nodes, dependency.getNode().getId()));
            affectedNodes.addAll(gatherImpactFromDependencies(nodes, javaNodes, totalNodes, javaNode, affectedNodes, depth));
        }
    }

    private static void gatherFather(List<CIANode> nodes,
                                     List<JavaNode> javaNodes,
                                     Integer totalNodes,
                                     Set<CIANode> affectedNodes,
                                     Pair dependency) {

    }
}
