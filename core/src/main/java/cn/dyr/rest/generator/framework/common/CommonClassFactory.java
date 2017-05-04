package cn.dyr.rest.generator.framework.common;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.framework.j2ee.ServletTypeFactory;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.framework.jdk.JDKTypeFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataTypeFactory;
import cn.dyr.rest.generator.framework.spring.hateoas.SpringHATEOASTypeFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCConstant;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCTypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.ConstructorMethodInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.AnnotationFactory;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.MethodInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterValueFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 共有类元信息的工厂方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CommonClassFactory {

    /**
     * 构建用于组装异常信息 Map 对象的内部方法
     *
     * @return 用于组装异常信息 Map 对象的内部方法信息
     */
    private static MethodInfo buildResponseMapMethod() {
        TypeInfo mapType = CollectionsTypeFactory.mapWithGeneric(TypeInfoFactory.stringType(), TypeInfoFactory.objectType());
        TypeInfo hashMapType = CollectionsTypeFactory.hashMapWithGeneric(TypeInfoFactory.stringType(), TypeInfoFactory.objectType());
        TypeInfo linkedHashMapType = CollectionsTypeFactory.linkedHashMapWithGeneric(TypeInfoFactory.stringType(), TypeInfoFactory.objectType());

        MethodInfo buildResponseMapMethod = new MethodInfo()
                .setName("buildResponseMap")
                .setReturnValueType(mapType)
                .setPrivate()
                .addParameter(ParameterFactory.create(TypeInfoFactory.stringType(), "msg"))
                .addParameter(ParameterFactory.create(TypeInfoFactory.stringType(), "url"));

        // 2.2. 创建方法的指令
        // 2.2.1. 创建 retMap 变量初始化的指令
        IInstruction retMapDeclarationInstruction =
                InstructionFactory.variableDeclaration(
                        mapType, "retMap", ValueExpressionFactory.invokeConstructor(linkedHashMapType));
        IValueExpression retMapValueExpression = ValueExpressionFactory.variable("retMap");

        // 2.2.2. 创建 error 变量初始化的指令
        IInstruction errorDeclarationInstruction =
                InstructionFactory.variableDeclaration(
                        mapType, "error", ValueExpressionFactory.invokeConstructor(hashMapType));
        IValueExpression errorMapValueExpression = ValueExpressionFactory.variable("error");

        // 2.2.3. 创建 links 变量初始化的指令
        IInstruction linksDeclarationInstruction =
                InstructionFactory.variableDeclaration(
                        mapType, "links", ValueExpressionFactory.invokeConstructor(hashMapType));
        IValueExpression linksValueExpression = ValueExpressionFactory.variable("links");

        // 2.2.4. 创建 self 变量初始化的指令
        IInstruction selfDeclarationInstruction =
                InstructionFactory.variableDeclaration(
                        mapType, "self", ValueExpressionFactory.invokeConstructor(hashMapType));
        IValueExpression selfValueExpression = ValueExpressionFactory.variable("self");

        // 2.2.5. 将 errors 放到 retMap 当中
        IInstruction putErrorIntoRetMap =
                InstructionFactory.invoke(retMapValueExpression, "put", new Object[]{
                        "error", errorMapValueExpression
                });

        // 2.2.6. 将 links 放到 retMap 当中
        IInstruction putLinksIntoRetMap =
                InstructionFactory.invoke(retMapValueExpression, "put", new Object[]{
                        "_links", linksValueExpression
                });

        // 2.2.7. 将 msg 放到 error 当中
        IInstruction putMsgIntoError =
                InstructionFactory.invoke(errorMapValueExpression, "put", new Object[]{
                        "msg", ValueExpressionFactory.variable("msg")
                });

        // 2.2.8. 将 self 放到 links 当中
        IInstruction putSelfIntoLinks =
                InstructionFactory.invoke(linksValueExpression, "put", new Object[]{
                        "self", selfValueExpression
                });

        // 2.2.9. 将 url 放到 self 当中
        IInstruction putUrlInfoSelf =
                InstructionFactory.invoke(selfValueExpression, "put", new Object[]{
                        "href", ValueExpressionFactory.variable("url")
                });

        // 2.2.10. 返回 retMap
        IInstruction returnInstruction = InstructionFactory.returnInstruction(retMapValueExpression);

        // 2.2.X. 将上面的指令合并成一条指令，设置到方法当中
        IInstruction allInstruction = InstructionFactory.sequence(
                retMapDeclarationInstruction,
                errorDeclarationInstruction,
                linksDeclarationInstruction,
                selfDeclarationInstruction,
                InstructionFactory.emptyInstruction(),
                putErrorIntoRetMap,
                putLinksIntoRetMap,
                putMsgIntoError,
                putSelfIntoLinks,
                putUrlInfoSelf,
                InstructionFactory.emptyInstruction(),
                returnInstruction);

        buildResponseMapMethod.setRootInstruction(allInstruction);
        return buildResponseMapMethod;
    }

    /**
     * 创建用于处理 DBConstraintException 的异常处理方法
     *
     * @return 通用处理方法
     */
    private static MethodInfo handleDBConstraintException(TypeInfo exceptionType) {
        TypeInfo returnType =
                CollectionsTypeFactory.mapWithGeneric(TypeInfoFactory.stringType(), TypeInfoFactory.objectType());

        MethodInfo methodInfo = new MethodInfo()
                .setName("handleConstraintFailed")
                .setReturnValueType(returnType)
                .addAnnotationInfo(SpringMVCAnnotationFactory.exceptionHandler(exceptionType))
                .addAnnotationInfo(SpringMVCAnnotationFactory.responseStatus(SpringMVCConstant.HTTP_STATUS_MEMBER_BAD_REQUEST))
                .addAnnotationInfo(SpringMVCAnnotationFactory.responseBody())
                .addParameter(ParameterFactory.create(exceptionType, "exception"))
                .addParameter(ParameterFactory.create(ServletTypeFactory.httpServletRequest(), "request"));

        IValueExpression errMsgValueExpression = ValueExpressionFactory.variable("exception").invokeMethod("getMessage");
        IValueExpression urlValueExpression = ValueExpressionFactory.variable("request").invokeMethod("getRequestURL").invokeMethod("toString");

        IValueExpression returnValue =
                ValueExpressionFactory.thisReference().invokeMethod("buildResponseMap", new Object[]{
                        errMsgValueExpression, urlValueExpression
                });
        IInstruction returnInstruction = InstructionFactory.returnInstruction(returnValue);

        methodInfo.setRootInstruction(returnInstruction);

        return methodInfo;
    }

    /**
     * 创建一个用于全局异常处理的控制器增强类
     *
     * @param packageName 包名
     * @param context     转换过程中的数据上下文对象
     * @return 控制器增强类
     */
    public static ClassInfo commonExceptionHandler(String packageName, ConvertDataContext context) {
        // 1. 创建类的基本信息
        ClassInfo classInfo = new ClassInfo()
                .setPackageName(packageName)
                .setClassName("CommonExceptionHandler")
                .addAnnotation(SpringMVCAnnotationFactory.controllerAdvice());

        // 2. 构建各种的方法
        classInfo.addMethod(buildResponseMapMethod());
        classInfo.addMethod(handleDBConstraintException(
                context.getCommonClass(ConverterContext.KEY_DB_CONSTRAINT_EXCEPTION).getType()));

        return classInfo;
    }

    /**
     * 创建一个 RuntimeException 类的子类
     *
     * @param name        类名
     * @param packageName 包名
     * @return 异常类的类元信息
     */
    public static ClassInfo createSubTypeOfRuntimeException(String name, String packageName) {
        ClassInfo exceptionClass = new ClassInfo()
                .setClassName(name)
                .setPackageName(packageName)
                .extendClass(JDKTypeFactory.runtimeException());

        // 不带任何参数的构造方法
        ConstructorMethodInfo emptyParameterConstructor = new ConstructorMethodInfo(exceptionClass.getType());
        IInstruction instruction = InstructionFactory.superConstructor(new ParameterValue[]{});
        emptyParameterConstructor.setRootInstruction(instruction);

        exceptionClass.addMethod(emptyParameterConstructor);

        // 字符串参数的构造方法
        ConstructorMethodInfo msgConstructor = new ConstructorMethodInfo(exceptionClass.getType());
        Parameter msgParameter = new Parameter().setTypeInfo(TypeInfoFactory.stringType()).setName("message");
        msgConstructor.addParameter(msgParameter);

        IInstruction instruction1 = InstructionFactory.superConstructor(new ParameterValue[]{ParameterValueFactory.fromObject(ValueExpressionFactory.variable("message"))});
        msgConstructor.setRootInstruction(instruction1);

        exceptionClass.addMethod(msgConstructor);

        return exceptionClass;
    }

    /**
     * 创建一个用于呈现分页字段的资源类
     *
     * @param packageName 这个类所在包的包名
     * @return 用于呈现分页的资源页
     */
    public static ClassInfo createPagedResource(String packageName) {
        // 1.1. 创建类的基本信息
        ClassInfo pagedResourceClass = new ClassInfo()
                .setClassName("PagedResource")
                .setPackageName(packageName)
                .addGenericPlaceHolder(TypeInfoFactory.placeHolder("T"))
                .extendClass(SpringHATEOASTypeFactory.resourceSupportType());

        // 1.2. 添加字段信息
        FieldInfo contentField = new FieldInfo()
                .setName("content")
                .setType(CollectionsTypeFactory.listWithGeneric(pagedResourceClass.getGenericPlaceHolder(0)))
                .setPrivate();
        pagedResourceClass.addField(contentField);

        // 1.3. 创建构造函数
        ConstructorMethodInfo pagedResourceConstructor = new ConstructorMethodInfo(pagedResourceClass.getType());
        pagedResourceConstructor.addParameter(
                ParameterFactory.create(SpringDataTypeFactory.pageTypeWithGeneric(pagedResourceClass.getGenericPlaceHolder(0)), "page")
        );
        pagedResourceClass.addMethod(pagedResourceConstructor);

        // 1.3.1 构造函数的指令
        // 1.3.1.1  this.content = page.getContent();
        IValueExpression thisReference = ValueExpressionFactory.thisReference();
        IInstruction contentAssignmentInstruction = InstructionFactory.assignment(
                thisReference.accessField("content"),
                ValueExpressionFactory.variable("page").invokeMethod("getContent", new Object[]{})
        );

        // 1.3.1.2. 创建第一页的链接
        IValueExpression firstPageBuildPageLinkValue = thisReference.invokeMethod("buildPageLink", new Object[]{0,
                ValueExpressionFactory.variable("page").invokeMethod("getSize", new Object[]{}),
                ValueExpressionFactory.stringExpression("first")});
        IInstruction firstPageInstruction = InstructionFactory.invoke(
                thisReference, "add", new Object[]{firstPageBuildPageLinkValue});

        // 1.3.1.3. 创建最后一页的链接
        IValueExpression lastPageBuildPageLinkValue = thisReference.invokeMethod("buildPageLink", new Object[]{
                ValueExpressionFactory.subtract(
                        ValueExpressionFactory.variable("page").invokeMethod("getTotalPages", new Object[]{}),
                        ValueExpressionFactory.intExpression(1)
                ),
                ValueExpressionFactory.variable("page").invokeMethod("getSize", new Object[]{}),
                ValueExpressionFactory.stringExpression("last")
        });
        IInstruction lastPageInstruction = InstructionFactory.invoke(
                thisReference, "add", new Object[]{lastPageBuildPageLinkValue});

        // 1.3.1.4. 创建本页的链接
        IValueExpression selfPageBuildPageLinkValue = thisReference.invokeMethod("buildPageLink", new Object[]{
                ValueExpressionFactory.variable("page").invokeMethod("getNumber", new Object[]{}),
                ValueExpressionFactory.variable("page").invokeMethod("getSize", new Object[]{}),
                ValueExpressionFactory.stringExpression("self")
        });
        IInstruction selfPageInstruction = InstructionFactory.invoke(
                thisReference, "add", new Object[]{selfPageBuildPageLinkValue});

        // 1.3.1.5. 创建上一页的链接
        IValueExpression prevPageCondition = ValueExpressionFactory.logicalNot(
                ValueExpressionFactory.variable("page").invokeMethod("isFirst", new Object[]{}));
        IValueExpression prevPageBuildLinkValue = thisReference.invokeMethod("buildPageLink", new Object[]{
                ValueExpressionFactory.subtract(
                        ValueExpressionFactory.variable("page").invokeMethod("getNumber", new Object[]{}),
                        ValueExpressionFactory.intExpression(1)),
                ValueExpressionFactory.variable("page").invokeMethod("getSize", new Object[]{}),
                ValueExpressionFactory.stringExpression("prev")
        });
        IInstruction prevPageLinkInstruction = InstructionFactory.choiceBuilder(
                prevPageCondition,
                InstructionFactory.invoke(thisReference, "add", new Object[]{prevPageBuildLinkValue})).build();

        // 1.3.1.6. 创建下一页的链接
        IValueExpression nextPageCondition = ValueExpressionFactory.logicalNot(
                ValueExpressionFactory.variable("page").invokeMethod("isLast", new Object[]{}));
        IValueExpression nextPageBuildLinkValue = thisReference.invokeMethod("buildPageLink", new Object[]{
                ValueExpressionFactory.plus(
                        ValueExpressionFactory.variable("page").invokeMethod("getNumber", new Object[]{}),
                        ValueExpressionFactory.intExpression(1)),
                ValueExpressionFactory.variable("page").invokeMethod("getSize"),
                ValueExpressionFactory.stringExpression("next")
        });
        IInstruction nextPageLinkInstruction = InstructionFactory.choiceBuilder(nextPageCondition,
                InstructionFactory.invoke(thisReference, "add", new Object[]{nextPageBuildLinkValue})).build();

        // 1.3.x. 指令的整合
        IInstruction allInstruction = InstructionFactory.sequence(
                contentAssignmentInstruction,
                InstructionFactory.emptyInstruction(),
                firstPageInstruction,
                lastPageInstruction,
                selfPageInstruction,
                InstructionFactory.emptyInstruction(),
                prevPageLinkInstruction,
                InstructionFactory.emptyInstruction(),
                nextPageLinkInstruction);
        pagedResourceConstructor.setRootInstruction(allInstruction);

        // 1.4. buildPageLink 函数的创建
        MethodInfo buildPageLinkMethod = new MethodInfo()
                .setReturnValueType(SpringHATEOASTypeFactory.linkType())
                .setPrivate()
                .setName("buildPageLink")
                .addParameter(ParameterFactory.create(TypeInfoFactory.intType(), "page"))
                .addParameter(ParameterFactory.create(TypeInfoFactory.intType(), "size"))
                .addParameter(ParameterFactory.create(TypeInfoFactory.stringType(), "rel"));
        pagedResourceClass.addMethod(buildPageLinkMethod);

        // 1.4.1. 从当前的请求 URL 创建一个 uriBuilder 对象
        IInstruction builderGetInstruction = InstructionFactory.variableDeclaration(
                SpringMVCTypeFactory.servletUriComponentsBuilder(), "builder",
                ValueExpressionFactory.classForStatic(SpringMVCTypeFactory.servletUriComponentsBuilder())
                        .invokeMethod("fromCurrentRequestUri", new Object[]{}));
        IInstruction urlBuildInstruction = InstructionFactory.variableDeclaration(TypeInfoFactory.stringType(), "url",
                ValueExpressionFactory.variable("builder")
                        .invokeMethod("queryParam", new Object[]{"page", ValueExpressionFactory.variable("page")})
                        .invokeMethod("queryParam", new Object[]{"size", ValueExpressionFactory.variable("size")})
                        .invokeMethod("build", new Object[]{})
                        .invokeMethod("toUriString", new Object[]{}));
        IInstruction linkReturnInstruction = InstructionFactory.returnInstruction(
                ValueExpressionFactory.invokeConstructor(
                        SpringHATEOASTypeFactory.linkType(),
                        new Object[]{
                                ValueExpressionFactory.variable("url"),
                                ValueExpressionFactory.variable("rel")}));

        // 1.4.x. 构建一个顺序指令
        IInstruction buildPageLinkMethodRootInstruction = InstructionFactory.sequence(
                builderGetInstruction,
                urlBuildInstruction,
                linkReturnInstruction);
        buildPageLinkMethod.setRootInstruction(buildPageLinkMethodRootInstruction);

        // 1.5. getContent 函数的创建
        MethodInfo contentGetter = MethodInfoFactory.getter(contentField);
        pagedResourceClass.addMethod(contentGetter);

        return pagedResourceClass;
    }

}
