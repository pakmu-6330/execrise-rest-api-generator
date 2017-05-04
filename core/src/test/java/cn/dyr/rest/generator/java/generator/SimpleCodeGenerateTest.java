package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.exception.DuplicatedNameException;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.MethodInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.instruction.AssignmentInstruction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * 简单类生成的测试
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SimpleCodeGenerateTest {

    /**
     * 测试用例一：
     * <p>
     * 生成最基本类的框架
     * </p>
     */
    @Test
    public void testUnit1() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("BookDAO");
        classInfo.setPackageName("cn.dyr.java.generator");

        TypeInfo connectionTypeInfo = TypeInfoFactory.fromClass("java.sql.Connection");
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setPrivate().setType(connectionTypeInfo).setName("connection");

        classInfo.addField(fieldInfo);

        String code = generator.generateSingleClass(classInfo);
        System.out.println(code);
    }

    /**
     * 测试用例二：生成一个含有两个相同类型的字段
     * <p>
     * 测试判断是否会出现重复的 import 语句
     * </p>
     */
    @Test
    public void testUnit2() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("BookDAO");
        classInfo.setPackageName("cn.dyr.java.generator");

        TypeInfo connectionTypeInfo = TypeInfoFactory.fromClass("java.sql.Connection");
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setPrivate().setType(connectionTypeInfo).setName("connection");
        classInfo.addField(fieldInfo);

        FieldInfo anotherFieldInfo = new FieldInfo();
        anotherFieldInfo.setPrivate().setType(connectionTypeInfo).setName("anotherConnection");
        classInfo.addField(anotherFieldInfo);

        String code = generator.generateSingleClass(classInfo);
        System.out.println(code);
    }

    /**
     * 测试用例三：尝试生成一个含有同名字段的类，在生成类信息的过程中会抛出异常
     */
    @Test
    public void testUnit3() {
        try {
            DefaultCodeGenerator generator = new DefaultCodeGenerator();

            ClassInfo classInfo = new ClassInfo();
            classInfo.setClassName("BookDAO");
            classInfo.setPackageName("cn.dyr.java.generator");

            TypeInfo connectionTypeInfo = TypeInfoFactory.fromClass("java.sql.Connection");
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.setPrivate().setType(connectionTypeInfo).setName("connection");
            classInfo.addField(fieldInfo);

            FieldInfo anotherFieldInfo = new FieldInfo();
            anotherFieldInfo.setPrivate().setType(connectionTypeInfo).setName("connection");
            classInfo.addField(anotherFieldInfo);

            String code = generator.generateSingleClass(classInfo);
            System.out.println(code);
            fail("exception should be thrown but not");
        } catch (Exception e) {
            if (!(e instanceof DuplicatedNameException)) {
                fail("exception is not expected!");
            }
        }
    }

    @Test
    public void testImportStatementsSortASC() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        CodeGeneratorConfig config = generator.getConfig();
        config.setSortImportStatements(true);
        config.setInsertBlankLineBetweenDifferentLatterInImportSection(true);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("BookDAO");
        classInfo.setPackageName("cn.dyr.java.generator");

        TypeInfo springMvcRequestMethod =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.RequestMethod");
        TypeInfo noHandlerFound =
                TypeInfoFactory.fromClass("org.springframework.web.servlet.NoHandlerFoundException");
        TypeInfo executeResponse =
                TypeInfoFactory.fromClass("cn.dyr.dns.log.report.common.ExecuteResponse");
        TypeInfo list = TypeInfoFactory.fromClass("java.util.List");

        FieldInfo fieldA = new FieldInfo();
        fieldA.setType(springMvcRequestMethod).setName("requestMethod").setPrivate();

        FieldInfo fieldB = new FieldInfo();
        fieldB.setType(executeResponse).setName("executeResponse").setPrivate();

        FieldInfo fieldC = new FieldInfo();
        fieldC.setType(list).setName("list").setPrivate();

        FieldInfo fieldD = new FieldInfo();
        fieldD.setType(noHandlerFound).setName("noHandlerFound").setPrivate();

        classInfo.addField(fieldA).addField(fieldB).addField(fieldC).addField(fieldD);
        String code = generator.generateSingleClass(classInfo);
        System.out.println(code);
    }

    @Test
    public void testSameClassNameProcess() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        CodeGeneratorConfig config = generator.getConfig();
        config.setSortImportStatements(false);
        config.setInsertBlankLineBetweenDifferentLatterInImportSection(false);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("BookDAO");
        classInfo.setPackageName("cn.dyr.java.generator");

        TypeInfo resourceA = TypeInfoFactory.fromClass("com.xxx.ooo.Resource");
        TypeInfo resourceB = TypeInfoFactory.fromClass("com.xxx.aaaa.Resource");

        FieldInfo fieldInfoA = new FieldInfo().setType(resourceA).setName("resA").setPrivate();
        FieldInfo fieldInfoB = new FieldInfo().setType(resourceB).setName("resB").setPrivate();

        classInfo.addField(fieldInfoA).addField(fieldInfoB);
        String targetCode = generator.generateSingleClass(classInfo);

        System.out.println(targetCode);
    }

    @Test
    public void testClassWithAnnotationWithoutSameName() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        CodeGeneratorConfig config = generator.getConfig();
        config.setSortImportStatements(true);
        config.setInsertBlankLineBetweenDifferentLatterInImportSection(true);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("IBookDAO").setPackageName("cn.dyr.java.generator");

        TypeInfo dataSourceType = TypeInfoFactory.fromClass("javax.sql.DataSource");
        TypeInfo repositoryType = TypeInfoFactory.fromClass("org.springframework.stereotype.Repository");

        classInfo.addField(new FieldInfo().setType(dataSourceType).setName("dataSource").setPrivate());
        classInfo.addAnnotation(new AnnotationInfo().setType(repositoryType));

        String targetCode = generator.generateSingleClass(classInfo);
        System.out.println(targetCode);
    }

    @Test
    public void testClassWithAnnotationWithSameName() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        CodeGeneratorConfig config = generator.getConfig();
        config.setSortImportStatements(true);
        config.setInsertBlankLineBetweenDifferentLatterInImportSection(true);

        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName("IBookDAO").setPackageName("cn.dyr.java.generator");

        TypeInfo dataSourceType = TypeInfoFactory.fromClass("javax.sql.DataSource");
        TypeInfo repositoryType = TypeInfoFactory.fromClass("org.springframework.stereotype.Repository");
        TypeInfo anotherRepositoryType = TypeInfoFactory.fromClass("cn.dyr.test.Repository");

        classInfo.addField(new FieldInfo().setType(dataSourceType).setName("dataSource").setPrivate());
        classInfo.addField(new FieldInfo().setType(anotherRepositoryType).setName("myRepository").setPrivate());
        classInfo.addAnnotation(new AnnotationInfo().setType(repositoryType));

        String targetCode = generator.generateSingleClass(classInfo);
        System.out.println(targetCode);
    }

    @Test
    public void testMethodWithPrimitiveDeclarationStatement() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();

        FieldInfo idField = new FieldInfo()
                .setName("id")
                .setType(TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG))
                .setPrivate();
        MethodInfo idGetterMethod = MethodInfoFactory.getter(idField);
        MethodInfo idSetterMethod = MethodInfoFactory.setter(idField);

        MethodInfo longDeclarationMethod = new MethodInfo()
                .setPublic()
                .setName("longDeclaration")
                .setReturnValueType(TypeInfoFactory.voidType())
                .setRootInstruction(new AssignmentInstruction()
                        .setDeclaration(true)
                        .setTargetValueType(
                                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG))
                        .setTargetVariable(ValueExpressionFactory.variable("value"))
                        .setValue(ValueExpressionFactory.longExpression(1)));

        MethodInfo constructorMethod = new MethodInfo()
                .setPublic()
                .setName("constructorMethod")
                .setReturnValueType(TypeInfoFactory.voidType())
                .setRootInstruction(
                        InstructionFactory.sequence(
                                InstructionFactory.variableDeclaration(
                                        TypeInfoFactory.stringType(), "str",
                                        ValueExpressionFactory.invokeConstructor(
                                                TypeInfoFactory.stringType(), new Object[]{"a"})),
                                InstructionFactory.assignment(
                                        "str", ValueExpressionFactory.invokeConstructor(
                                                TypeInfoFactory.stringType(), new Object[]{"b"})),
                                InstructionFactory.assignment(
                                        "str",
                                        ValueExpressionFactory.variable("str")
                                                .invokeMethod("trim", null)),
                                InstructionFactory.variableDeclaration(
                                        TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_CHAR),
                                        "c",
                                        ValueExpressionFactory.variable("str")
                                                .invokeMethod("charAt", new Object[]{1})),
                                InstructionFactory.variableDeclaration(
                                        TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT),
                                        "result",
                                        ValueExpressionFactory.plus(
                                                ValueExpressionFactory.intExpression(1),
                                                ValueExpressionFactory.intExpression(2)
                                        )
                                ),
                                InstructionFactory.invoke(
                                        ValueExpressionFactory.thisReference(),
                                        "setId", new Object[]{2L}))
                );

        MethodInfo doubleVariableDeclarationMethod = new MethodInfo()
                .setPublic()
                .setName("doubleDeclaration")
                .setReturnValueType(TypeInfoFactory.voidType())
                .setRootInstruction(InstructionFactory.sequence(
                        InstructionFactory.variableDeclaration(
                                TypeInfoFactory.stringType(), "str", ValueExpressionFactory.stringExpression("a")),
                        InstructionFactory.variableDeclaration(
                                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG), "l", ValueExpressionFactory.longDefault())
                ));

        ClassInfo classInfo = new ClassInfo()
                .setPackageName("cn.dyr.test")
                .setClassName("TestClass")
                .setPublic()
                .addField(idField)
                .addMethod(idGetterMethod)
                .addMethod(idSetterMethod)
                .addMethod(longDeclarationMethod)
                .addMethod(doubleVariableDeclarationMethod)
                .addMethod(constructorMethod);

        String s = generator.generateSingleClass(classInfo);
        System.out.println(s);
    }

    @Test
    public void testClassProperty() {
        List<Long> longList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        System.out.println(longList.getClass() == intList.getClass());
    }
}
