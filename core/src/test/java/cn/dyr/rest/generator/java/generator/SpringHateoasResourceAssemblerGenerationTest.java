package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterValueFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;
import org.junit.Test;

/**
 * 用于创建一个用于 spring hateoas 的资源装配器代码的测试用例
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringHateoasResourceAssemblerGenerationTest {

    @Test
    public void testGenerateBookResourceAssembler() {
        TypeInfo bookTypeInfo = TypeInfoFactory.fromClass("cn.dyr.spring.boot.data.po.Book");
        TypeInfo resourceTypeInfo = TypeInfoFactory.fromClass("cn.dyr.spring.boot.data.hateoas.BookResource");
        TypeInfo controllerTypeInfo = TypeInfoFactory.fromClass("cn.dyr.spring.boot.data.controller.BookController");
        TypeInfo componentTypeInfo = TypeInfoFactory.fromClass("org.springframework.stereotype.Component");

        TypeInfo baseClassTypeInfo = TypeInfoFactory.fromClass("org.springframework.hateoas.mvc.ResourceAssemblerSupport");
        baseClassTypeInfo = TypeInfoFactory.wrapGenerics(baseClassTypeInfo, new TypeInfo[]{
                bookTypeInfo, resourceTypeInfo
        });

        TypeInfo systemTypeInfo = TypeInfoFactory.fromClass("java.lang.System");
        TypeInfo mathTypeInfo = TypeInfoFactory.fromClass("java.lang.Math");

        TypeInfo controllerLinkBuilderType = TypeInfoFactory.fromClass("org.springframework.hateoas.mvc.ControllerLinkBuilder");

        ClassInfo classInfo = new ClassInfo()
                .setPackageName("cn.dyr.spring.boot.data.hateoas.assembler")
                .setClassName("BookResourceAssembler")
                .setPublic()
                .extendClass(baseClassTypeInfo)
                .addAnnotation(new AnnotationInfo()
                        .setType(componentTypeInfo));

        MethodInfo constructorMethod = classInfo.createConstructorMethod();
        IInstruction constructorMethodInstruction = InstructionFactory.sequence(
                InstructionFactory.superConstructor(new ParameterValue[]{
                        ParameterValueFactory.fromObject(controllerTypeInfo),
                        ParameterValueFactory.fromObject(resourceTypeInfo)
                })
        );
        constructorMethod.setRootInstruction(constructorMethodInstruction);

        MethodInfo toResourceMethod = new MethodInfo()
                .setReturnValueType(resourceTypeInfo)
                .setName("toResource")
                .addParameter(new Parameter()
                        .setName("entity")
                        .setTypeInfo(bookTypeInfo));
        classInfo.addMethod(toResourceMethod);

        IValueExpression resourceVariable = ValueExpressionFactory.variable("resource");
        IValueExpression entityVariable = ValueExpressionFactory.variable("entity");

        IInstruction methodOnInstruction = InstructionFactory.invokeStaticMethod(controllerLinkBuilderType, true, "methodOn", new Object[]{controllerTypeInfo});
        IInstruction getInstruction = methodOnInstruction.invoke("get", new Object[]{entityVariable.invokeMethod("getById", new Object[]{})});
        IInstruction linkToInstruction = InstructionFactory.invokeStaticMethod(controllerLinkBuilderType, true, "linkTo", new Object[]{getInstruction.toValueExpression()});
        IInstruction withSelfRelInstruction = linkToInstruction.invoke("withSelfRel", new Object[]{});

        IInstruction toResourceMethodInstruction = InstructionFactory.sequence(
                InstructionFactory.variableDeclaration(
                        resourceTypeInfo, "resource",
                        ValueExpressionFactory.invokeConstructor(resourceTypeInfo, null)),
                InstructionFactory.invoke(resourceVariable, "setName",
                        new Object[]{entityVariable.invokeMethod("getName", new Object[]{})})
                        .invoke("setAuthors", new Object[]{entityVariable.invokeMethod("getAuthors", new Object[]{})}),
                InstructionFactory.invoke(resourceVariable, "add", new Object[]{withSelfRelInstruction.toValueExpression()})
        );

        toResourceMethod.setRootInstruction(toResourceMethodInstruction);

        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        generator.getConfig().setStaticImportSeparation(true);

        String code = generator.generateSingleClass(classInfo);

        System.out.println(code);
    }

}
