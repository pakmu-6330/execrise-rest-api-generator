package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.MethodInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;
import org.junit.Test;

/**
 * 用于生成 Spring Boot 启动类代码的测试
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringBootApplicationGenerationTest {

    @Test
    public void test() {
        DefaultCodeGenerator generator = new DefaultCodeGenerator();

        TypeInfo servletInitializerClassInfo =
                TypeInfoFactory.fromClass("org.springframework.boot.web.support.SpringBootServletInitializer");
        TypeInfo springBootApplicationAnnotation =
                TypeInfoFactory.fromClass("org.springframework.boot.autoconfigure.SpringBootApplication");
        TypeInfo enableConfigurationPropertiesAnnotation =
                TypeInfoFactory.fromClass("org.springframework.boot.context.properties.EnableConfigurationProperties");
        TypeInfo springApplicationBuilderTypeInfo =
                TypeInfoFactory.fromClass("org.springframework.boot.builder.SpringApplicationBuilder");
        TypeInfo beanTypeInfo =
                TypeInfoFactory.fromClass("org.springframework.context.annotation.Bean");
        TypeInfo configurationPropertiesAnnotation =
                TypeInfoFactory.fromClass("org.springframework.boot.context.properties.ConfigurationProperties");
        TypeInfo overrideAnnotation = TypeInfoFactory.fromClass("java.lang.Override");
        TypeInfo dataSourceType = TypeInfoFactory.fromClass("javax.sql.DataSource");

        MethodInfo configureMethod = new MethodInfo()
                .setProtected()
                .setReturnValueType(springApplicationBuilderTypeInfo)
                .setName("configure")
                .addParameter(new Parameter()
                        .setName("builder")
                        .setTypeInfo(springApplicationBuilderTypeInfo))
                .addAnnotationInfo(new AnnotationInfo().setType(overrideAnnotation));
        MethodInfo dataSourceMethod = new MethodInfo()
                .setName("dataSource")
                .setPublic()
                .setReturnValueType(dataSourceType)
                .addAnnotationInfo(new AnnotationInfo().setType(beanTypeInfo))
                .addAnnotationInfo(new AnnotationInfo()
                        .setType(configurationPropertiesAnnotation)
                        .addParameter("prefix",
                                AnnotationParameterFactory.stringParameter("spring.datasource")))
                .setRootInstruction(
                        InstructionFactory.returnInstruction(
                                ValueExpressionFactory.nullExpression()));
        MethodInfo returnOneMethod = new MethodInfo()
                .setName("returnOne")
                .setPublic()
                .setReturnValueType(TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG))
                .setRootInstruction(
                        InstructionFactory.returnInstruction(
                                ValueExpressionFactory.longExpression(1)));

        ClassInfo classInfo = new ClassInfo()
                .setClassName("DnsLogReportApplication")
                .setPackageName("cn.dyr.dns.log.report")
                .setPublic()
                .extendClass(servletInitializerClassInfo)
                .addAnnotation(new AnnotationInfo().setType(springBootApplicationAnnotation))
                .addAnnotation(new AnnotationInfo().setType(enableConfigurationPropertiesAnnotation))
                .addMethod(configureMethod)
                .addMethod(dataSourceMethod)
                .addMethod(MethodInfoFactory.createMainMethod())
                .addMethod(returnOneMethod);

        String code = generator.generateSingleClass(classInfo);
        System.out.println(code);
    }

}
