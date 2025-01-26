package utils.versionCompareUtil.utils;

import mrmathami.annotations.Nonnull;
import mrmathami.cia.java.JavaCiaException;
import mrmathami.cia.java.jdt.ProjectBuilder;
import mrmathami.cia.java.jdt.project.builder.parameter.BuildInputSources;
import mrmathami.cia.java.jdt.project.builder.parameter.JavaBuildParameter;
import mrmathami.cia.java.jdt.tree.node.AbstractNode;
import mrmathami.cia.java.project.JavaProjectSnapshot;
import mrmathami.cia.java.project.JavaProjectSnapshotComparison;
import mrmathami.cia.java.project.JavaSourceFileType;
import mrmathami.cia.java.tree.dependency.JavaDependency;
import mrmathami.cia.java.tree.dependency.JavaDependencyWeightTable;
import utils.CIAutil.model.ast.CIANode;
import utils.FilePath;
import utils.uploadUtil.ConcolicUploadUtil;
import utils.versionCompareUtil.model.VersionCompareResponse;
import utils.versionCompareUtil.model.Version;
import utils.versionCompareUtil.model.ast.dependency.Dependency;
import utils.versionCompareUtil.model.ast.node.JavaNode;
import utils.versionCompareUtil.model.ast.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Utils {

    public static final JavaDependencyWeightTable DEPENDENCY_WEIGHT_TABLE = JavaDependencyWeightTable.of(Map.of(
            JavaDependency.USE, 1.0,
            JavaDependency.MEMBER, 1.0,
            JavaDependency.INHERITANCE, 4.0,
            JavaDependency.INVOCATION, 4.0,
            JavaDependency.OVERRIDE, 1.0
    ));
    public static final JavaDependencyWeightTable DEPENDENCY_IMPACT_TABLE = JavaDependencyWeightTable.of(Map.of(
            JavaDependency.USE, 0.4,
            JavaDependency.MEMBER, 0.2,
            JavaDependency.INHERITANCE, 0.3,
            JavaDependency.INVOCATION, 0.3,
            JavaDependency.OVERRIDE, 0.3
    ));

    public static VersionCompareResponse getCompare(File V1File, File V2File) throws JavaCiaException, IOException {
        Version version = new Version();

        String oldVersionPath = FilePath.uploadedProjectPath + "\\" + ConcolicUploadUtil.javaUnzipFile(V1File.getPath(), FilePath.uploadedProjectPath);
        String newVersionPath = FilePath.uploadedProjectPath + "\\" + ConcolicUploadUtil.javaUnzipFile(V2File.getPath(), FilePath.uploadedProjectPath);

        version.setOldVersion(oldVersionPath);
        version.setNewVersion(newVersionPath);

        return getCompare(version);
    }

    private static VersionCompareResponse getCompare(Version version) throws JavaCiaException, IOException {

        //Old version path
        final Path inputPathA = Path.of(version.getOldVersion());
        final BuildInputSources inputSourcesA = new BuildInputSources(inputPathA);
        Utils.getFileList(inputSourcesA.createModule("core", inputPathA), inputPathA);

        //New version path
        final Path inputPathB = Path.of(version.getNewVersion());
        System.out.println(version.getNewVersion());
        final BuildInputSources inputSourcesB = new BuildInputSources(inputPathB);
        Utils.getFileList(inputSourcesB.createModule("core", inputPathB), inputPathB);

        //Compare two version
        final JavaProjectSnapshot projectSnapshotA = ProjectBuilder.createProjectSnapshot("JSON-java-before",
                DEPENDENCY_WEIGHT_TABLE, inputSourcesA, Set.of(new JavaBuildParameter(List.of(), false)));

        final JavaProjectSnapshot projectSnapshotB = ProjectBuilder.createProjectSnapshot("JSON-java-after",
                DEPENDENCY_WEIGHT_TABLE, inputSourcesB, Set.of(new JavaBuildParameter(List.of(), false)));

        JavaProjectSnapshotComparison snapshotComparison = ProjectBuilder.createProjectSnapshotComparison(
                "compare", projectSnapshotB, projectSnapshotA, DEPENDENCY_IMPACT_TABLE);

        //Node initialization
        int projectSize = snapshotComparison.getPreviousSnapshot().getRootNode().getAllNodes().size();
        List<JavaNode> changedNodes = new ArrayList<>();
        List<JavaNode> addedNodes = new ArrayList<>();
        List<JavaNode> deletedNodes = Utility.convertJavaNodeSet(snapshotComparison.getRemovedNodes(), "deleted", projectSize, version.getOldVersion());
        List<Pair<Integer, JavaNode>> removedNodes = new ArrayList<>();
        List<JavaNode> unchangedNodes = new ArrayList<>();
        String path = version.getNewVersion();

        JavaNode rootNode = new JavaNode((AbstractNode) projectSnapshotB.getRootNode(),
                true,
                path);

        //Bind to Tree Node
        Wrapper.applyCompare(rootNode, changedNodes, addedNodes, removedNodes, unchangedNodes, snapshotComparison, version);

        //Find impact
        List<Dependency> dependencies = Getter.getDependency(rootNode);
        List<JavaNode> javaNodes = Utility.convertToAllNodes((List<AbstractNode>) projectSnapshotB.getRootNode().getAllNodes());
        List<CIANode> nodeWeights = Requester.getImpactedNodes(javaNodes, changedNodes, dependencies);
        nodeWeights.addAll(Requester.getImpactedNodes(javaNodes, addedNodes, dependencies));

        VersionCompareResponse response = new VersionCompareResponse(changedNodes, deletedNodes, addedNodes, null, null, null, dependencies, nodeWeights, rootNode);
        return response;
    }

    private static void getFileList(@Nonnull BuildInputSources.InputModule inputModule, @Nonnull Path dir)
            throws IOException {
        try (final DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (final Path path : stream) {
                if (path.toFile().isDirectory()) {
//					System.out.println(path.toFile().getName());
                    getFileList(inputModule, path);
                } else if (path.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".java")) {
                    inputModule.createFile(path, JavaSourceFileType.JAVA);
                }
            }
        }
    }

    public static JavaNode convertNode(JavaNode rootNode,
                                       List<JavaNode> changedNodes,
                                       List<JavaNode> addedNodes) {

        for(JavaNode javaNode : changedNodes) {
            changeStatus(rootNode, javaNode, "changed");
        }

        for(JavaNode javaNode : addedNodes) {
            changeStatus(rootNode, javaNode, "added");
        }

        return rootNode;
    }

    private static void changeStatus(JavaNode javaNode, JavaNode statusNode, String status) {
        if(javaNode.getUniqueName().equals(statusNode.getUniqueName())) {
            System.out.println(status);
            System.out.println(javaNode.getUniqueName() + " " + javaNode.getId());
            System.out.println(statusNode.getUniqueName() + " " + statusNode.getId());
            System.out.println();
            javaNode.setStatus(status);
        } else {
            for(Object childNode: javaNode.getChildren()) {
                if(childNode instanceof JavaNode) {
                    changeStatus((JavaNode) childNode, statusNode, status);
                }
            }
        }
    }
}
