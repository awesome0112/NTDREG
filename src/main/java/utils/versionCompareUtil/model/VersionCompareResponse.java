package utils.versionCompareUtil.model;

import utils.versionCompareUtil.model.ast.node.JavaNode;

import java.util.List;
import java.util.Set;

public class VersionCompareResponse {
    private List changedNodes;

    private List xmlChangedNodes;

    private List deletedNodes;

    private List xmlDeletedNodes;

    private List addedNodes;

    private List xmlAddedNodes;

    private List dependencies;

    private List impactedNodes;

    private JavaNode rootNode;

    public VersionCompareResponse() {
    }

    public VersionCompareResponse(List changedNodes, List deletedNodes, List addedNodes, List xmlChangedNodes, List xmlDeletedNodes, List xmlAddedNodes) {
        this.changedNodes = changedNodes;
        this.deletedNodes = deletedNodes;
        this.addedNodes = addedNodes;
        this.xmlChangedNodes = xmlChangedNodes;
        this.xmlDeletedNodes = xmlDeletedNodes;
        this.xmlAddedNodes = xmlAddedNodes;
    }

    public VersionCompareResponse(List changedNodes, List deletedNodes, List addedNodes, List xmlChangedNodes, List xmlDeletedNodes, List xmlAddedNodes, JavaNode rootNode) {
        this.changedNodes = changedNodes;
        this.deletedNodes = deletedNodes;
        this.addedNodes = addedNodes;
        this.rootNode = rootNode;
        this.xmlChangedNodes = xmlChangedNodes;
        this.xmlDeletedNodes = xmlDeletedNodes;
        this.xmlAddedNodes = xmlAddedNodes;
    }

    public VersionCompareResponse(List changedNodes, List deletedNodes, List addedNodes, List xmlChangedNodes, List xmlDeletedNodes, List xmlAddedNodes, List dependencies, JavaNode rootNode) {
        this.changedNodes = changedNodes;
        this.deletedNodes = deletedNodes;
        this.addedNodes = addedNodes;
        this.dependencies = dependencies;
        this.rootNode = rootNode;
        this.xmlChangedNodes = xmlChangedNodes;
        this.xmlDeletedNodes = xmlDeletedNodes;
        this.xmlAddedNodes = xmlAddedNodes;
    }

    public VersionCompareResponse(List changedNodes, List deletedNodes, List addedNodes, List xmlChangedNodes, List xmlDeletedNodes, List xmlAddedNodes, List dependencies, List impactedNodes, JavaNode rootNode) {
        this.changedNodes = changedNodes;
        this.deletedNodes = deletedNodes;
        this.addedNodes = addedNodes;
        this.dependencies = dependencies;
        this.impactedNodes = impactedNodes;
        this.rootNode = rootNode;
        this.xmlChangedNodes = xmlChangedNodes;
        this.xmlDeletedNodes = xmlDeletedNodes;
        this.xmlAddedNodes = xmlAddedNodes;

    }

    public List getChangedNodes() {
        return changedNodes;
    }

    public void setChangedNodes(List changedNodes) {
        this.changedNodes = changedNodes;
    }

    public List getDeletedNodes() {
        return deletedNodes;
    }

    public void setDeletedNodes(List deletedNodes) {
        this.deletedNodes = deletedNodes;
    }

    public List getAddedNodes() {
        return addedNodes;
    }

    public void setAddedNodes(List addedNodes) {
        this.addedNodes = addedNodes;
    }

    public JavaNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(JavaNode rootNode) {
        this.rootNode = rootNode;
    }

    public List getDependencies() {
        return dependencies;
    }

    public void setDependencies(List dependencies) {
        this.dependencies = dependencies;
    }

    public List getImpactedNodes() {
        return impactedNodes;
    }

    public void setImpactedNodes(List impactedNodes) {
        this.impactedNodes = impactedNodes;
    }
}
