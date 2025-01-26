package utils.versionCompareUtil.utils;

import utils.versionCompareUtil.model.ast.dependency.Dependency;
import utils.versionCompareUtil.model.ast.dependency.Pair;
import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.ArrayList;
import java.util.List;

public class Getter {

    public static List getDependency(JavaNode rootNode) {
        List<Dependency> dependencies = new ArrayList<>();

        if(!rootNode.getStatus().equals("deleted")) {
            for(Pair node : rootNode.getDependencyTo()) {
                dependencies.add(new Dependency(rootNode.getId(), node.getNode().getId(), node.getDependency()));
            }


            for (Object javaNode : rootNode.getChildren()) {
                if(javaNode instanceof JavaNode) {
                    dependencies.addAll(getDependency((JavaNode) javaNode));
                }
            }
        }

        return dependencies;
    }
}
