package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.factory.ChoiceFlowBuilder;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import org.junit.Test;

/**
 * 这个类中储存了用于测试 if 生成的测试用例
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class IfGenerationTest {

    private String generateCode(IInstruction instruction) {
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator();

        MethodInfo methodInfo = new MethodInfo()
                .setName("testMethod")
                .setPublic()
                .setRootInstruction(instruction);
        ClassInfo mainClass = new ClassInfo()
                .setClassName("MainClass")
                .setPackageName("cn.dyr.test")
                .addMethod(methodInfo);

        return codeGenerator.generateSingleClass(mainClass);
    }

    @Test
    public void testIfOnly() {
        IValueExpression leftExpression = ValueExpressionFactory.intExpression(1);
        IValueExpression rightExpression = ValueExpressionFactory.intExpression(2);

        IValueExpression ifValueExpression = ValueExpressionFactory.logicalEqual(leftExpression, rightExpression);
        IInstruction ifBlock = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(leftExpression, rightExpression));

        ChoiceFlowBuilder choiceFlowBuilder = InstructionFactory.choiceBuilder(ifValueExpression, ifBlock);
        IInstruction ifFlow = choiceFlowBuilder.build();

        String code = generateCode(ifFlow);
        System.out.println(code);
    }

    @Test
    public void testIfElse() {
        IValueExpression oneExpression = ValueExpressionFactory.intExpression(1);
        IValueExpression twoExpression = ValueExpressionFactory.intExpression(2);
        IValueExpression threeExpression = ValueExpressionFactory.intExpression(3);
        IValueExpression ifValueExpression = ValueExpressionFactory.logicalEqual(oneExpression, twoExpression);
        IInstruction ifBlock = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(oneExpression, twoExpression));
        IInstruction elseBlock = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(twoExpression, threeExpression)
        );

        ChoiceFlowBuilder builder = InstructionFactory.choiceBuilder(ifValueExpression, ifBlock);
        builder.setElse(elseBlock);

        IInstruction instruction = builder.build();
        String code = generateCode(instruction);
        System.out.println(code);
    }

    @Test
    public void testIfElseIfElse() {
        IValueExpression oneExpression = ValueExpressionFactory.intExpression(1);
        IValueExpression twoExpression = ValueExpressionFactory.intExpression(2);
        IValueExpression threeExpression = ValueExpressionFactory.intExpression(3);
        IValueExpression fourExpression = ValueExpressionFactory.intExpression(4);
        IValueExpression fiveExpression = ValueExpressionFactory.intExpression(5);

        IValueExpression ifValueExpression = ValueExpressionFactory.logicalEqual(oneExpression, twoExpression);
        IValueExpression elseIfValueExpression1 = ValueExpressionFactory.logicalEqual(twoExpression, threeExpression);
        IValueExpression elseIfValueExpression2 = ValueExpressionFactory.logicalEqual(threeExpression, fourExpression);

        IInstruction ifBlock = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(oneExpression, twoExpression));
        IInstruction elseIfBlock1 = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(threeExpression, fourExpression)
        );
        IInstruction elseIfBlock2 = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(fourExpression, fiveExpression)
        );
        IInstruction elseBlock = InstructionFactory.variableDeclaration(
                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                "result",
                ValueExpressionFactory.plus(twoExpression, threeExpression)
        );

        ChoiceFlowBuilder builder = InstructionFactory.choiceBuilder(ifValueExpression, ifBlock);
        builder.addElseIf(elseIfValueExpression1, elseIfBlock1);
        builder.addElseIf(elseIfValueExpression2, elseIfBlock2);
        builder.setElse(elseBlock);

        IInstruction instruction = builder.build();
        String code = generateCode(instruction);
        System.out.println(code);
    }

}
