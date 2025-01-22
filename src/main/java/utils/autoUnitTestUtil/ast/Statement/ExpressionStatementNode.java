package utils.autoUnitTestUtil.ast.Statement;

import utils.autoUnitTestUtil.ast.AstNode;
import utils.autoUnitTestUtil.ast.Expression.ExpressionNode;
import utils.autoUnitTestUtil.symbolicExecution.MemoryModel;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class ExpressionStatementNode extends StatementNode {

    private ExpressionNode expression = null;

    public static AstNode executeExpressionStatement(ExpressionStatement expressionStatement, MemoryModel memoryModel) {
        ExpressionStatementNode expressionStatementNode = new ExpressionStatementNode();
        expressionStatementNode.expression = (ExpressionNode) ExpressionNode.executeExpression(expressionStatement.getExpression(), memoryModel);
        return expressionStatementNode;
    }

    public static void replaceMethodInvocationWithStub(ExpressionStatement originExpressionStatement, MethodInvocation originMethodInvocation, ASTNode replacement) {
        if (originExpressionStatement.getExpression() == originMethodInvocation){
            originExpressionStatement.setExpression((Expression) replacement);
        }
    }
}
