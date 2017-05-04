package cn.dyr.rest.generator.converter.clazz;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.field.IFieldConverter;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.framework.jpa.JPAAnnotationFactory;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.util.StringUtils;

import java.util.List;

import static cn.dyr.rest.generator.converter.ConvertDataContext.TYPE_ENTITY_CLASS;

/**
 * 实体类转换器的默认实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultEntityClassConverter implements IEntityClassConverter {

    @DataInject(DataInjectType.ENTITY_PACKAGE_NAME)
    private String poPackageName;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @ConverterInject(ConverterInjectType.FIELD)
    private IFieldConverter fieldConverter;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    /**
     * 将一个实体当中不存在关联关系的属性转换为字段
     *
     * @param entityInfo 实体信息
     */
    private void convertNonRelationshipAttribute(EntityInfo entityInfo, ClassInfo classInfo) {
        int idCount = 0;

        FieldInfo idFieldInfo = null;

        List<AttributeInfo> attributes = entityInfo.getAttributes();
        for (AttributeInfo attributeInfo : attributes) {
            FieldInfo fieldInfo = this.fieldConverter.fromAttributeInfo(attributeInfo);

            // 如果是主键而且类型为数字，则添加自增长相关的注解
            if (attributeInfo.isPrimaryIdentifier()) {
                idCount++;
                idFieldInfo = fieldInfo;
            }

            classInfo.addField(fieldInfo);

            // 将属性与字段进行关联
            convertDataContext.putAttribute(classInfo, fieldInfo, attributeInfo);
        }

        // 处理主键相关的逻辑
        // 当没有主键时，自动添加一个名为 id 的唯一标识符
        // 如果只有一个主键，而且主键为数字时，则自动添加一个自增长的注解
        // 如果有多个主键，则创建为联合主键
        if (idCount == 1) {
            idFieldInfo.addAnnotation(JPAAnnotationFactory.createIdentityGeneratedValued());
        } else if (idCount == 0) {
            idFieldInfo = new FieldInfo();
            idFieldInfo.setName("id");
            idFieldInfo.setType(TypeInfoFactory.longType());

            idFieldInfo.addAnnotation(JPAAnnotationFactory.idAnnotation());
            idFieldInfo.addAnnotation(JPAAnnotationFactory.createIdentityGeneratedValued());

            // 将字段标记为自动生成的字段
            convertDataContext.setAutoCreated(classInfo, idFieldInfo);
        } else {
            classInfo.addAnnotation(JPAAnnotationFactory.createIdClassAnnotation(classInfo.getType()));
        }
    }

    @Override
    public ClassInfo basicInfo(EntityInfo entityInfo) {
        // 1.1. 生成实体类本身的信息
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(this.nameConverter.classNameFromEntityName(entityInfo.getName()));
        classInfo.setPackageName(poPackageName);

        // 1.2. 生成相应的注解信息
        // 1.2.1. @Table 注解
        String entityInfoTableName = entityInfo.getTableName();
        String tableName = this.nameConverter.tableNameFromEntityName(entityInfo.getName());

        AnnotationInfo tableAnnotation = JPAAnnotationFactory.tableAnnotation(
                (StringUtils.isStringEmpty(entityInfoTableName) ? tableName : entityInfoTableName));
        classInfo.addAnnotation(tableAnnotation);

        // 1.2.2. @Entity 注解
        AnnotationInfo entityAnnotation = JPAAnnotationFactory.entityAnnotation();
        classInfo.addAnnotation(entityAnnotation);

        // 1.3 处理不涉及关联关系的属性转换
        convertNonRelationshipAttribute(entityInfo, classInfo);

        return classInfo;
    }

    @Override
    public void processRelationship(EntityRelationship relationship) {
        // 1. 判断关联关系的方向（单向还是双向），在类中添加相应的字段
        // 1.1. 处理 A->B 方向的关联关系
        if (relationship.hasAToB()) {
            FieldInfo endAField = this.fieldConverter.fromRelationship(relationship, true);
            if (endAField != null) {
                ClassInfo endAClassInfo = this.convertDataContext.getClassByEntityAndType(
                        relationship.getEndA().getName(), TYPE_ENTITY_CLASS);
                if (endAClassInfo == null) {
                    throw new IllegalArgumentException(
                            String.format("failed to found class info for entity %s", relationship.getEndA().getName()));
                }

                endAClassInfo.addField(endAField);

                // 将这个字段与关联关系之间建立联系
                convertDataContext.putRelationship(endAClassInfo, endAField, relationship, true);
            }
        }

        // 1.2. 处理 B->A 方向的关联关系
        if (relationship.hasBToA()) {
            FieldInfo endBField = this.fieldConverter.fromRelationship(relationship, false);
            if (endBField != null) {
                ClassInfo endBClassInfo = this.convertDataContext.getClassByEntityAndType(relationship.getEndB().getName(), TYPE_ENTITY_CLASS);
                if (endBClassInfo == null) {
                    throw new IllegalArgumentException(
                            String.format("failed to found class info for entity %s", relationship.getEndB().getName()));
                }

                endBClassInfo.addField(endBField);

                // 将这个字段与关联关系建立联系
                convertDataContext.putRelationship(endBClassInfo, endBField, relationship, false);
            }
        }
    }
}
