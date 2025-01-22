package utils.autoUnitTestUtil.ast.Statement;

import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.symbolicExecution.SymbolicExecution;
import utils.autoUnitTestUtil.cfg.utils.CfgUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class IfStatementNode extends StatementNode {
    public static void replaceMethodInvocationWithStub(IfStatement originIfStatement, MethodInvocation originMethodInvocation, ASTNode replacement) {
        if (originIfStatement.getExpression() == originMethodInvocation){
            originIfStatement.setExpression((Expression) replacement);

            CfgNode currentCfgNode = SymbolicExecution.getCurrentCfgNode();
            if (currentCfgNode != null) {
                currentCfgNode.setAst(replacement);
            } else {
                CfgUtils.getCurrentCfgNode().setAst(replacement);
            }
        }
        // then statement
        // else statement
    }
}
