package utils.autoUnitTestUtil.ast.Statement;

import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.symbolicExecution.SymbolicExecution;
import utils.autoUnitTestUtil.cfg.utils.CfgUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.List;

public class ForStatementNode extends StatementNode {

    public static void replaceMethodInvocationWithStub(ForStatement originForStatement, MethodInvocation originMethodInvocation, ASTNode replacement) {
        if (originForStatement.getExpression() == originMethodInvocation) {
            originForStatement.setExpression((Expression) replacement);

            CfgNode currentCfgNode = SymbolicExecution.getCurrentCfgNode();
            if (currentCfgNode != null) {
                currentCfgNode.setAst(replacement);
            } else {
                CfgUtils.getCurrentCfgNode().setAst(replacement);
            }
        } else {
            List<ASTNode> updaters = originForStatement.updaters();
            for (int i = 0; i < updaters.size(); i++) {
                if (updaters.get(i) == originMethodInvocation) {
                    updaters.set(i, replacement);
                    return;
                }
            }

            List<ASTNode> initializers = originForStatement.initializers();
            for (int i = 0; i < initializers.size(); i++) {
                if (initializers.get(i) == originMethodInvocation) {
                    initializers.set(i, replacement);
                    return;
                }
            }
        }
    }
}
