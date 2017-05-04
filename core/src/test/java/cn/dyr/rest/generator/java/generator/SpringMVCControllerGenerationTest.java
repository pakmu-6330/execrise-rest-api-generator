package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameter;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;
import org.junit.Test;

/**
 * 用于测试 SpringMVC 控制器类代码的生成
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringMVCControllerGenerationTest {

    @Test
    public void testGenerationListMethod() {
        // 读入 Spring 相关的注解
        TypeInfo crossOriginAnnotation =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.CrossOrigin");
        TypeInfo restControllerAnnotation =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.RestController");
        TypeInfo requestMappingAnnotation =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.RequestMapping");
        TypeInfo requestMethod =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.RequestMethod");
        TypeInfo pathVariableAnnotation =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.PathVariable");
        TypeInfo requestParamAnnotation =
                TypeInfoFactory.fromClass("org.springframework.web.bind.annotation.RequestParam");

        // javax 的一些注解
        TypeInfo resourceAnnotation = TypeInfoFactory.fromClass("javax.annotation.Resource");

        // 这个应用本身的一些类
        TypeInfo reportServiceInterface =
                TypeInfoFactory.fromClass("cn.dyr.dns.log.report.service.IReportService");
        TypeInfo domainServiceInterface =
                TypeInfoFactory.fromClass("cn.dyr.dns.log.report.service.IDomainService");
        TypeInfo executeResponse =
                TypeInfoFactory.fromClass("cn.dyr.dns.log.report.common.ExecuteResponse");

        // 下面开始拼装类信息
        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        ClassInfo reportControllerClass = new ClassInfo()
                .setPublic()
                .setClassName("ReportController")
                .setPackageName("cn.dyr.dns.log.report.controller")
                .addAnnotation(new AnnotationInfo()
                        .setType(crossOriginAnnotation)
                        .addParameter("origins", AnnotationParameterFactory.stringParameter("*"))
                        .addParameter("maxAge", AnnotationParameterFactory.intParameter(3600)))
                .addAnnotation(new AnnotationInfo()
                        .setType(restControllerAnnotation))
                .addAnnotation(new AnnotationInfo()
                        .setType(requestMappingAnnotation)
                        .setDefaultValue(AnnotationParameterFactory.stringParameter("/reports")));

        // 创建 serviceMap
        TypeInfo map = TypeInfoFactory.fromClass("java.util.Map");
        TypeInfo serviceMapType = TypeInfoFactory.wrapGenerics(map,
                new TypeInfo[]{
                        TypeInfoFactory.primitiveWrapper(TypeInfoFactory.PRIMITIVE_INT),
                        reportServiceInterface
                });
        FieldInfo serviceMap = new FieldInfo()
                .setPrivate()
                .setType(serviceMapType)
                .setName("serviceMap");
        reportControllerClass.addField(serviceMap);

        // 域名报告业务类
        FieldInfo domainService = new FieldInfo()
                .setPrivate()
                .setType(domainServiceInterface)
                .setName("domainService")
                .addAnnotation(new AnnotationInfo().setType(resourceAnnotation));
        reportControllerClass.addField(domainService);

        // count 方法
        MethodInfo countMethod = new MethodInfo()
                .setPublic()
                .setName("count")
                .setReturnValueType(executeResponse)
                .addAnnotationInfo(new AnnotationInfo()
                        .setType(requestMappingAnnotation)
                        .setDefaultValue(
                                AnnotationParameterFactory.stringParameter("/{field}/{period}/count"))
                        .addParameter("method",
                                AnnotationParameterFactory.enumerationParameter(requestMethod, "GET")))
                .addParameter(
                        ParameterFactory.create(TypeInfoFactory.stringType(), "field")
                                .addAnnotationInfo(new AnnotationInfo()
                                        .setType(pathVariableAnnotation)
                                        .setDefaultValue(AnnotationParameterFactory.stringParameter("field"))))
                .addParameter(
                        ParameterFactory.create(TypeInfoFactory.stringType(), "period")
                                .addAnnotationInfo(new AnnotationInfo()
                                        .setType(pathVariableAnnotation)
                                        .setDefaultValue(AnnotationParameterFactory.stringParameter("period")))
                );
        reportControllerClass.addMethod(countMethod);

        // list 方法
        MethodInfo listMethod = new MethodInfo()
                .setPublic()
                .setName("list")
                .setReturnValueType(executeResponse)
                .addAnnotationInfo(new AnnotationInfo()
                        .setType(requestMappingAnnotation)
                        .setDefaultValue(AnnotationParameterFactory.from("/{field}/{period}"))
                        .addParameter("method", AnnotationParameterFactory.enumerationParameter(requestMethod, "GET")))
                .addParameter(
                        ParameterFactory.create(
                                TypeInfoFactory.stringType(), "field")
                                .addAnnotationInfo(new AnnotationInfo()
                                        .setType(pathVariableAnnotation)
                                        .setDefaultValue(AnnotationParameterFactory.from("field"))))
                .addParameter(
                        ParameterFactory.create(
                                TypeInfoFactory.stringType(), "period")
                                .addAnnotationInfo(new AnnotationInfo()
                                        .setType(pathVariableAnnotation)
                                        .setDefaultValue(AnnotationParameterFactory.from("period"))))
                .addParameter(
                        ParameterFactory.create(
                                TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT), "offset")
                                .addAnnotationInfo(new AnnotationInfo()
                                        .setType(requestMappingAnnotation)
                                        .setDefaultValue(AnnotationParameterFactory.from("offset"))
                                        .addParameter("required", AnnotationParameterFactory.from(false))
                                        .addParameter("defaultValue", AnnotationParameterFactory.from("0"))));
        reportControllerClass.addMethod(listMethod);

        String code = generator.generateSingleClass(reportControllerClass);
        System.out.println(code);
    }

}
