package utils.versionCompareUtil.utils;

import mrmathami.cia.java.project.JavaProjectSnapshotComparison;
import mrmathami.cia.java.tree.node.JavaMethodNode;
import utils.versionCompareUtil.model.Version;
import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.ArrayList;
import java.util.List;

public class Wrapper {

    public static void applyCompare(JavaNode rootNode,
                                    List<JavaNode> changedNodes,
                                    List<JavaNode> addedNodes,
                                    List<Pair<Integer, JavaNode>> deletedNodes,
                                    List<JavaNode> unchangedNodes,
                                    JavaProjectSnapshotComparison snapshotComparison,
                                    Version version) {
        int projectSize = snapshotComparison.getPreviousSnapshot().getRootNode().getAllNodes().size();
        List<Pair<Integer, Integer>> changedNodesBind = new ArrayList<>();

        //add unchanged nodes
        for(mrmathami.utils.Pair<mrmathami.cia.java.tree.node.JavaNode, mrmathami.cia.java.tree.node.JavaNode> javaNode
                : snapshotComparison.getUnchangedNodes()) {
            if(javaNode.getA() instanceof JavaMethodNode && javaNode.getB() instanceof JavaMethodNode) {
                String oldNode = ((JavaMethodNode) javaNode.getA()).getReturnType().getDescription();
                String newNode = ((JavaMethodNode) javaNode.getB()).getReturnType().getDescription();
                if(!oldNode.equals(newNode)) {
                    changedNodes.add(new JavaNode(javaNode.getA(), "changed", version.getNewVersion()));
                }
            } else {
                unchangedNodes.add(new JavaNode(javaNode.getA(), "unchanged", version.getNewVersion()));
            }
        }

        //add changed nodes
        for(mrmathami.utils.Pair<mrmathami.cia.java.tree.node.JavaNode, mrmathami.cia.java.tree.node.JavaNode> javaNode
                : snapshotComparison.getChangedNodes()) {
            changedNodes.add(new JavaNode(javaNode.getA(), "changed", version.getNewVersion()));
            changedNodesBind.add(new Pair<>(javaNode.getA().getId(), javaNode.getB().getId()));
        }

        //add added nodes
        for(mrmathami.cia.java.tree.node.JavaNode javaNode : snapshotComparison.getAddedNodes()) {
            addedNodes.add(new JavaNode(javaNode, "added", version.getNewVersion()));
        }

        //add removed nodes
        for(mrmathami.cia.java.tree.node.JavaNode javaNode : snapshotComparison.getRemovedNodes()) {
            deletedNodes.add(new Pair<>(javaNode.getParent().getId(), new JavaNode(javaNode, "deleted", version.getOldVersion())));
        }

        //bind changed nodes and added nodes to nodes tree
        Utils.convertNode(rootNode, changedNodes, addedNodes);

        //bind deleted nodes to nodes tree
        bindRemovedNode(rootNode, deletedNodes, changedNodesBind, projectSize);
    }

    //Node binding
    public static void bindRemovedNode(JavaNode rootNode,
                                       List<Pair<Integer, JavaNode>> deletedNodes,
                                       List<Pair<Integer, Integer>> changedNodesBind,
                                       int size) {
        int bindId = size;
        for (Pair<Integer, JavaNode> node : deletedNodes) {
            int parentId = node.first();
            int newParentId;
            for(Pair<Integer, Integer> bind : changedNodesBind) {
                if(parentId == bind.second()) {
                    newParentId = bind.first();
                    addNode(rootNode, node.second(), newParentId, bindId);
                }
            }
        }
    }

    public static void addNode(JavaNode rootNode, JavaNode addedNode, int parentId, int bindId) {
        if(rootNode.getId() == parentId) {
            bindId(addedNode, bindId);
            rootNode.addChildren(addedNode);
        } else {
            for (Object javaNode : rootNode.getChildren()) {
                if (javaNode instanceof JavaNode) {
                    addNode((JavaNode) javaNode, addedNode, parentId, bindId);
                }
            }

        }
    }

    public static void bindId(JavaNode addedNode, int bindId) {
        addedNode.setId(addedNode.getId()*(-1));
        addedNode.setStatus("deleted");

        for(Object childNode : addedNode.getChildren()) {
            if(childNode instanceof JavaNode) {
                bindId((JavaNode) childNode, bindId);
            }
        }
    }
}
