package utils.autoUnitTestUtil.testGeneration;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import utils.autoUnitTestUtil.cfg.CfgEndBlockNode;
import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.cfg.utils.ASTHelper;
import utils.autoUnitTestUtil.path.MarkedPath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestGeneration {
    public enum Coverage {
        STATEMENT,
        BRANCH
    }
    protected static CompilationUnit compilationUnit;
    protected static ArrayList<ASTNode> funcAstNodeList;
    protected static CfgNode cfgBeginNode;
    protected static CfgEndBlockNode cfgEndNode;
    protected static List<ASTNode> parameters;
    protected static Class<?>[] parameterClasses;
    protected static List<String> parameterNames;
    protected static ASTNode testFunc;
    protected static String classKey;

    protected static boolean isSetup;
    protected static List<ASTNode> originalParameters; // parameters before adding stub vars

    protected static CfgNode findUncoverNode(CfgNode cfgNode, Coverage coverage) {
        switch (coverage) {
            case STATEMENT:
                return MarkedPath.findUncoveredStatement(cfgNode);
            case BRANCH:
                return MarkedPath.findUncoveredBranch(cfgNode);
            default:
                throw new RuntimeException("Invalid coverage type");
        }
    }

    protected static ASTHelper.Coverage getCoverageType(Coverage coverage) {
        switch (coverage) {
            case STATEMENT:
                return ASTHelper.Coverage.STATEMENT;
            case BRANCH:
                return ASTHelper.Coverage.BRANCH;
            default:
                throw new RuntimeException("Invalid coverage");
        }
    }

    protected static void writeDataToFile(String data, String path, boolean append) {
        try {
            FileWriter writer = new FileWriter(path, append);
            writer.write(data);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static String getDataFromFile(String path) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            if ((line = br.readLine()) != null) {
                result.append(line);
            }
            while ((line = br.readLine()) != null) {
                result.append("\n").append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static ArrayList<ASTNode> getFuncAstNodeList() {
//        if (isSetup) {
        return funcAstNodeList;
//        } else {
//            throw new RuntimeException("Value has not been setup");
//        }
    }

    public static CompilationUnit getCompilationUnit() {
        if (isSetup) {
            return compilationUnit;
        } else {
            throw new RuntimeException("Value has not been setup");
        }
    }

    public static MethodDeclaration getTestFunc() {
//        if (isSetup) {
        return (MethodDeclaration) testFunc;
//        } else {
//            throw new RuntimeException("Value has not been setup");
//        }
    }

    protected static String getTotalFunctionCoverageVariableName(MethodDeclaration methodDeclaration, TestGeneration.Coverage coverage) {
        StringBuilder result = new StringBuilder(classKey);
        result.append(methodDeclaration.getReturnType2());
        result.append(methodDeclaration.getName());
        for (int i = 0; i < originalParameters.size(); i++) {
            result.append(originalParameters.get(i));
        }
        if (coverage == TestGeneration.Coverage.STATEMENT) {
            result.append("TotalStatement");
        } else if (coverage == TestGeneration.Coverage.BRANCH) {
            result.append("TotalBranch");
        } else {
            throw new RuntimeException("Invalid Coverage");
        }

        return reformatVariableName(result.toString());
    }

    private static String reformatVariableName(String name) {
        return name.replace(" ", "").replace(".", "")
                .replace("[", "").replace("]", "")
                .replace("<", "").replace(">", "")
                .replace(",", "");
    }
}
