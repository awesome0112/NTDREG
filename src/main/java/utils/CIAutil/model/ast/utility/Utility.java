package utils.CIAutil.model.ast.utility;



import utils.CIAutil.model.CIAResponse;
import utils.CIAutil.model.ast.CIANode;
import utils.versionCompareUtil.model.ast.dependency.DependencyCountTable;
import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utility {

    public static Integer calculateWeight(DependencyCountTable dependencyCountTable) {
        Integer weight =
                dependencyCountTable.getINHERITANCE()*4
                + dependencyCountTable.getINVOCATION()*4
                + dependencyCountTable.getMEMBER()
                + dependencyCountTable.getOVERRIDE()
                + dependencyCountTable.getSPRING()
                + dependencyCountTable.getUSE()
                ;
        return weight;
    }

    public static CIAResponse convertMapToNodes(List<JavaNode> javaNodes, Map<Integer, Integer> nodes) {
        List<CIANode> nodeList = new ArrayList<>();

        for(Integer id : nodes.keySet()) {
            nodeList.add(new CIANode(id, nodes.get(id), javaNodes));
        }

        return new CIAResponse(nodeList);
    }

    public static CIAResponse convertSetToNodes(Set<CIANode> nodes) {
        List<CIANode> nodeList = new ArrayList<>();

        for(CIANode node : nodes) {
            nodeList.add(node);
        }

        return new CIAResponse(nodeList);
    }
}
