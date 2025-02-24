package utils.autoUnitTestUtil.ast.Expression.OperationExpression;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import utils.autoUnitTestUtil.Z3Vars.Z3VariableWrapper;
import utils.autoUnitTestUtil.ast.AstNode;
import utils.autoUnitTestUtil.ast.Expression.ExpressionNode;
import utils.autoUnitTestUtil.ast.Expression.Literal.BooleanLiteralNode;
import utils.autoUnitTestUtil.ast.Expression.Literal.CharacterLiteralNode;
import utils.autoUnitTestUtil.ast.Expression.Literal.LiteralNode;
import utils.autoUnitTestUtil.ast.Expression.Literal.NumberLiteral.DoubleLiteralNode;
import utils.autoUnitTestUtil.ast.Expression.Literal.NumberLiteral.IntegerLiteralNode;
import utils.autoUnitTestUtil.ast.Expression.Literal.NumberLiteral.NumberLiteralNode;
import utils.autoUnitTestUtil.ast.Expression.Name.NameNode;
import utils.autoUnitTestUtil.symbolicExecution.MemoryModel;
import utils.autoUnitTestUtil.variable.Variable;
import org.eclipse.jdt.core.dom.*;

import java.util.List;

public abstract class OperationExpressionNode extends ExpressionNode {

//    public static Expr createZ3Expression(OperationExpressionNode operationExpressionNode, Context ctx, List<Expr> vars, MemoryModel memoryModel) {
//        if (operationExpressionNode instanceof InfixExpressionNode) {
//            return InfixExpressionNode.createZ3Expression((InfixExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else if (operationExpressionNode instanceof PrefixExpressionNode) {
//            return PrefixExpressionNode.createZ3Expression((PrefixExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else if (operationExpressionNode instanceof PostfixExpressionNode) {
//            return PostfixExpressionNode.createZ3Expression((PostfixExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else if (operationExpressionNode instanceof ParenthesizedExpressionNode) {
//            return ParenthesizedExpressionNode.createZ3Expression((ParenthesizedExpressionNode) operationExpressionNode, ctx, vars, memoryModel);
//        } else {
//            throw new RuntimeException(operationExpressionNode.getClass() + " is not an OperationExpression!!!");
//        }
//    }

    public static Expr createZ3Expression(ExpressionNode operand, Context ctx, List<Z3VariableWrapper> vars, MemoryModel memoryModel) {
        if (operand instanceof InfixExpressionNode) {
            return InfixExpressionNode.createZ3Expression((InfixExpressionNode) operand, ctx, vars, memoryModel);
        } else if (operand instanceof PostfixExpressionNode) {
            return PostfixExpressionNode.createZ3Expression((PostfixExpressionNode) operand, ctx, vars, memoryModel);
        } else if (operand instanceof PrefixExpressionNode) {
            return PrefixExpressionNode.createZ3Expression((PrefixExpressionNode) operand, ctx, vars, memoryModel);
        } else if (operand instanceof ParenthesizedExpressionNode) {
            return ParenthesizedExpressionNode.createZ3Expression((ParenthesizedExpressionNode) operand, ctx, vars, memoryModel);
        } else if (operand instanceof NameNode) {
            return createZ3Variable((NameNode) operand, ctx, vars, memoryModel);
        } else if (operand instanceof LiteralNode) {
            if (operand instanceof NumberLiteralNode) {
                if (operand instanceof IntegerLiteralNode) {
                    return ctx.mkInt(((IntegerLiteralNode) operand).getIntegerValue());
                } else { // operand instanceof DoubleLiteralNode
                    return ctx.mkReal(((DoubleLiteralNode) operand).getTokenValue());
                }
            } else if (operand instanceof BooleanLiteralNode) {
                return ctx.mkBool(((BooleanLiteralNode) operand).getValue());
            } else if (operand instanceof CharacterLiteralNode) {
                return ctx.mkInt(((CharacterLiteralNode) operand).getCharacterValue());
            } else {
                throw new RuntimeException("Invalid Literal");
            }
        } else {
            throw new RuntimeException(operand.getClass() + " is not an Expression");
        }
    }

