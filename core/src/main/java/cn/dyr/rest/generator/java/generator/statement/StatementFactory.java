package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.statement.method.AssignmentStatement;
import cn.dyr.rest.generator.java.generator.statement.method.ExceptionThrowStatement;
import cn.dyr.rest.generator.java.generator.statement.method.MethodInvocationStatement;
import cn.dyr.rest.generator.java.generator.statement.method.ReturnStatement;
import cn.dyr.rest.generator.java.generator.statement.method.VariableDeclarationStatement;
import cn.dyr.rest.generator.java.generator.statement.method.block.BranchBlock;
import cn.dyr.rest.generator.java.generator.statement.method.block.ForEachLoopBlock;
import cn.dyr.rest.generator.java.generator.statement.method.block.SequenceBlock;
import cn.dyr.rest.generator.java.meta.flow.ChoiceFlow;
import cn.dyr.rest.generator.java.meta.flow.ForEachLoop;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.MultiLineCommentInstruction;
import cn.dyr.rest.generator.java.meta.flow.SequenceFlow;
import cn.dyr.rest.generator.java.meta.flow.SingleLineCommentInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.AssignmentInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.EmptyInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.ExceptionThrowInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.MethodInvocationInstruction;
import cn.dyr.rest.generator.java.meta.flow.instruction.ReturnInstruction;

/**
 * 用于根据不同的条件创建 Statement 的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class StatementFactory {

    /**
     * 根据一条抽象指令生成相应的代码语句
     *
     * @param instruction 抽象指令
     * @return 相应的代码语句
     */
    public static IStatement fromInstruction(IInstruction instruction) {
        if (instruction == null) {
            return new EmptyStatement();
        }

        if (instruction instanceof EmptyInstruction) {
            return new EmptyStatement();
        } else if (instruction instanceof ReturnInstruction) {
            ReturnInstruction returnInstruction = (ReturnInstruction) instruction;
            return new ReturnStatement(returnInstruction);
        } else if (instruction instanceof AssignmentInstruction) {
            AssignmentInstruction assignmentInstruction = (AssignmentInstruction) instruction;
            if (assignmentInstruction.isDeclaration()) {
                return new VariableDeclarationStatement(assignmentInstruction);
            } else {
                return new AssignmentStatement(assignmentInstruction);
            }
        } else if (instruction instanceof ExceptionThrowInstruction) {
            return new ExceptionThrowStatement((ExceptionThrowInstruction) instruction);
        } else if (instruction instanceof SequenceFlow) {
            return new SequenceBlock((SequenceFlow) instruction);
        } else if (instruction instanceof ChoiceFlow) {
            return new BranchBlock((ChoiceFlow) instruction);
        } else if (instruction instanceof ForEachLoop) {
            return new ForEachLoopBlock((ForEachLoop) instruction);
        } else if (instruction instanceof SingleLineCommentInstruction) {
            return new SingleLineCommentStatement((SingleLineCommentInstruction) instruction);
        } else if (instruction instanceof MultiLineCommentInstruction) {
            return new MultiLineCommentStatement((MultiLineCommentInstruction) instruction);
        } else if (instruction instanceof MethodInvocationInstruction) {
            return new MethodInvocationStatement((MethodInvocationInstruction) instruction);
        }

        throw new IllegalArgumentException("unsupported instruction: " + instruction.getClass());
    }

}
