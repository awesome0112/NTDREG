package utils.versionCompareUtil.model.ast.utility;

import utils.versionCompareUtil.model.ast.annotation.JavaAnnotation;
import utils.versionCompareUtil.model.ast.dependency.DependencyCountTable;
import utils.versionCompareUtil.model.ast.dependency.Pair;
import utils.versionCompareUtil.model.ast.node.JavaNode;
import utils.versionCompareUtil.model.ast.node.Node;
import utils.versionCompareUtil.model.ast.type.JavaType;
import mrmathami.annotations.Nonnull;
import mrmathami.cia.java.jdt.tree.annotate.Annotate;
import mrmathami.cia.java.jdt.tree.node.AbstractNode;
import mrmathami.cia.java.jdt.tree.type.AbstractType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utility {

    public static void printList (List list) {

        for(Object obj : list) {
            if(obj instanceof JavaNode) {
                System.out.println(((JavaNode) obj).getQualifiedName());
            }
        }
    }

    public static List<JavaNode> convertJavaNodeList (List nodeList, String path) {
        List<JavaNode> nodes = new ArrayList<>();

        for(Object javaNode : nodeList) {
            if(javaNode instanceof mrmathami.cia.java.tree.node.JavaNode) {
                nodes.add(new JavaNode((AbstractNode) javaNode, true, path));
            }
        }

        return nodes;
    }

    public static List<JavaNode> convertJavaNodeSet (Set<mrmathami.cia.java.tree.node.JavaNode> nodeList) {
        List<JavaNode> nodes = new ArrayList<>();

        for(mrmathami.cia.java.tree.node.JavaNode javaNode : nodeList) {
            nodes.add(new JavaNode(javaNode));
        }

        return nodes;
    }

    public static List<JavaNode> convertJavaNodeSet (Set<mrmathami.cia.java.tree.node.JavaNode> nodeList, String status, int bindId) {
        List<JavaNode> nodes = new ArrayList<>();

        for(mrmathami.cia.java.tree.node.JavaNode javaNode : nodeList) {
            JavaNode temp = new JavaNode(javaNode, status);
            temp.setId(bindId + temp.getId());
            nodes.add(temp);
        }

        return nodes;
    }

    public static List<JavaNode> convertJavaNodeSet (Set<mrmathami.cia.java.tree.node.JavaNode> nodeList, String status, int bindId, String path) {
        List<JavaNode> nodes = new ArrayList<>();

        for(mrmathami.cia.java.tree.node.JavaNode javaNode : nodeList) {
            JavaNode temp = new JavaNode(javaNode, status, path);
            temp.setId(temp.getId()*(-1));
            nodes.add(temp);
        }

        return nodes;
    }

    @Nonnull
    public static List<JavaNode> convertAbstractNode(List<AbstractNode> abstractNodeList, boolean getDependency) {
        List<JavaNode> javaNodeList = new ArrayList<>();
        for(AbstractNode node : abstractNodeList) {

            javaNodeList.add(new JavaNode(node, true, getDependency));
        }

        return javaNodeList;
    }

    public static List<JavaNode> convertAbstractNode(List<AbstractNode> abstractNodeList, boolean getDependency, String rootPath) {
        List<JavaNode> javaNodeList = new ArrayList<>();
        for(AbstractNode node : abstractNodeList) {
            Integer parent = node.getParent().getId();
            String path = rootPath;
            if(node.getEntityClass().equals("JavaClassNode") || node.getEntityClass().equals("JavaInterfaceNode")) {
                path = rootPath + "/" + node.getSourceFile().getRelativePath().toString();
            }
            javaNodeList.add(new JavaNode(node, true, parent, path));
        }

        return javaNodeList;
    }

    @Nonnull
    public static List<JavaNode> convertToAllNodes(List<AbstractNode> abstractNodeList, String path) {
        List<JavaNode> javaNodeList = new ArrayList<>();
        for(AbstractNode node : abstractNodeList) {
            javaNodeList.add(new JavaNode(node, false, path));
        }

        return javaNodeList;
    }

    @Nonnull
    public static List<JavaNode> convertToAllNodes(List<AbstractNode> abstractNodeList) {
        List<JavaNode> javaNodeList = new ArrayList<>();
        for(AbstractNode node : abstractNodeList) {
            javaNodeList.add(new JavaNode(node, false, ""));
        }

        return javaNodeList;
    }

    @Nonnull
    public static List<Integer> convertChildren(List<AbstractNode> abstractNodeList) {
        List<Integer> javaNodeList = new ArrayList<>();
        for(AbstractNode node : abstractNodeList) {
            javaNodeList.add(node.getId());
        }

        return javaNodeList;
    }

    @Nonnull
    public static List<Node> convertNode(List<AbstractNode> abstractNodeList) {
        List<Node> children = new ArrayList<>();
        for(AbstractNode node : abstractNodeList) {
            children.add(new Node(node));
        }

        return children;
    }

    @Nonnull
    public static List convertMap(Map<AbstractNode, mrmathami.cia.java.jdt.tree.dependency.DependencyCountTable> nodeList) {
        List<Pair> javaNodeList = new ArrayList<>();
        for(AbstractNode node : nodeList.keySet()) {
            DependencyCountTable dependencyCountTable = new DependencyCountTable(nodeList.get(node));
            javaNodeList.add(new Pair(new Node(node), dependencyCountTable));
        }
        return javaNodeList;
    }

    @Nonnull
    public static List<String> convertModifiers(Set modifierSet) {
        List<String> modifierList = new ArrayList<>();

        for(Object obj : modifierSet) {
            modifierList.add(obj.toString());
        }

        return modifierList;
    }

    public static List<JavaAnnotation> convertAnnotates(List<Annotate> annotates) {
        List<JavaAnnotation> javaAnnotationList = new ArrayList<>();

        for(Annotate annotate : annotates) {
            javaAnnotationList.add(new JavaAnnotation(annotate));
        }
        return javaAnnotationList;
    }

    public static List<JavaType> convertParameters(List<AbstractType> parameters) {
        List<JavaType> javaTypeList = new ArrayList<>();

        for(AbstractType parameter : parameters) {
            javaTypeList.add(new JavaType(parameter));
        }

        return javaTypeList;
    }

    public static List<JavaType> convertArguments(List<AbstractType> argumentList) {
        List<JavaType> arguments = new ArrayList<>();

        for(AbstractType abstractType : argumentList) {
            arguments.add(new JavaType(abstractType));
        }

        return arguments;
    }

    public static void findDependency(mrmathami.cia.java.tree.node.JavaNode javaRootNode) {

        printDependency(javaRootNode.getDependencyTo());

        for(mrmathami.cia.java.tree.node.JavaNode javaNode : javaRootNode.getChildren())
        {
            findDependency(javaNode);
        }
    }

    private static void printDependency(Map Dependencies) {
        Set<AbstractNode> nodes = Dependencies.keySet();


        for(AbstractNode node : nodes) {
            mrmathami.cia.java.jdt.tree.dependency.DependencyCountTable dependencyCountTable = (mrmathami.cia.java.jdt.tree.dependency.DependencyCountTable) Dependencies.get(node);
//            System.out.println(dependencyCountTable.getCount(JavaDependency.USE));
        }
    }
}