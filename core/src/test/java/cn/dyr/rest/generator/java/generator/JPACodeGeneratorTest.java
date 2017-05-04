package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;
import org.junit.Test;

/**
 * 生成一个含有 JPA 注解的
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPACodeGeneratorTest {

    /**
     * 生成一个含有 JPA 注解的 PO 类
     */
    @Test
    public void testSimpleJPAEntityCase1() {
        DefaultCodeGenerator defaultCodeGenerator = new DefaultCodeGenerator();
        CodeGeneratorConfig config = defaultCodeGenerator.getConfig();
        config.setSortImportStatements(true);
        config.setInsertBlankLineBetweenDifferentLatterInImportSection(true);

        ClassInfo classInfo = new ClassInfo()
                .setClassName("Book").setPackageName("cn.dyr.library.po");

        // 加载注解的类型
        TypeInfo entityAnnotation =
                TypeInfoFactory.fromClass("javax.persistence.Entity");
        TypeInfo tableAnnotation =
                TypeInfoFactory.fromClass("javax.persistence.Table");
        TypeInfo idAnnotationTypeInfo =
                TypeInfoFactory.fromClass("javax.persistence.Id");
        TypeInfo columnTypeInfo =
                TypeInfoFactory.fromClass("javax.persistence.Column");
        TypeInfo generatedValueTypeInfo =
                TypeInfoFactory.fromClass("javax.persistence.GeneratedValue");
        TypeInfo generationTypeEnumInfo =
                TypeInfoFactory.fromClass("javax.persistence.GenerationType");
        TypeInfo listTypeInfo =
                TypeInfoFactory.fromClass("java.util.List");
        TypeInfo authorListType = TypeInfoFactory.wrapGenerics(listTypeInfo,
                new TypeInfo[]{TypeInfoFactory.fromClass("cn.dyr.library.po.Author")});
        TypeInfo entityBaseInfo =
                TypeInfoFactory.fromClass("cn.dyr.library.base.EntityBase");
        TypeInfo baseDaoInfo = TypeInfoFactory.fromClass("cn.dyr.library.dao.IBaseDAO");
        TypeInfo overrideTypeInfo = TypeInfoFactory.fromClass("java.lang.Override");
        TypeInfo illegalAccessExceptionType =
                TypeInfoFactory.fromClass("java.lang.IllegalAccessException");
        TypeInfo classCastException = TypeInfoFactory.fromClass("java.lang.ClassCastException");

        TypeInfo stringTypeInfo = TypeInfoFactory.stringType();
        TypeInfo longTypeInfo = TypeInfoFactory.longType();

        AnnotationParameter tableNameParameter =
                AnnotationParameterFactory.stringParameter("t_book");

        classInfo.extendClass(entityBaseInfo);
        classInfo.implementInterface(baseDaoInfo);
        classInfo.addAnnotation(new AnnotationInfo().setType(entityAnnotation));
        classInfo.addAnnotation(new AnnotationInfo().setType(tableAnnotation).addParameter("name", tableNameParameter));

        AnnotationInfo generatedValueAnnotation =
                new AnnotationInfo().setType(generatedValueTypeInfo);
        AnnotationParameter strategyParameter =
                AnnotationParameterFactory.enumerationParameter(
                        generationTypeEnumInfo, "IDENTITY");
        generatedValueAnnotation.addParameter("strategy", strategyParameter);

        FieldInfo idFieldInfo = new FieldInfo().setPrivate().setType(longTypeInfo).setName("id");
        idFieldInfo.addAnnotation(new AnnotationInfo().setType(idAnnotationTypeInfo));
        idFieldInfo.addAnnotation(generatedValueAnnotation);
        classInfo.addField(idFieldInfo);

        FieldInfo nameFieldInfo = new FieldInfo().setPrivate().setType(stringTypeInfo).setName("name");
        nameFieldInfo.addAnnotation(new AnnotationInfo().setType(columnTypeInfo));
        classInfo.addField(nameFieldInfo);

        FieldInfo authorListInfo = new FieldInfo().setPrivate().setType(authorListType).setName("authors");
        classInfo.addField(authorListInfo);

        MethodInfo idGetterMethod = new MethodInfo();
        idGetterMethod.setName("getById")
                .setReturnValueType(
                        TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG))
                .setPublic();
        classInfo.addMethod(idGetterMethod);

        MethodInfo idSetterMethod = new MethodInfo();
        idSetterMethod.setName("setId")
                .setReturnValueType(TypeInfoFactory.voidType())
                .setPublic()
                .addParameter(
                        new Parameter()
                                .setTypeInfo(TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG))
                                .setName("id"))
                .setRootInstruction(
                        InstructionFactory.assignment(
                                ValueExpressionFactory.thisReference().accessField("id"),
                                ValueExpressionFactory.variable("id")
                        )
                )
                .addThrowable(illegalAccessExceptionType);
        classInfo.addMethod(idSetterMethod);

        MethodInfo nameSetterMethod = new MethodInfo();
        nameSetterMethod.setName("setName")
                .setReturnValueType(TypeInfoFactory.voidType())
                .setPublic()
                .addParameter(
                        new Parameter()
                                .setTypeInfo(TypeInfoFactory.stringType())
                                .setName("name")
                )
                .addThrowable(illegalAccessExceptionType)
                .addThrowable(classCastException);
        classInfo.addMethod(nameSetterMethod);

        MethodInfo toStringMethod = new MethodInfo();
        toStringMethod.setName("toString")
                .setReturnValueType(TypeInfoFactory.stringType())
                .setPublic()
                .addAnnotationInfo(new AnnotationInfo().setType(overrideTypeInfo));
        classInfo.addMethod(toStringMethod);

        String code = defaultCodeGenerator.generateSingleClass(classInfo);
        System.out.println(code);
    }

}
