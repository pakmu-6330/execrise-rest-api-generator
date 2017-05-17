package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.framework.jpa.JPATypeFactory;
import cn.dyr.rest.generator.framework.spring.hateoas.SpringHATEOASTypeFactory;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.factory.FieldInfoFactory;
import cn.dyr.rest.generator.util.ClassInfoUtils;

import java.util.Iterator;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;

/**
 * 资源类的默认实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultResourceClassConverter implements IResourceClassConverter {

    private static final boolean RESOURCE_RELATIONSHIP_FIELD = false;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    @DataInject(DataInjectType.HATEOAS_RESOURCE_PACKAGE_NAME)
    private String hateoasResourcePackageName;

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig converterConfig;

    @Override
    public ClassInfo fromEntity(EntityInfo entityInfo) {
        ClassInfo entityClass = this.convertDataContext.getClassByEntityAndType(entityInfo.getName(), TYPE_ENTITY_CLASS);
        String resourceClassName = this.nameConverter.hateoasResourceNameFromEntityName(entityInfo.getName());

        // 资源类
        ClassInfo resourceClassInfo = new ClassInfo()
                .setClassName(resourceClassName)
                .setPackageName(hateoasResourcePackageName)
                .extendClass(SpringHATEOASTypeFactory.resourceSupportType());

        // 对类当中逐个的字段进行循环
        Iterator<FieldInfo> fieldInfoIterator = entityClass.iterateFields();
        while (fieldInfoIterator.hasNext()) {
            FieldInfo fieldInfo = fieldInfoIterator.next();

            if (!fieldInfo.isAnnotatedByType(JPATypeFactory.idAnnotationType())) {
                AttributeInfo attribute = convertDataContext.getAttribute(entityClass, fieldInfo);
                EntityRelationship relationship = convertDataContext.getRelationship(entityClass, fieldInfo);
                FieldInfo resourceClassField = FieldInfoFactory.cloneFieldWithoutAnnotation(fieldInfo);

                // 如果是属性，而且直接直接是对外暴露的，克隆一个不带注解的字段信息添加到段类当中
                if (attribute != null && attribute.isExpose()) {
                    resourceClassInfo.addField(resourceClassField);
                }

                // 如果是关联关系创建的字段，而且这个关系对外暴露
                if (relationship != null && RESOURCE_RELATIONSHIP_FIELD) {
                    boolean isEndAField = convertDataContext.isEndAField(entityClass, fieldInfo);

                    // 如果这个类对应这个关联关系的 A 端，而且 A 端的关系对外暴露
                    if (isEndAField && relationship.isEndAExpose()) {
                        resourceClassInfo.addField(resourceClassField);
                    }

                    // 如果这个类对应这个关联关系的 B 端，而且 B 端的关系对外暴露
                    if (!isEndAField && relationship.isEndBExpose()) {
                        resourceClassInfo.addField(resourceClassField);
                    }
                }
            }
        }

        // 处理资源类的 get set 方法
        ClassInfoUtils.createBothGetterAndSetter(resourceClassInfo, converterConfig.isBuilderStyleSetter());

        return resourceClassInfo;
    }
}
