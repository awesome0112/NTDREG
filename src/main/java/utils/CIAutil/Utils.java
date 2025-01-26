package utils.CIAutil;



import utils.CIAutil.Utility.Getter;
import utils.CIAutil.Utility.Searcher;
import utils.CIAutil.model.CIAResponse;
import utils.CIAutil.model.ast.CIANode;
import utils.CIAutil.model.ast.utility.Utility;
import utils.versionCompareUtil.model.ast.dependency.Dependency;
import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.*;

public class Utils {

    public static CIAResponse findImpact(List<JavaNode> javaNodes, List<Dependency> dependencies, Integer totalNodes, List<Integer> changedNodes) {
        List<CIANode> nodes = calculate(javaNodes, dependencies, totalNodes).getNodes();
        Set<CIANode> affectedNodes = new HashSet<>();

        for(Integer javaNode : changedNodes) {
            JavaNode changedNode = Searcher.findJavaNode(javaNodes, javaNode);
            Getter.gatherImpactFromDependencies(nodes, javaNodes, totalNodes, changedNode, affectedNodes, 3);
        }

        CIAResponse response = Utility.convertSetToNodes(affectedNodes);

        return response;
    }

    public static CIAResponse calculate(List<JavaNode> javaNodes, List<Dependency> dependencies, Integer totalNodes) {
        CIAResponse response = new CIAResponse();
        Map<Integer, Integer> nodes = new HashMap<>(totalNodes);

        for(int i = 0; i < totalNodes; ++i) {
            nodes.put(i, 0);
        }

        for(Dependency dependency : dependencies) {
            Integer calleeNodeId = dependency.getCalleeNode();
            nodes.put(calleeNodeId, nodes.getOrDefault(calleeNodeId, 0) + Utility.calculateWeight(dependency.getType()));
        }

        for(Dependency dependency : dependencies) {
            if(dependency.getType().getMEMBER().equals(1)) {
                Integer calleeNodeId = dependency.getCalleeNode();
                Integer callerNodeId = dependency.getCallerNode();
                nodes.put(callerNodeId, nodes.getOrDefault(callerNodeId, 0) + nodes.getOrDefault(calleeNodeId, 0));
            }
        }

        response = Utility.convertMapToNodes(javaNodes, nodes);

        return response;
    }
}
