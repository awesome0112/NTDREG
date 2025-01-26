package utils.CIAutil.Utility;

import utils.CIAutil.model.ast.CIANode;
import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.List;

public class Searcher {

    public static JavaNode findJavaNode(List<JavaNode> javaNodes, Integer id) {
        JavaNode javaNode = new JavaNode();

        for(JavaNode obj : javaNodes) {
            if(obj.getId().equals(id)) {
                javaNode = obj;
            }
        }

        return javaNode;
    }

    public static CIANode findNode (List<CIANode> nodes, Integer id) {
        CIANode node = new CIANode();

        for(CIANode obj : nodes) {
            if(obj.getId().equals(id)) {
                node = obj;
            }
        }

        return node;
    }
}
