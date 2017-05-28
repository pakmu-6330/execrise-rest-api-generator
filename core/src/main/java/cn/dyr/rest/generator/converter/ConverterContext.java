package cn.dyr.rest.generator.converter;

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
import cn.dyr.rest.generator.converter.instruction.IServiceInstructionConverter;
import cn.dyr.rest.generator.converter.method.DefaultControllerMethodConverter;
import cn.dyr.rest.generator.converter.method.IControllerMethodConverter;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.converter.name.KeywordFilterConverter;
import cn.dyr.rest.generator.converter.type.DefaultTypeConverter;
import cn.dyr.rest.generator.converter.type.ITypeConverter;
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
import cn.dyr.rest.generator.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ASSEMBLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_CONTROLLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_DAO_INTERFACE;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_RESOURCE_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_SERVICE_IMPL_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_SERVICE_INTERFACE;

/**
 * 一个转换操作的上下文对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConverterContext {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(ConverterContext.class);
    }

    /**
     * 用于描述分页资源的 PagedResource 类
     */
    public static final String KEY_PAGED_RESOURCE = "paged-resource";

    /**
     * 用于表示数据库外键约束异常的异常类
     */
    public static final String KEY_DB_CONSTRAINT_EXCEPTION = "db-constraint-exception";

    /**
     * 全局的异常处理类
     */
    public static final String KEY_COMMON_EXCEPTION_HANDLER = "common-exception-handler";

    // 一堆转换器的定义
    private INameConverter nameConverter;
    private ITypeConverter typeConverter;
    private IFieldConverter fieldConverter;
    private IServiceInstructionConverter instructionConverter;
    private IControllerConverter controllerConverter;
    private IControllerMethodConverter controllerMethodConverter;
    private IResourceAssemblerConverter resourceAssemblerConverter;
    private IDAOConverter daoConverter;
    private IServiceConverter serviceConverter;
    private IEntityClassConverter entityClassConverter;
    private IResourceClassConverter resourceClassConverter;

    // 项目信息
    private Project project;

    // 基包名
    private String basePackageName;

    // 一些配置
    private ConverterConfig converterConfig;

    private List<EntityInfo> entityInfoList;
    private List<EntityRelationship> entityRelationshipList;

    private ClassInfo mainClass;                                    // 程序入口类
    private ClassInfo swaggerConfigClass;                           // Swagger 的配置类

    private List<ClassInfo> commonClasses;

    private ConvertDataContext convertDataContext;                // 字段信息管理类

    /**
     * 创建一个转换操作的上下文对象
     *
     * @param project 项目信息
     */
    public ConverterContext(Project project) {
        // 对包名进行预处理
        this.project = project;
        this.basePackageName = project.getBasePackage();
        if (StringUtils.isStringEmpty(this.basePackageName)) {
            this.basePackageName = project.getAuthor();
        }

        // 加载配置文件
        this.converterConfig = ConverterConfig.fromFile(
                this.getClass().getClassLoader().getResourceAsStream("cn/dyr/rest/generator/config/converter.xml"));

        // 初始化各种的容器
        this.entityInfoList = new ArrayList<>();
        this.entityRelationshipList = new ArrayList<>();
        this.commonClasses = new ArrayList<>();
        this.convertDataContext = new ConvertDataContext(basePackageName);

        // 加载各种各样的转换器
        this.initConverters();
    }

    /**
     * 根据实体的名称获得实体对象
     *
     * @param entityName 实体名称
     * @return 这个实体名称对应的
     */
    public EntityInfo getEntityInfoByName(String entityName) {
        return this.convertDataContext.getEntityByName(entityName);
    }

    /**
     * 根据实体类寻找相应的关联关系对象。这个方法寻找的关联关系对象满足下面的几个条件：
     * <ul>
     * <li>给定的实体类存在与这个关联关系对象</li>
     * <li>在这个关联关系当中，这个实体存在指向另外一个方向的实体</li>
     * </ul>
     * <br>
     * 另外，下面条件只要满足其中一个，关联关系对象即会被排除：
     * <ul>
     * <li>自身与自身的关系</li>
     * </ul>
     *
     * @param entityInfo 实体类
     * @return 满足条件的关联关系对象的集合
     */
    public List<EntityRelationship> getRelationshipByEntity(EntityInfo entityInfo) {
        List<EntityRelationship> retValue = new ArrayList<>();

        for (EntityRelationship relationship : this.entityRelationshipList) {
            String endAEntityName = relationship.getEndA().getName();
            String endBEntityName = relationship.getEndB().getName();

            // 实体自身的关联关系，排除
            if (endAEntityName.equals(endBEntityName)) {
                continue;
            }

            // 逐个条件进行验证
            if (endAEntityName.equals(entityInfo.getName()) && relationship.hasAToB()) {
                if (!retValue.contains(relationship)) {
                    retValue.add(relationship);
                }

                continue;
            }

            if (endBEntityName.equals(entityInfo.getName()) && relationship.hasBToA()) {
                if (!retValue.contains(relationship)) {
                    retValue.add(relationship);
                }
            }
        }

        return retValue;
    }

    /**
     * 获得转换器的配置信息
     *
     * @return 转换器的配置信息
     */
    public ConverterConfig getConverterConfig() {
        return converterConfig;
    }

    /**
     * 设置转换器的配置信息
     *
     * @param converterConfig 要设置到转换器中的配置信息
     * @return 转换器本身
     */
    public ConverterContext setConverterConfig(ConverterConfig converterConfig) {
        Objects.requireNonNull(converterConfig);

        this.converterConfig = converterConfig;
        return this;
    }

    /**
     * 从配置文件中读取各种元素转换器配置，并创建相应的转换器实例
     */
    private void initConverters() {
        try {
            Class<?> nameConverterClass = Class.forName(converterConfig.getNameConverterClass());
            Class<?> typeConverterClass = Class.forName(converterConfig.getTypeConverterClass());
            Class<?> fieldConverterClass = Class.forName(converterConfig.getFieldConverterClass());
            Class<?> instructionConverterClass = Class.forName(converterConfig.getServiceInstructionConverterClass());
            Class<?> controllerMethodConverterClass = Class.forName(converterConfig.getControllerMethodConverterClass());
            Class<?> resourceAssemblerConverterClass = Class.forName(converterConfig.getHateoasResourceAssemblerConverterClass());
            Class<?> serviceConverterClass = Class.forName(converterConfig.getServiceConverterClass());
            Class<?> daoConverterClass = Class.forName(converterConfig.getDaoConverterClass());
            Class<?> controllerClassConverterClass = Class.forName(converterConfig.getControllerConverterClass());
            Class<?> entityClassConverterClass = Class.forName(converterConfig.getEntityConverterClass());
            Class<?> resourceClassConverterClass = Class.forName(converterConfig.getHateoasResourceConverterClass());

            // 初始化名称转换器
            Object rawNameConverter = nameConverterClass.newInstance();
            if (rawNameConverter instanceof INameConverter) {
                this.nameConverter = (INameConverter) rawNameConverter;
            } else {
                logger.warn("{} is not implementation of INameConverter, using default impl class",
                        converterConfig.getNameConverterClass());

            }

            // 初始化类型转换器
            Object rawTypeConverter = typeConverterClass.newInstance();
            if (rawTypeConverter instanceof ITypeConverter) {
                this.typeConverter = (ITypeConverter) rawTypeConverter;
            } else {
                logger.warn("{} is not implementation of ITypeConverter, using default impl class",
                        converterConfig.getTypeConverterClass());
            }

            // 初始化属性转换器
            Object rawFieldConverter = fieldConverterClass.newInstance();
            if (rawFieldConverter instanceof IFieldConverter) {
                this.fieldConverter = (IFieldConverter) rawFieldConverter;
            } else {
                logger.warn("{} is not implementation of IFieldConverter, using default impl class",
                        converterConfig.getFieldConverterClass());
            }

            // 初始化指令转换器
            Object rawInstructionConverter = instructionConverterClass.newInstance();
            if (rawInstructionConverter instanceof IServiceInstructionConverter) {
                this.instructionConverter = (IServiceInstructionConverter) rawInstructionConverter;
            } else {
                logger.warn("{} is not implementation of IServiceInstructionConverter, using default impl class",
                        converterConfig.getServiceInstructionConverterClass());
            }

            // 初始化方法转换器
            Object rawControllerMethodConverter = controllerMethodConverterClass.newInstance();
            if (rawControllerMethodConverter instanceof IControllerMethodConverter) {
                this.controllerMethodConverter = (IControllerMethodConverter) rawControllerMethodConverter;
            } else {
                logger.warn("{} is not implementation of IControllerMethodConverter, using default impl class");
            }

            // 初始化资源装配器转换器
            Object rawResourceAssemblerConverter = resourceAssemblerConverterClass.newInstance();
            if (rawResourceAssemblerConverter instanceof IResourceAssemblerConverter) {
                this.resourceAssemblerConverter = (IResourceAssemblerConverter) rawResourceAssemblerConverter;
            } else {
                logger.warn("{} is not implementation of IResourceAssemblerConverter, using default impl class");
            }

            // 初始化 DAO 类元信息的转换器
            Object rawDaoConverterClass = daoConverterClass.newInstance();
            if (rawDaoConverterClass instanceof IDAOConverter) {
                this.daoConverter = (IDAOConverter) rawDaoConverterClass;
            } else {
                logger.warn("{} is not implementation of IDAOConverter, using default impl class");
            }

            // 初始化 Service 类元信息的转换器
            Object rawServiceConverterClass = serviceConverterClass.newInstance();
            if (rawServiceConverterClass instanceof IServiceConverter) {
                this.serviceConverter = (IServiceConverter) rawServiceConverterClass;
            } else {
                logger.warn("{} is not implementation of IServiceConverter, using default impl class");
            }

            // 初始化 Controller 类的转换器
            Object rawControllerConverterClass = controllerClassConverterClass.newInstance();
            if (rawControllerConverterClass instanceof IControllerConverter) {
                this.controllerConverter = (IControllerConverter) rawControllerConverterClass;
            } else {
                logger.warn("{} is not implementation of IControllerConverter, using default impl class");
            }

            // 初始化实体类的转换器
            Object rawEntityClassConverter = entityClassConverterClass.newInstance();
            if (rawEntityClassConverter instanceof IEntityClassConverter) {
                this.entityClassConverter = (IEntityClassConverter) rawEntityClassConverter;
            } else {
                logger.warn("{} is not implementation of IEntityClassConverter, using default impl class");
            }

            // 初始化资源类转换器
            Object rawResourceClassConverter = resourceClassConverterClass.newInstance();
            if (rawResourceClassConverter instanceof IResourceClassConverter) {
                this.resourceClassConverter = (IResourceClassConverter) rawResourceClassConverter;
            } else {
                logger.warn("{} is not implementation of IResourceClassConverter, using default impl class");
            }

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            logger.error("failed to init converter", e);
        } finally {
            initDefaultConverters();
        }
    }

    /**
     * 对所有的转换器进行依赖注入
     */
    private void injectAllConverter() {
        injectConverter(this.nameConverter);
        injectConverter(this.typeConverter);
        injectConverter(this.fieldConverter);
        injectConverter(this.instructionConverter);
        injectConverter(this.controllerMethodConverter);
        injectConverter(this.resourceAssemblerConverter);
        injectConverter(this.daoConverter);
        injectConverter(this.serviceConverter);
        injectConverter(this.controllerConverter);
        injectConverter(this.entityClassConverter);
        injectConverter(this.resourceClassConverter);
    }

    /**
     * 根据标记进行转换器的注入
     *
     * @param converter 要进行注入的转换器
     */
    private void injectConverter(IConverter converter) {
        if (converter == null) {
            return;
        }

        Field[] declaredFields = converter.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                if (field.isAnnotationPresent(ConverterInject.class)) {
                    ConverterInject annotation = field.getAnnotation(ConverterInject.class);

                    // 根据参数确定要进行注入的类型
                    ConverterInjectType value = annotation.value();
                    field.setAccessible(true);
                    switch (value) {
                        case NAME:
                            field.set(converter, this.nameConverter);
                            break;
                        case TYPE:
                            field.set(converter, this.typeConverter);
                            break;
                        case FIELD:
                            field.set(converter, this.fieldConverter);
                            break;
                        case ENTITY:
                            field.set(converter, this.entityClassConverter);
                            break;
                        case DAO:
                            field.set(converter, this.daoConverter);
                            break;
                        case SERVICE_INSTRUCTION:
                            field.set(converter, this.instructionConverter);
                            break;
                        case RESOURCE:
                            field.set(converter, this.resourceClassConverter);
                            break;
                        case RESOURCE_ASSEMBLER:
                            field.set(converter, this.resourceAssemblerConverter);
                            break;
                        case CONTROLLER_METHOD:
                            field.set(converter, this.controllerMethodConverter);
                            break;
                        case CONTROLLER:
                            field.set(converter, this.controllerConverter);
                            break;
                        case SERVICE:
                            field.set(converter, this.serviceConverter);
                            break;
                    }
                } else if (field.isAnnotationPresent(DataInject.class)) {
                    DataInject annotation = field.getAnnotation(DataInject.class);

                    // 根据参数确定要进行注入的类型
                    DataInjectType value = annotation.value();
                    field.setAccessible(true);
                    switch (value) {
                        case CONFIG:
                            field.set(converter, this.converterConfig);
                            break;
                        case DATA_CONTEXT:
                            field.set(converter, this.convertDataContext);
                            break;
                        case ENTITY_LIST:
                            field.set(converter, this.entityInfoList);
                            break;
                        case RELATIONSHIP_LIST:
                            field.set(converter, this.entityRelationshipList);
                            break;
                        case ENTITY_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getPoPackageName()));
                            break;
                        case SERVICE_INTERFACE_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getServiceInterfacePackageName()));
                            break;
                        case SERVICE_IMPL_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getServiceImplPackageName()));
                            break;
                        case DAO_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getDaoPackageName()));
                            break;
                        case CONTROLLER_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getControllerPackageName()));
                            break;
                        case HATEOAS_RESOURCE_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getResourceClassPackageName()));
                            break;
                        case HATEOAS_RESOURCE_ASSEMBLER_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getResourceAssemblerPackageName()));
                            break;
                        case COMMON_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getCommonPackageName()));
                            break;
                        case EXCEPTION_PACKAGE_NAME:
                            field.set(converter, subPackage(converterConfig.getExceptionPackageName()));
                            break;
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("exception occurred during inject converters.", e);
            }
        }
    }

    /**
     * 对所需要使用的转换器进行逐个判断，没有创建成功的转换器则采用默认的转换器
     */
    private void initDefaultConverters() {
        if (this.nameConverter == null) {
            this.nameConverter = new KeywordFilterConverter();
        }

        if (this.typeConverter == null) {
            this.typeConverter = new DefaultTypeConverter();
        }

        if (this.fieldConverter == null) {
            this.fieldConverter = new DefaultFieldConverter();
        }

        if (this.instructionConverter == null) {
            this.instructionConverter = new DefaultServiceInstructionConverter();
        }

        if (this.controllerMethodConverter == null) {
            this.controllerMethodConverter = new DefaultControllerMethodConverter();
        }

        if (this.resourceAssemblerConverter == null) {
            this.resourceAssemblerConverter = new DefaultResourceAssemblerConverter();
        }

        if (this.daoConverter == null) {
            this.daoConverter = new DefaultDAOConverter();
        }

        if (this.serviceConverter == null) {
            this.serviceConverter = new DefaultServiceConverter();
        }

        if (this.controllerConverter == null) {
            this.controllerConverter = new DefaultControllerConverter();
        }

        if (this.entityClassConverter == null) {
            this.entityClassConverter = new DefaultEntityClassConverter();
        }

        if (this.resourceClassConverter == null) {
            this.resourceClassConverter = new DefaultResourceClassConverter();
        }

        injectAllConverter();
    }

    /**
     * 在当前配置的项目包中生成子包
     *
     * @param subPackage 子包名称
     * @return 包全名
     */
    private String subPackage(String subPackage) {
        if (this.basePackageName != null && this.basePackageName.trim().length() > 0) {
            return this.basePackageName + "." + subPackage;
        } else {
            return subPackage;
        }
    }

    /**
     * 获得这个项目的根包的包名
     *
     * @return 根包的包名
     */
    private String getRootPackageName() {
        if (this.basePackageName != null && this.basePackageName.trim().length() > 0) {
            return this.basePackageName;
        } else {
            return "";
        }
    }

    /**
     * 获得一个用于迭代包名的迭代器
     *
     * @return 一个用于迭代包名的迭代器
     */
    public Iterator<String> iteratePackageName() {
        return convertDataContext.iteratePackageName();
    }

    /**
     * 得到一个用于迭代已经生成的所有元信息的迭代器
     *
     * @return 用于迭代所有元信息的迭代器。如果生成的类当中没有类，则返回一个空的迭代器
     */
    public Iterator<ClassInfo> iterateClass() {
        List<ClassInfo> allClassInfoList = new ArrayList<>();

        allClassInfoList.addAll(this.commonClasses);
        allClassInfoList.addAll(this.convertDataContext.getAllClassInfo());

        return allClassInfoList.iterator();
    }

    /**
     * 往转换器当中添加一个实体信息
     *
     * @param entityInfo 要添加到转换器当中的实体信息
     */
    public void addEntityInfo(EntityInfo entityInfo) {
        EntityInfo originalEntity = convertDataContext.saveEntityByName(entityInfo.getName(), entityInfo);
        if (originalEntity != null) {
            this.entityInfoList.remove(originalEntity);
        }

        this.entityInfoList.add(entityInfo);
    }

    /**
     * 往转换器当中添加一个关联关系的信息
     *
     * @param relationship 要添加到转换器当中关联关系的信息
     */
    public void addEntityRelationship(EntityRelationship relationship) {
        this.entityRelationshipList.add(relationship);
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
                    .setPackageName(subPackage(this.converterConfig.getServiceInterfacePackageName()))
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

    /**
     * 创建 Spring Boot 的主类
     */
    private void createSpringBootRootApplication() {
        // 1. 创建类信息
        ClassInfo mainClass = new ClassInfo()
                .setPackageName(getRootPackageName())
                .setClassName("MainApplication");

        if (this.converterConfig.isCrossOriginEnabled()) {
            mainClass.extendClass(SpringMVCTypeFactory.webMvcConfigurerAdapter());
        }

        // 2. 创建相应的注解
        AnnotationInfo appAnnotation = SpringBootAnnotationFactory.appAnnotation();
        mainClass.addAnnotation(appAnnotation);

        AnnotationInfo enableJPARepositories = SpringDataAnnotationFactory.enableJPARepositories(subPackage(converterConfig.getDaoPackageName()));
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

            crosConfigMethod.setRootInstruction(InstructionFactory.sequence(
                    parentAddCrosMappingsInstruction,
                    InstructionFactory.emptyInstruction(),
                    configInstruction
            ));

            mainClass.addMethod(crosConfigMethod);
        }

        // 6. 将类保存到包当中
        this.convertDataContext.saveClassByPackageName(mainClass);
    }

    /**
     * 这个方法用于创建一些共有类
     */
    private void createCommonClasses() {
        // 1. PagedResource 类
        ClassInfo pagedResourceClass = CommonClassFactory.createPagedResource(subPackage(converterConfig.getCommonPackageName()));
        this.convertDataContext.saveCommonClass(ConverterContext.KEY_PAGED_RESOURCE, pagedResourceClass);

        // 2. CommonExceptionHandler 类
        ClassInfo commonExceptionHandlerClass = CommonClassFactory.commonExceptionHandler(subPackage(converterConfig.getControllerPackageName()), convertDataContext);
        this.convertDataContext.saveCommonClass(ConverterContext.KEY_COMMON_EXCEPTION_HANDLER, commonExceptionHandlerClass);
    }

    /**
     * 这个方法用于创建一些异常类
     */
    private void createExceptionClasses() {
        // 1. 数据库外键约束异常类
        ClassInfo dbConstraintException = CommonClassFactory.createSubTypeOfRuntimeException("DBConstraintException", subPackage(converterConfig.getExceptionPackageName()));

        this.convertDataContext.saveCommonClass(ConverterContext.KEY_DB_CONSTRAINT_EXCEPTION, dbConstraintException);
    }

    /**
     * 将 context 对象当中所有的对象转换成 classInfo 元信息
     */
    public void generate() {
        if (this.entityInfoList == null || this.entityInfoList.size() == 0) {
            logger.debug("generation was terminated because no entity was found in context");
            return;
        }

        // 创建主类
        createSpringBootRootApplication();

        // 创建 Swagger 的配置类
        createSwaggerConfigClass();

        // 创建异常类
        createExceptionClasses();

        // 先创建共有类
        createCommonClasses();

        // 生成实体类（PO 类）
        createEntityClasses();

        // 生成 Spring Data DAO 接口
        createDaoInterfaces();

        // 生成 Service 类
        createServiceClasses();

        // 创建 Controller 类
        createControllerClasses();

        // 创建 HATEOAS 相关类
        createHateoasResourceClasses();

        // 创建 HATEOAS 资源装配类
        createResourceAssembler();

        // 完善 Controller 类信息
        fillControllerClass();
    }

    /**
     * 创建 Swagger 的主配置类信息
     */
    private void createSwaggerConfigClass() {
        ClassInfo classInfo = new ClassInfo()
                .setPackageName(getRootPackageName())
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
}