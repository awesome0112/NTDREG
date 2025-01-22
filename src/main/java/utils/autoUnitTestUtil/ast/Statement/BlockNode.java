package utils.autoUnitTestUtil.ast.Statement;

import utils.autoUnitTestUtil.cfg.CfgNode;
import utils.autoUnitTestUtil.symbolicExecution.SymbolicExecution;
import utils.autoUnitTestUtil.cfg.utils.CfgUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.List;

public class BlockNode extends StatementNode {
    public static void replaceMethodInvocationWithStub(Block originBlock, MethodInvocation originMethodInvocation, ASTNode replacement) {
        List<ASTNode> statements = originBlock.statements();
        for (int i = 0; i < statements.size(); i++) {
            if (statements.get(i) == originMethodInvocation) {
                statements.set(i, replacement);

                CfgNode currentCfgNode = SymbolicExecution.getCurrentCfgNode();
                if (currentCfgNode != null) {
                    currentCfgNode.setAst(replacement);
                } else {
                    CfgUtils.getCurrentCfgNode().setAst(replacement);
                }
                return;
            }
        }
    }
}
