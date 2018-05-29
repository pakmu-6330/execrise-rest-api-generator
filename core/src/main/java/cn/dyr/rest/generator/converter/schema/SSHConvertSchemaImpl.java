package cn.dyr.rest.generator.converter.schema;

import cn.dyr.rest.generator.bridge.channel.MessageChannel;
import cn.dyr.rest.generator.bridge.message.Message;
import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.clazz.DefaultControllerConverter;
import cn.dyr.rest.generator.converter.clazz.DefaultDAOConverter;
import cn.dyr.rest.generator.converter.clazz.DefaultEntityClassConverter;
import cn.dyr.rest.generator.converter.clazz.DefaultResourceAssemblerConverter;
import cn.dyr.rest.generator.converter.clazz.DefaultResourceClassConverter;
import cn.dyr.rest.generator.converter.clazz.DefaultServiceConverter;
import cn.dyr.rest.generator.converter.clazz.IControllerConverter;
import cn.dyr.rest.generator.converter.clazz.IDAOConverter;
import cn.dyr.rest.generator.converter.clazz.IEntityClassConverter;
import cn.dyr.rest.generator.converter.clazz.IResourceAssemblerConverter;
import cn.dyr.rest.generator.converter.clazz.IResourceClassConverter;
import cn.dyr.rest.generator.converter.clazz.IServiceConverter;
import cn.dyr.rest.generator.converter.field.DefaultFieldConverter;
import cn.dyr.rest.generator.converter.field.IFieldConverter;
import cn.dyr.rest.generator.converter.instruction.DefaultServiceInstructionConverter;
import cn.dyr.rest.generator.converter.method.DefaultControllerMethodConverter;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.converter.name.KeywordFilterConverter;
import cn.dyr.rest.generator.converter.type.DefaultTypeConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.framework.common.CommonClassFactory;
import cn.dyr.rest.generator.framework.jdk.JDKAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.SpringFrameworkAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.boot.SpringBootAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.boot.SpringBootConstant;
import cn.dyr.rest.generator.framework.spring.data.SpringDataAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.data.SpringDataTypeFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCTypeFactory;
import cn.dyr.rest.generator.framework.swagger.SwaggerAnnotationFactory;
import cn.dyr.rest.generator.framework.swagger.SwaggerTypeFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.InstructionFactory;
import cn.dyr.rest.generator.java.meta.factory.MethodInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ParameterFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.ValueExpressionFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.flow.expression.IValueExpression;
import cn.dyr.rest.generator.project.Project;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.MethodInfoUtils;
import cn.dyr.rest.generator.util.PackageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ASSEMBLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_CONTROLLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_DAO_INTERFACE;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_RESOURCE_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_SERVICE_IMPL_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_SERVICE_INTERFACE;

