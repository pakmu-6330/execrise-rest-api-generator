package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.method.IControllerMethodConverter;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.framework.spring.SpringFrameworkAnnotationFactory;
import cn.dyr.rest.generator.framework.spring.mvc.SpringMVCAnnotationFactory;
import cn.dyr.rest.generator.framework.swagger.DocumentGeneratorUtils;
import cn.dyr.rest.generator.framework.swagger.SwaggerAnnotationFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ASSEMBLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_CONTROLLER_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_SERVICE_IMPL_CLASS;
import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_SERVICE_INTERFACE;

/**
 * Controller 类转换默认实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultControllerConverter implements IControllerConverter {

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig converterConfig;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @ConverterInject(ConverterInjectType.CONTROLLER_METHOD)
    private IControllerMethodConverter controllerMethodConverter;

    @DataInject(DataInjectType.RELATIONSHIP_LIST)
    private List<EntityRelationship> entityRelationshipList;

    @DataInject(DataInjectType.CONTROLLER_PACKAGE_NAME)
    private String controllerPackageName;

    /**
     * 获得与这个实体有关联关系的实体信息
     *
     * @param entityInfo 实体信息
     * @return 与这个实体有关联关系的实体信息
     */
    private List<EntityInfo> getRelatedEntityInfo(EntityInfo entityInfo) {
        Set<String> entityNames = new HashSet<>();
        List<EntityInfo> retValue = new ArrayList<>();

        // 寻找有关联关系的实体名称
        for (EntityRelationship relationship : this.entityRelationshipList) {
            String endAEntityName = relationship.getEndA().getName();
            String endBEntityName = relationship.getEndB().getName();

            if (endAEntityName.equals(endBEntityName)) {
                continue;
            }

            if (endAEntityName.equals(entityInfo.getName()) && relationship.hasAToB()) {
                entityNames.add(endBEntityName);
                continue;
            }

            if (endBEntityName.equals(entityInfo.getName()) && relationship.hasBToA()) {
                entityNames.add(endAEntityName);
            }
        }

        entityNames.remove(entityInfo.getName());

        // 根据实体名称取出所有的实体对象
        for (String entityName : entityNames) {
            EntityInfo entity = this.convertDataContext.getEntityByName(entityName);
            retValue.add(entity);
        }

        return retValue;
    }

    @Override
    public ClassInfo basicInfo(EntityInfo entityInfo) {
        // 1. 控制器类本身的信息
        String controllerClassName = this.nameConverter.controllerNameFromEntityName(entityInfo.getName());
        ClassInfo controllerClass = new ClassInfo()
                .setPackageName(controllerPackageName)
                .setClassName(controllerClassName)
                .addAnnotation(SwaggerAnnotationFactory.apiAnnotation(DocumentGeneratorUtils.getApiDocument(entityInfo.getDescription())));

        String uriRequest =
                converterConfig.getUriPrefix() + "/" + Inflector.getInstance().pluralize(StringUtils.lowerFirstLatter(entityInfo.getName()));
        AnnotationInfo restControllerAnnotation = SpringMVCAnnotationFactory.restController();
        AnnotationInfo requestMappingAnnotation = SpringMVCAnnotationFactory.requestMapping(uriRequest);
        controllerClass.addAnnotation(restControllerAnnotation).addAnnotation(requestMappingAnnotation);

        // 2. 控制器类中本实体的 Service 字段
        ClassInfo serviceClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_SERVICE_INTERFACE);
        FieldInfo serviceField = new FieldInfo()
                .setType(serviceClass.getType())
                .setName(this.convertDataContext.getServiceDefaultFieldName(entityInfo.getName()))
                .addAnnotation(SpringFrameworkAnnotationFactory.autowired());

        controllerClass.addField(serviceField);

        // 3. 本实体相关的 Service 字段
        generateRelatedServiceField(controllerClass, entityInfo);

        return controllerClass;
    }

    /**
     * 产生相关的 Service 接口字段
     *
     * @param classInfo  本 Service 类
     * @param entityInfo 这个 Service 类相关的实体信息
     */
    private void generateRelatedServiceField(ClassInfo classInfo, EntityInfo entityInfo) {
        Map<String, Object> repeatFreeMap = new HashMap<>();

        // 本实体维护的关系对方实体对应的 Service 类字段
        List<ConvertDataContext.RelationshipHandler> handlers = convertDataContext.findByHandler(entityInfo.getName());
        if (handlers != null && handlers.size() > 0) {
            for (ConvertDataContext.RelationshipHandler handler : handlers) {
                String handledEntityName = handler.getToBeHandled();

                importServiceField(classInfo, repeatFreeMap, handledEntityName);
            }
        }

        // 其他实体维护着本实体关联关系相应的 Service 类字段
        List<ConvertDataContext.RelationshipHandler> handled = convertDataContext.findByHandled(entityInfo.getName());
        if (handled != null && handled.size() > 0) {
            for (ConvertDataContext.RelationshipHandler handler : handled) {
                String handlerEntityName = handler.getHandler();

                importServiceField(classInfo, repeatFreeMap, handlerEntityName);
            }
        }
    }

    private void importServiceField(ClassInfo classInfo, Map<String, Object> repeatFreeMap, String entityName) {
        if (repeatFreeMap.get(entityName) == null) {
            ClassInfo serviceInterface = convertDataContext.getClassByEntityAndType(entityName, TYPE_SERVICE_INTERFACE);
            FieldInfo serviceField = new FieldInfo()
                    .setPrivate()
                    .setType(serviceInterface.getType())
                    .setName(convertDataContext.getServiceDefaultFieldName(entityName))
                    .addAnnotation(SpringFrameworkAnnotationFactory.autowired());
            classInfo.addField(serviceField);

            repeatFreeMap.put(entityName, new Object());
        }
    }

    @Override
    public ClassInfo convertMethod(EntityInfo entityInfo) {
        ClassInfo controllerClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_CONTROLLER_CLASS);

        // 1. 获得与这个实体相关的 Assembler 对象
        ClassInfo thisEntityAssembler = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ASSEMBLER_CLASS);
        FieldInfo thisEntityAssemblerField = new FieldInfo()
                .setType(thisEntityAssembler.getType())
                .setName(this.convertDataContext.getAssemblerDefaultFieldName(entityInfo.getName()))
                .addAnnotation(SpringFrameworkAnnotationFactory.autowired());
        controllerClass.addField(thisEntityAssemblerField);

        // 2. 获得与这个实体有关联关系的实体对应的 Assembler 对象
        List<EntityInfo> relatedEntityInfoList = this.getRelatedEntityInfo(entityInfo);
        for (EntityInfo relatedEntityInfo : relatedEntityInfoList) {
            ClassInfo assemblerClass = this.convertDataContext.getClassByEntityAndType(relatedEntityInfo.getName(), TYPE_ASSEMBLER_CLASS);
            FieldInfo assemblerField = new FieldInfo()
                    .setName(this.convertDataContext.getAssemblerDefaultFieldName(relatedEntityInfo.getName()))
                    .setType(assemblerClass.getType())
                    .addAnnotation(SpringFrameworkAnnotationFactory.autowired());

            controllerClass.addField(assemblerField);
        }

        // 3. 增加分页资源获取的方法
        MethodInfo pagedListMethod = this.controllerMethodConverter.getPagedList(entityInfo.getName(), converterConfig.getDefaultPageSize());
        controllerClass.addMethod(pagedListMethod);

        // 4. 增加按照编号获取的方法
        MethodInfo getByIdMethod = this.controllerMethodConverter.getById(entityInfo.getName());
        controllerClass.addMethod(getByIdMethod);

        // 5. 增加创建资源的方法
        MethodInfo createResourceMethod = this.controllerMethodConverter.newResource(entityInfo.getName());
        controllerClass.addMethod(createResourceMethod);

        // 6. 增加更新资源的方法
        MethodInfo updateResourceMethod = this.controllerMethodConverter.updateResource(entityInfo.getName());
        controllerClass.addMethod(updateResourceMethod);

        // 7. 增加删除资源方法
        MethodInfo delete = this.controllerMethodConverter.delete(entityInfo.getName());
        controllerClass.addMethod(delete);

        // 8. 添加关联实体的查询方法
        List<ConvertDataContext.RelationshipHandler> handlerList = this.convertDataContext.findByHandler(entityInfo.getName());
        List<ConvertDataContext.RelationshipHandler> handledList = this.convertDataContext.findByHandled(entityInfo.getName());
        for (ConvertDataContext.RelationshipHandler handler : handlerList) {
            MethodInfo relatedEntityGetMethod = this.controllerMethodConverter.getRelatedResourceById(entityInfo.getName(), handler);
            controllerClass.addMethod(relatedEntityGetMethod);

            MethodInfo toOneUpdateMethod = this.controllerMethodConverter.getRelatedToOneUpdateForHandler(entityInfo.getName(), handler);
            if (toOneUpdateMethod != null) {
                controllerClass.addMethod(toOneUpdateMethod);
            }
        }

        // 9. 添加对多关联实体的创建方法
        for (ConvertDataContext.RelationshipHandler handler : handlerList) {
            MethodInfo methodInfo = this.controllerMethodConverter.getRelatedResourcesCreateForHandler(entityInfo.getName(), handler);
            if (methodInfo != null) {
                controllerClass.addMethod(methodInfo);
            }
        }

        // 10. 遍历相关的方法
        for (ConvertDataContext.RelationshipHandler handler : handledList) {
            MethodInfo methodInfo = this.controllerMethodConverter.getRelatedManyToOneCreateForHandled(entityInfo.getName(), handler);
            if (methodInfo != null) {
                controllerClass.addMethod(methodInfo);
            }

            MethodInfo getMethod = this.controllerMethodConverter.getRelatedManyToOneGetForHandled(entityInfo.getName(), handler);
            if (getMethod != null) {
                controllerClass.addMethod(getMethod);
            }
        }

        return controllerClass;
    }
}