    private static Expr createZ3Variable(NameNode variableName, Context ctx, List<Z3VariableWrapper> vars, MemoryModel memoryModel) {
        String stringName = NameNode.getStringNameNode(variableName);
        Expr variable = Variable.createZ3Variable(memoryModel.getVariable(stringName), ctx);
        Z3VariableWrapper z3VariableWrapper = new Z3VariableWrapper(variable);
        //Check duplicate and add to vars
        int variableIndex = getDuplicateVariableIndex(z3VariableWrapper, vars);
        if (variableIndex != -1) {
            vars.set(variableIndex, z3VariableWrapper);
        } else {
            vars.add(z3VariableWrapper);
        }

        return variable;
    }

    public static int getDuplicateVariableIndex(Z3VariableWrapper z3Variable, List<Z3VariableWrapper> vars) {
        for (int i = 0; i < vars.size(); i++) {
            if (z3Variable.equals(vars.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static AstNode executeOperationExpression(Expression expression, MemoryModel memoryModel) {
        if (expression instanceof InfixExpression) {
            return InfixExpressionNode.executeInfixExpression((InfixExpression) expression, memoryModel);
        } else if (expression instanceof PrefixExpression) {
            return PrefixExpressionNode.executePrefixExpression((PrefixExpression) expression, memoryModel);
        } else if (expression instanceof PostfixExpression) {
            return PostfixExpressionNode.executePostfixExpression((PostfixExpression) expression, memoryModel);
        } else if (expression instanceof ParenthesizedExpression) {
            return ParenthesizedExpressionNode.executeParenthesizedExpression((ParenthesizedExpression) expression, memoryModel);
        } else {
            throw new RuntimeException(expression.getClass() + " is not an OperationExpression!!!");
        }
    }

    public static ExpressionNode executeOperandNode(ExpressionNode operand, MemoryModel memoryModel) {
        if (operand instanceof InfixExpressionNode) {
            return InfixExpressionNode.executeInfixExpressionNode((InfixExpressionNode) operand, memoryModel);
        } else if (operand instanceof PrefixExpressionNode) {
            return PrefixExpressionNode.executePrefixExpressionNode((PrefixExpressionNode) operand, memoryModel);
        } else if (operand instanceof PostfixExpressionNode) {
            return PostfixExpressionNode.executePostfixExpressionNode((PostfixExpressionNode) operand, memoryModel);
        } else if (operand instanceof ParenthesizedExpressionNode) {
            return ParenthesizedExpressionNode.executeParenthesizedExpressionNode((ParenthesizedExpressionNode) operand, memoryModel);
        } else if (operand instanceof NameNode) {
            return NameNode.executeNameNode((NameNode) operand, memoryModel);
        } else {
            throw new RuntimeException(operand.getClass() + " is Invalid expressionNode");
        }
    }

    public static void replaceMethodInvocationWithStub(Expression originExpression, MethodInvocation originMethodInvocation, ASTNode replacement) {
        if (originExpression instanceof InfixExpression) {
            InfixExpressionNode.replaceMethodInvocationWithStub((InfixExpression) originExpression, originMethodInvocation, replacement);
        } else if (originExpression instanceof PrefixExpression) {
            PrefixExpressionNode.replaceMethodInvocationWithStub((PrefixExpression) originExpression, originMethodInvocation, replacement);
        } else if (originExpression instanceof PostfixExpression) {
            PostfixExpressionNode.replaceMethodInvocationWithStub((PostfixExpression) originExpression, originMethodInvocation, replacement);
        } else if (originExpression instanceof ParenthesizedExpression) {
            ParenthesizedExpressionNode.replaceMethodInvocationWithStub((ParenthesizedExpression) originExpression, originMethodInvocation, replacement);
        } else {
            throw new RuntimeException(originExpression.getClass() + " is not an OperationExpression!!!");
        }
    }
}