/**
 * 这个方案是用于生成基于 Spring, Spring MVC 和 Hibernate 三个框架的 REST 风格 Web API 实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SSHConvertSchemaImpl implements IConvertSchema {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(SSHConvertSchemaImpl.class);
    }

    @DataInject(DataInjectType.ENTITY_LIST)
    private List<EntityInfo> entityInfoList;

    @DataInject(DataInjectType.RELATIONSHIP_LIST)
    private List<EntityRelationship> entityRelationshipList;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig converterConfig;

    @DataInject(DataInjectType.BASE_PACKAGE_NAME)
    private String basePackageName;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    @DataInject(DataInjectType.PROJECT)
    private Project project;

    @ConverterInject(ConverterInjectType.ENTITY)
    private IEntityClassConverter entityClassConverter;

    @ConverterInject(ConverterInjectType.DAO)
    private IDAOConverter daoConverter;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @ConverterInject(ConverterInjectType.SERVICE)
    private IServiceConverter serviceConverter;

    @ConverterInject(ConverterInjectType.CONTROLLER)
    private IControllerConverter controllerConverter;

    @ConverterInject(ConverterInjectType.RESOURCE)
    private IResourceClassConverter resourceClassConverter;

    @ConverterInject(ConverterInjectType.RESOURCE_ASSEMBLER)
    private IResourceAssemblerConverter resourceAssemblerConverter;

    @ConverterInject(ConverterInjectType.FIELD)
    private IFieldConverter fieldConverter;

    private MessageChannel messageChannel;

    public SSHConvertSchemaImpl() {
        this.messageChannel = MessageChannel.getDefaultChannel();
    }

    @Override
    public void getConverterList(SchemaConverterList schemaConverterList) {
        schemaConverterList.setNameConverter(KeywordFilterConverter.class)
                .setTypeConverter(DefaultTypeConverter.class)
                .setFieldConverter(DefaultFieldConverter.class)
                .setEntityClassConverter(DefaultEntityClassConverter.class)
                .setDaoConverter(DefaultDAOConverter.class)
                .setServiceConverter(DefaultServiceConverter.class)
                .setServiceInstructionConverter(DefaultServiceInstructionConverter.class)
                .setControllerConverter(DefaultControllerConverter.class)
                .setControllerMethodConverter(DefaultControllerMethodConverter.class)
                .setResourceClassConverter(DefaultResourceClassConverter.class)
                .setResourceAssemblerConverter(DefaultResourceAssemblerConverter.class);
    }

    /**
     * 创建 Spring Boot 的主类
     */
    private void createSpringBootRootApplication() {
        // 1. 创建类信息
        ClassInfo mainClass = new ClassInfo()
                .setPackageName(PackageUtils.getRootPackageName(this.basePackageName))
                .setClassName("MainApplication");

        if (this.converterConfig.isCrossOriginEnabled()) {
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                mainClass.extendClass(SpringMVCTypeFactory.webMvcConfigurerAdapter());
            } else {
                mainClass.implementInterface(SpringMVCTypeFactory.webMvcConfigurer());
            }
        }

        // 2. 创建相应的注解
        AnnotationInfo appAnnotation = SpringBootAnnotationFactory.appAnnotation();
        mainClass.addAnnotation(appAnnotation);

        AnnotationInfo enableJPARepositories =
                SpringDataAnnotationFactory.enableJPARepositories(
                        PackageUtils.getSubPackage(this.basePackageName, converterConfig.getDaoPackageName()));
        mainClass.addAnnotation(enableJPARepositories);

        // 3. 创建 main 方法
        MethodInfo mainMethod = MethodInfoFactory.createMainMethod();
        mainClass.addMethod(mainMethod);

        // 4. Spring Boot 的固定套路
        TypeInfo springApplicationType = TypeInfoFactory.fromClass(SpringBootConstant.SPRING_APPLICATION);
        IInstruction mainInstruction = InstructionFactory.invokeStaticMethod(
                springApplicationType, false, "run", new Object[]{
                        mainClass, ValueExpressionFactory.variable("args")
                });
        mainMethod.setRootInstruction(mainInstruction);

        // 5. 判断是否支持跨域进行相关的配置
        if (converterConfig.isCrossOriginEnabled()) {
            MethodInfo crosConfigMethod = new MethodInfo()
                    .setName("addCorsMappings")
                    .addParameter(ParameterFactory.create(SpringMVCTypeFactory.corsRegistry(), "registry"))
                    .addAnnotationInfo(JDKAnnotationFactory.override());

            // 调用父类的相关方法进行配置
            IValueExpression registryValueExpression = ValueExpressionFactory.variable("registry");
            IInstruction parentAddCrosMappingsInstruction = InstructionFactory.invoke(
                    ValueExpressionFactory.parent(), "addCorsMappings", new Object[]{registryValueExpression});

            // 进行跨域的相应配置
            IInstruction configInstruction = InstructionFactory.invoke(registryValueExpression, "addMapping", "/**")
                    .invoke("allowedOrigins", "*")
                    .invoke("allowedHeaders", "*")
                    .invoke("allowCredentials", true)
                    .invoke("allowedMethods", "*")
                    .invoke("maxAge", 3600);

            IInstruction rootInstruction = null;
            if (project.getSpringBootVersion().getMajorVersion() == 1) {
                rootInstruction = InstructionFactory.sequence(
                        parentAddCrosMappingsInstruction,
                        InstructionFactory.emptyInstruction(),
                        configInstruction);
            } else {
                rootInstruction = configInstruction;
            }

            crosConfigMethod.setRootInstruction(rootInstruction);

            mainClass.addMethod(crosConfigMethod);
        }

        // 6. 将类保存到包当中
        this.convertDataContext.saveClassByPackageName(mainClass);
    }


    /**
     * 创建 Swagger 的主配置类信息
     */
    private void createSwaggerConfigClass() {
        ClassInfo classInfo = new ClassInfo()
                .setPackageName(PackageUtils.getRootPackageName(this.basePackageName))
                .setClassName("SwaggerConfig")
                .addAnnotation(SpringFrameworkAnnotationFactory.configuration())
                .addAnnotation(SwaggerAnnotationFactory.enableSwagger());

        // 1. apiInfo 方法的创建
        {
            MethodInfo apiInfoMethod = new MethodInfo()
                    .setName("apiInfo")
                    .setReturnValueType(SwaggerTypeFactory.apiInfo());

            IValueExpression returnExpression = ValueExpressionFactory.invokeConstructor(SwaggerTypeFactory.apiInfoBuilder())
                    .invokeMethod("title", new Object[]{this.project.getProjectName()})
                    .invokeMethod("version", new Object[]{this.project.getVersion()})
                    .invokeMethod("build");
            IInstruction returnInstruction = InstructionFactory.returnInstruction(returnExpression);
            apiInfoMethod.setRootInstruction(returnInstruction);

            classInfo.addMethod(apiInfoMethod);
        }

        // 2. docket 方法的创建
        {
            MethodInfo docketMethod = new MethodInfo()
                    .addAnnotationInfo(SpringFrameworkAnnotationFactory.bean())
                    .setName("docket")
                    .setReturnValueType(SwaggerTypeFactory.docket());

            IValueExpression returnValueExpression =
                    ValueExpressionFactory.invokeConstructor(SwaggerTypeFactory.docket(), new Object[]{ValueExpressionFactory.classForStatic(SwaggerTypeFactory.documentType()).accessField("SWAGGER_2")})
                            .invokeMethod("apiInfo", ValueExpressionFactory.thisReference().invokeMethod("apiInfo"))
                            .invokeMethod("select")
                            .invokeMethod("apis", ValueExpressionFactory.classForStatic(SwaggerTypeFactory.requestHandlerSelectors()).invokeMethod("basePackage", this.basePackageName))
                            .invokeMethod("paths", ValueExpressionFactory.classForStatic(SwaggerTypeFactory.pathSelector()).invokeMethod("any"))
                            .invokeMethod("build")
                            .invokeMethod("ignoredParameterTypes", SpringDataTypeFactory.pageable())
                            .invokeMethod("useDefaultResponseMessages", false);
            IInstruction returnInstruction = InstructionFactory.returnInstruction(returnValueExpression);
            docketMethod.setRootInstruction(returnInstruction);

            classInfo.addMethod(docketMethod);
        }

        this.convertDataContext.saveClassByPackageName(classInfo);
    }

    /**
     * 这个方法用于创建一些异常类
     */
    private void createExceptionClasses() {
        // 1. 数据库外键约束异常类
        ClassInfo dbConstraintException =
                CommonClassFactory.createSubTypeOfRuntimeException("DBConstraintException",
                        PackageUtils.getSubPackage(this.basePackageName, converterConfig.getExceptionPackageName()));

        this.convertDataContext.saveCommonClass(ConverterContext.KEY_DB_CONSTRAINT_EXCEPTION, dbConstraintException);
    }

    /**
     * 这个方法用于创建一些共有类
     */
    private void createCommonClasses() {
        // 1. PagedResource 类
        ClassInfo pagedResourceClass =
                CommonClassFactory.createPagedResource(
                        PackageUtils.getSubPackage(this.basePackageName, converterConfig.getCommonPackageName()));
        this.convertDataContext.saveCommonClass(ConverterContext.KEY_PAGED_RESOURCE, pagedResourceClass);

        // 2. CommonExceptionHandler 类
        ClassInfo commonExceptionHandlerClass =
                CommonClassFactory.commonExceptionHandler(
                        PackageUtils.getSubPackage(this.basePackageName,
                                converterConfig.getControllerPackageName()), convertDataContext);
        this.convertDataContext.saveCommonClass(ConverterContext.KEY_COMMON_EXCEPTION_HANDLER, commonExceptionHandlerClass);
    }


    /**
     * 根据实体信息创建实体类
     */
    private void createEntityClasses() {
        if (this.entityInfoList == null || this.entityInfoList.size() == 0) {
            logger.warn("generation was terminated because no entity was found in context");
            return;
        }

        // 1. 生成实体类信息
        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo classInfo = this.entityClassConverter.basicInfo(entityInfo);
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS, classInfo);
        }

        // 2. 生成关联关系相关的字段
        for (EntityRelationship relationship : this.entityRelationshipList) {
            this.entityClassConverter.processRelationship(relationship);
        }

        // 3. 生成类相应的 get set 方法
        Iterator<ClassInfo> entityClassInfoIterator = convertDataContext.iterateEntityClassInfo();
        while (entityClassInfoIterator.hasNext()) {
            ClassInfo entityClassInfo = entityClassInfoIterator.next();
            ClassInfoUtils.createBothGetterAndSetter(entityClassInfo, converterConfig.isBuilderStyleSetter());
        }

        // 4. 进行数据库结构的微调
        this.fieldConverter.postRelationshipProcess();
    }

    /**
     * 创建相关的 Spring Data DAO 接口
     */
    private void createDaoInterfaces() {
        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo interfaceClass = this.daoConverter.fromEntity(entityInfo);

            // 4. 将接口元数据保存起来
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_DAO_INTERFACE, interfaceClass);
            this.convertDataContext.saveDAODefaultFieldName(
                    entityInfo.getName(), this.nameConverter.defaultNameOfVariableName(interfaceClass.getClassName()));
        }
    }

    /**
     * 创建 Service 类元信息
     */
    private void createServiceClasses() {
        // 这里定义一些整个生成过程当中都会使用到的变量

        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo serviceClass = this.serviceConverter.fromEntity(entityInfo);

            // 根据 Service 类产生相应的接口
            ClassInfo serviceInterface = new ClassInfo()
                    .setClassName(this.nameConverter.serviceInterfaceNameFromEntityName(entityInfo.getName()))
                    .setPackageName(PackageUtils.getSubPackage(this.basePackageName, converterConfig.getServiceInterfacePackageName()))
                    .setInterface(true);
            serviceClass.implementInterface(serviceInterface.getType());

            Iterator<MethodInfo> methodInfoIterator = serviceClass.iterateMethods();
            TypeInfo overrideType = JDKAnnotationFactory.override().getType();

            while (methodInfoIterator.hasNext()) {
                MethodInfo methodInfo = methodInfoIterator.next();
                if (MethodInfoUtils.isMethodContainsAnnotationType(methodInfo, overrideType)) {
                    MethodInfo interfaceMethod = MethodInfoUtils.createMethodWithSameSignature(methodInfo);
                    interfaceMethod.setDefineOnly(true);

                    serviceInterface.addMethod(interfaceMethod);
                }
            }

            // 将 Service 类保存起来
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_SERVICE_INTERFACE, serviceInterface);
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_SERVICE_IMPL_CLASS, serviceClass);
            this.convertDataContext.saveServiceDefaultFieldName(entityInfo.getName(), this.nameConverter.defaultNameOfVariableName(serviceInterface.getClassName()));
        }
    }


    /**
     * 创建 Controller 类
     */
    private void createControllerClasses() {
        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo controllerClass = this.controllerConverter.basicInfo(entityInfo);
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_CONTROLLER_CLASS, controllerClass);
        }
    }


    /**
     * 完善 Controller 类的信息
     */
    private void fillControllerClass() {
        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo classInfo = this.controllerConverter.convertMethod(entityInfo);
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_CONTROLLER_CLASS, classInfo);
        }
    }

    /**
     * 创建用于 Spring HATEOAS 的资源类
     */
    private void createHateoasResourceClasses() {
        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo resourceClassInfo = this.resourceClassConverter.fromEntity(entityInfo);

            // 将这个类保存起来
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_RESOURCE_CLASS, resourceClassInfo);
        }
    }

    /**
     * 创建资源的装配类
     */
    private void createResourceAssembler() {
        // 一些能够通用的变量

        for (EntityInfo entityInfo : this.entityInfoList) {
            ClassInfo assemblerClass = this.resourceAssemblerConverter.toResourceAssembler(entityInfo);

            // X. 将这个类保存起来
            this.convertDataContext.saveClassByEntityAndType(entityInfo.getName(), TYPE_ASSEMBLER_CLASS, assemblerClass);
            this.convertDataContext.saveAssemblerDefaultFieldName(entityInfo.getName(), this.nameConverter.defaultNameOfVariableName(assemblerClass.getClassName()));
        }
    }

    private void pushProgressMessage(String message) {
        Message msgObj = new Message();
        msgObj.setType(Message.TYPE_GENERATE_PROGRESS);
        msgObj.setData(message);

        messageChannel.pushMessage(msgObj);
    }

    @Override
    public boolean generate(Project project) {
        if (this.entityInfoList == null || this.entityInfoList.size() == 0) {
            logger.debug("generation was terminated because no entity was found in context");
            return false;
        }

        // 创建主类
        createSpringBootRootApplication();
        pushProgressMessage("Spring Boot 主类创建生活完毕");

        // 创建 Swagger 的配置类
        createSwaggerConfigClass();
        pushProgressMessage("Swagger 配置类生成完毕");

        // 创建异常类
        createExceptionClasses();
        pushProgressMessage("异常类生成完毕");

        // 先创建共有类
        createCommonClasses();
        pushProgressMessage("通用类生成完毕");

        // 生成实体类（PO 类）
        createEntityClasses();
        pushProgressMessage("实体类生成完毕");

        // 生成 Spring Data DAO 接口
        createDaoInterfaces();
        pushProgressMessage("DAO 接口生成完毕");

        // 生成 Service 类
        createServiceClasses();
        pushProgressMessage("Service 接口和实现类生成完毕");

        // 创建 Controller 类
        createControllerClasses();
        pushProgressMessage("Controller 类基本信息生成完毕");

        // 创建 HATEOAS 相关类
        createHateoasResourceClasses();
        pushProgressMessage("HATEOAS 资源类生成完毕");

        // 创建 HATEOAS 资源装配类
        createResourceAssembler();
        pushProgressMessage("HATEOAS 资源装配类生成完毕");

        // 完善 Controller 类信息
        fillControllerClass();
        pushProgressMessage("Controller 类方法生成完毕");

        return true;
    }

}
