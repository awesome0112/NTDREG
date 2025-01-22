package utils.autoUnitTestUtil.testGeneration;

import controller.ChooseToolController;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import utils.FilePath;
import utils.autoUnitTestUtil.ast.Expression.MethodInvocationNode;
import utils.autoUnitTestUtil.cfg.CfgBlockNode;
import utils.autoUnitTestUtil.cfg.CfgBoolExprNode;
import utils.autoUnitTestUtil.cfg.CfgEndBlockNode;
import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.cfg.utils.ASTHelper;
import utils.autoUnitTestUtil.cfg.utils.CfgUtils;
import utils.autoUnitTestUtil.path.FindPath;
import utils.autoUnitTestUtil.path.MarkedPath;
import utils.autoUnitTestUtil.cfg.utils.ProjectParser;
import utils.autoUnitTestUtil.path.MarkedStatement;
import utils.autoUnitTestUtil.path.Path;
import utils.autoUnitTestUtil.symbolicExecution.SymbolicExecution;
import utils.autoUnitTestUtil.testDriver.TestDriverGenerator;
import utils.autoUnitTestUtil.testDriver.TestDriverRunner;
import utils.autoUnitTestUtil.testDriver.TestDriverUtils;
import utils.autoUnitTestUtil.testResult.CoveredStatement;
import utils.autoUnitTestUtil.testResult.TestData;
import utils.autoUnitTestUtil.testResult.TestResult;
import utils.autoUnitTestUtil.utils.Utils;
import utils.uploadUtil.ConcolicUploadUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConcolicTestingWithStub extends TestGeneration {

    private ConcolicTestingWithStub() {
    }

    private static long totalUsedMem = 0;
    private static long tickCount = 0;

    public static TestResult runFullConcolic(String path, String methodName, String className, TestGeneration.Coverage coverage) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, InterruptedException {
        setup(path, className, methodName);
        setupCfgTree(coverage);
        setupParameters(methodName);
        TestGeneration.isSetup = true;

        Timer T = new Timer(true);

        TimerTask memoryTask = new TimerTask() {
            @Override
            public void run() {
                totalUsedMem += (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                tickCount += 1;
            }
        };

        T.scheduleAtFixedRate(memoryTask, 0, 1); //0 delay and 5 ms tick

        long startRunTestTime = System.nanoTime();

        TestResult result = startGenerating(coverage);

        long endRunTestTime = System.nanoTime();

        double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;
        float usedMem = ((float) totalUsedMem) / tickCount / 1024 / 1024;

        result.setTestingTime(runTestDuration);
        result.setUsedMemory(usedMem);

        TestGeneration.isSetup = false;

        return result;
    }

    private static TestResult startGenerating(TestGeneration.Coverage coverage) throws IOException, InterruptedException {
        TestResult testResult = new TestResult();
        int testCaseID = 1;
        Object[] evaluatedValues = SymbolicExecution.createRandomTestData(TestGeneration.parameterClasses);

        TestGeneration.writeDataToFile("", FilePath.concreteExecuteResultPath, false);

        String testDriver = TestDriverGenerator.generateTestDriver((MethodDeclaration) TestGeneration.testFunc, evaluatedValues, TestGeneration.getCoverageType(coverage));
        List<MarkedStatement> markedStatements = TestDriverRunner.runTestDriver(testDriver);

        MarkedPath.markPathToCFGV2(TestGeneration.cfgBeginNode, markedStatements);

        List<CoveredStatement> coveredStatements = CoveredStatement.switchToCoveredStatementList(markedStatements);

        testResult.addToFullTestData(new TestData(TestGeneration.parameterNames, TestGeneration.parameterClasses, evaluatedValues, coveredStatements,
                TestDriverRunner.getOutput(), TestDriverRunner.getRuntime(), calculateRequiredCoverage(coverage), calculateFunctionCoverage(), calculateSourceCodeCoverage(), testCaseID++));

        boolean isTestedSuccessfully = true;

        for (CfgNode uncoveredNode = TestGeneration.findUncoverNode(TestGeneration.cfgBeginNode, coverage); uncoveredNode != null; uncoveredNode = TestGeneration.findUncoverNode(TestGeneration.cfgBeginNode, coverage)) {
            System.out.println("Uncovered Node: " + uncoveredNode);

            Path newPath = (new FindPath(TestGeneration.cfgBeginNode, uncoveredNode, TestGeneration.cfgEndNode)).getPath();

            SymbolicExecution solution = new SymbolicExecution(newPath, TestGeneration.parameters);

            try {
                solution.execute();
            } catch (RuntimeException e) {
//                e.printStackTrace();
                uncoveredNode.setFakeMarked(true); // for STATEMENT coverage
                if (coverage == Coverage.BRANCH) {
                    CfgNode parent = uncoveredNode.getParent();
                    if (parent instanceof CfgBoolExprNode) {
                        CfgBoolExprNode cfgBoolExprNode = (CfgBoolExprNode) parent;
                        if (cfgBoolExprNode.getTrueNode() == uncoveredNode) {
                            cfgBoolExprNode.setFakeTrueMarked(true);
                        } else if (cfgBoolExprNode.getFalseNode() == uncoveredNode) {
                            cfgBoolExprNode.setFakeFalseMarked(true);
                        }
                    }
                }
                continue;
            }

            // update parameter name with stub variable
            parameterNames = TestDriverUtils.getParameterNames(parameters);
            parameterClasses = TestDriverUtils.getParameterClasses(parameters);

            evaluatedValues = solution.getEvaluatedTestData(TestGeneration.parameterClasses);

            TestGeneration.writeDataToFile("", FilePath.concreteExecuteResultPath, false);

            testDriver = TestDriverGenerator.generateTestDriver((MethodDeclaration) TestGeneration.testFunc, evaluatedValues, TestGeneration.getCoverageType(coverage));
            markedStatements = TestDriverRunner.runTestDriver(testDriver);

            MarkedPath.markPathToCFGV2(TestGeneration.cfgBeginNode, markedStatements);
            coveredStatements = CoveredStatement.switchToCoveredStatementList(markedStatements);

            testResult.addToFullTestData(new TestData(parameterNames, parameterClasses, evaluatedValues, coveredStatements, TestDriverRunner.getOutput(), TestDriverRunner.getRuntime(), calculateRequiredCoverage(coverage), calculateFunctionCoverage(), calculateSourceCodeCoverage(), testCaseID++));

        }

        if (isTestedSuccessfully) System.out.println("Tested successfully with 100% coverage");
        else System.out.println("Test fail due to UNSATISFIABLE constraint");

        testResult.setFullCoverage(calculateFullTestSuiteCoverage(coverage));

        return testResult;
    }

    private static void setup(String path, String className, String methodName) throws IOException {
        TestGeneration.funcAstNodeList = ProjectParser.parseFile(path);
        TestGeneration.compilationUnit = ProjectParser.parseFileToCompilationUnit(path);
        classKey = (TestGeneration.compilationUnit.getPackage() != null ? TestGeneration.compilationUnit.getPackage().getName().toString() : "") + className.replace(".java", "") + "totalStatement";
        setUpTestFunc(methodName);
        MarkedPath.resetFullTestSuiteCoveredStatements();

        MethodInvocationNode.resetNumberOfFunctionsCall();
    }

    private static void setUpTestFunc(String methodName) {
        for (ASTNode func : TestGeneration.funcAstNodeList) {
            if (((MethodDeclaration) func).getName().getIdentifier().equals(methodName)) {
                TestGeneration.testFunc = func;
            }
        }
        originalParameters = new ArrayList<>(((MethodDeclaration) testFunc).parameters());
    }

    private static void setupCfgTree(TestGeneration.Coverage coverage) {
        Block functionBlock = Utils.getFunctionBlock(TestGeneration.testFunc);

        TestGeneration.cfgBeginNode = new CfgNode();
        TestGeneration.cfgBeginNode.setIsBeginCfgNode(true);

        TestGeneration.cfgEndNode = new CfgEndBlockNode();
        TestGeneration.cfgEndNode.setIsEndCfgNode(true);

        CfgNode block = new CfgBlockNode();
        block.setAst(functionBlock);

        int firstLine = TestGeneration.compilationUnit.getLineNumber(functionBlock.getStartPosition());
        block.setLineNumber(1);

        block.setBeforeStatementNode(TestGeneration.cfgBeginNode);
        block.setAfterStatementNode(TestGeneration.cfgEndNode);

        ASTHelper.generateCFG(block, TestGeneration.compilationUnit, firstLine, TestGeneration.getCoverageType(coverage));

        if (!ChooseToolController.allowHandleStubForLib) {
            CfgUtils.modifyCfgWithStubVars(cfgBeginNode);
        }
    }

    private static void setupParameters(String methodName) {
        TestGeneration.parameters = ((MethodDeclaration) TestGeneration.testFunc).parameters();
        TestGeneration.parameterClasses = TestDriverUtils.getParameterClasses(TestGeneration.parameters);
        TestGeneration.parameterNames = TestDriverUtils.getParameterNames(TestGeneration.parameters);
    }

    private static double calculateFullTestSuiteCoverage(Coverage coverage) {
        String key = getTotalFunctionCoverageVariableName((MethodDeclaration) testFunc, coverage);
        if (coverage == Coverage.STATEMENT) {
            int totalFunctionStatement = ConcolicUploadUtil.totalStatementsInUnits.get(key);
            int totalCovered = MarkedPath.getFullTestSuiteTotalCoveredStatements();
            return (totalCovered * 100.0) / totalFunctionStatement;
        } else { // branch
            int totalFunctionBranch = ConcolicUploadUtil.totalBranchesInUnits.get(key);
            int totalCovered = MarkedPath.getFullTestSuiteTotalCoveredBranch();
            return (totalCovered * 100.0) / totalFunctionBranch;
        }
    }

    private static double calculateRequiredCoverage(Coverage coverage) {
        String key = getTotalFunctionCoverageVariableName((MethodDeclaration) testFunc, coverage);
        int totalFunctionCoverage = 1;
        int totalCovered = 0;
        if (coverage == TestGeneration.Coverage.STATEMENT) {
            totalCovered = MarkedPath.getTotalCoveredStatement();
            totalFunctionCoverage = ConcolicUploadUtil.totalStatementsInUnits.get(key);
        } else if (coverage == TestGeneration.Coverage.BRANCH) {
            totalCovered = MarkedPath.getTotalCoveredBranch();
            totalFunctionCoverage = ConcolicUploadUtil.totalBranchesInUnits.get(key);
        }
        return (totalCovered * 100.0) / totalFunctionCoverage;
    }

    private static double calculateFunctionCoverage() {
        String key = getTotalFunctionCoverageVariableName((MethodDeclaration) testFunc, TestGeneration.Coverage.STATEMENT);
        int totalFunctionStatement = ConcolicUploadUtil.totalStatementsInUnits.get(key);
        int totalCoveredStatement = MarkedPath.getTotalCoveredStatement();
        return (totalCoveredStatement * 100.0) / (totalFunctionStatement * 1.0);
    }

    private static double calculateSourceCodeCoverage() {
        int totalClassStatement = ConcolicUploadUtil.totalStatementsInJavaFile.get(classKey);
        int totalCoveredStatement = MarkedPath.getTotalCoveredStatement();
        return (totalCoveredStatement * 100.0) / (totalClassStatement * 1.0);
    }
}
