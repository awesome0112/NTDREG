package utils.autoUnitTestUtil.ast.Expression;

import utils.autoUnitTestUtil.ast.AstNode;
import utils.autoUnitTestUtil.ast.VariableDeclaration.VariableDeclarationFragmentNode;
import utils.autoUnitTestUtil.symbolicExecution.MemoryModel;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

public class VariableDeclarationExpressionNode extends ExpressionNode {
    private List<ASTNode> modifiers = null;
    private Type baseType = null;
    private List<AstNode> variableDeclarationFragments;

    public static void executeVariableDeclarationExpression(VariableDeclarationExpression variableDeclarationExpression,
                                                            MemoryModel memoryModel) {
        List<VariableDeclarationFragment> fragments = variableDeclarationExpression.fragments();
        for(VariableDeclarationFragment fragment : fragments) {
            VariableDeclarationFragmentNode.executeVariableDeclarationFragment(fragment, variableDeclarationExpression.getType(), memoryModel);
        }
    }

    public static void replaceMethodInvocationWithStub(VariableDeclarationExpression originVariableDeclarationExpression, MethodInvocation originMethodInvocation, ASTNode replacement) {
//        List<VariableDeclarationFragment> fragments = originVariableDeclarationExpression.fragments();
//        for(VariableDeclarationFragment fragment : fragments) {
//            VariableDeclarationFragmentNode.replaceMethodInvocationWithStub(fragment, originMethodInvocation, replacement);
//        }
    }
}
