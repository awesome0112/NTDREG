package utils.versionCompareUtil.utils;

import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<Integer> convertNodesToNodeIds (List<JavaNode> javaNodes) {
        List<Integer> nodeIds = new ArrayList<>();

        for(JavaNode javaNode : javaNodes) {
            nodeIds.add(javaNode.getId());
        }

        return nodeIds;
    }
}
