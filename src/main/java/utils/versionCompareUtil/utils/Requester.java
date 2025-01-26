package utils.versionCompareUtil.utils;

import utils.CIAutil.Utils;
import utils.CIAutil.model.CIAResponse;
import utils.CIAutil.model.ast.CIANode;
import utils.versionCompareUtil.model.ast.dependency.Dependency;
import utils.versionCompareUtil.model.ast.node.JavaNode;


import java.util.List;

public class Requester {

    public static List<CIANode> getImpactedNodes (List<JavaNode> javaNodes, List<JavaNode> changedNodes, List<Dependency> dependencies) {

        List<Integer> changedNodesId = Converter.convertNodesToNodeIds(changedNodes);

        CIAResponse ciaResponse = Utils.findImpact(javaNodes, dependencies,javaNodes.size(), changedNodesId);

        return ciaResponse.getNodes();
    }

}
