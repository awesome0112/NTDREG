package utils.autoUnitTestUtil.ast.Statement;

import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.symbolicExecution.SymbolicExecution;
import utils.autoUnitTestUtil.cfg.utils.CfgUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.WhileStatement;

public class WhileStatementNode extends StatementNode {
    public static void replaceMethodInvocationWithStub(WhileStatement originWhileStatement, MethodInvocation originMethodInvocation, ASTNode replacement) {
        if (originWhileStatement.getExpression() == originMethodInvocation){
            originWhileStatement.setExpression((Expression) replacement);

            CfgNode currentCfgNode = SymbolicExecution.getCurrentCfgNode();
            if (currentCfgNode != null) {
                currentCfgNode.setAst(replacement);
            } else {
                CfgUtils.getCurrentCfgNode().setAst(replacement);
            }
        }
        // body
    }
}
