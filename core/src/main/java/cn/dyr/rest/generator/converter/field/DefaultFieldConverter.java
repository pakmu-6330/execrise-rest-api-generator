package cn.dyr.rest.generator.converter.field;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.converter.type.ITypeConverter;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.AttributeType;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.framework.jdk.CollectionsTypeFactory;
import cn.dyr.rest.generator.framework.jpa.JPAAnnotationFactory;
import cn.dyr.rest.generator.framework.jpa.JPARelationshipAnnotationBuilder;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.annotation.AnnotationParameterFactory;
import net.oschina.util.Inflector;

import static cn.dyr.rest.generator.entity.RelationshipType.MANY_TO_MANY;
import static cn.dyr.rest.generator.entity.RelationshipType.MANY_TO_ONE;
import static cn.dyr.rest.generator.entity.RelationshipType.ONE_TO_MANY;
import static cn.dyr.rest.generator.entity.RelationshipType.ONE_TO_ONE;

/**
 * 默认的字段转换器实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultFieldConverter implements IFieldConverter {

    @ConverterInject(ConverterInjectType.TYPE)
    private ITypeConverter typeConverter;

    @ConverterInject(ConverterInjectType.NAME)
    private INameConverter nameConverter;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext dataContext;

    /**
     * 检查关联关系对象是否有效
     *
     * @param relationship 要进行检查的关联关系对象
     * @throws IllegalArgumentException 如果进行检查的关联关系对象存在问题，则抛出这个异常
     */
    private void checkRelationship(EntityRelationship relationship) {
        EntityInfo endA = relationship.getEndA();
        EntityInfo endB = relationship.getEndB();

        // 1. 检查关联关系的方向是否已经设定
        if (!relationship.hasBToA() && !relationship.hasAToB()) {
            throw new IllegalArgumentException("direction of relationship must be specified.");
        }

        // 2. 检查关联关系两端是否已经指定实体
        if (endA == null || endB == null) {
            throw new IllegalArgumentException("both entity cannot be null");
        }

        // 3. 关联关系的维护方有效
        // 3.1. 是否已经指定了关联关系的维护方
        EntityInfo handler = relationship.getRelationHandler();
        if (handler == null) {
            throw new IllegalArgumentException("relationship handler is null");
        }

        // 3.2. 关联关系的维护方是否为实体两端的其中一个
        if (!handler.getName().equals(endA.getName()) &&
                !handler.getName().equals(endB.getName())) {
            throw new IllegalArgumentException("handler must be endA or endB");
        }
    }

    /**
     * 对关联关系进行预处理
     *
     * @param relationship 要进行预处理的关联关系对象
     */
    private void preHandleRelationship(EntityRelationship relationship) {
        // 1. 如果是单方向的关联关系配置，则将关系持有方设置为关系的维护方
        if (!relationship.isBidirectional()) {
            // 单向关联关系的处理原则
            // 如果只有 A 到 B 的关联关系，A 类当中持有 B 的集合类或者类实例，关联关系由 A 进行维护
            // 如果只有 B 到 A 的关联关系，B 类当中持有 A 的集合类或者类实例，关联关系由 B 进行维护

            if (relationship.hasAToB()) {
                relationship.setRelationHandler(relationship.getEndA());
            } else {
                relationship.setRelationHandler(relationship.getEndB());
            }
        }
    }

    /**
     * 根据实体之间的关联关系生成字段的名称
     *
     * @param relationship 实体之间的关联关系
     * @param forEndA      是否生成用于端A的字段名称
     * @return 字段名称
     */
    private String fieldNameFromRelationship(EntityRelationship relationship, boolean forEndA) {
        String attributeName = (forEndA ? relationship.getEndAAttributeName() : relationship.getEndBAttributeName());

        if (attributeName == null || attributeName.trim().equals("")) {
            attributeName = this.nameConverter.fieldNameFromAttributeName(
                    Inflector.getInstance().pluralize(
                            forEndA ? relationship.getEndB().getName() : relationship.getEndA().getName()
                    ));
        }

        return attributeName;
    }

    @Override
    public FieldInfo fromAttributeInfo(AttributeInfo attributeInfo) {
        FieldInfo retValue = new FieldInfo();
        retValue.setName(this.nameConverter.fieldNameFromAttributeName(attributeInfo.getName()));
        retValue.setType(this.typeConverter.convertTypeInfo(attributeInfo));

        if (attributeInfo.isPrimaryIdentifier()) {
            retValue.addAnnotation(JPAAnnotationFactory.idAnnotation());
        } else {
            AnnotationInfo columnAnnotation = JPAAnnotationFactory.columnAnnotation();
            AttributeType type = attributeInfo.getType();

            // 对于指定了长度的字符串进行特别处理
            if (type == AttributeType.FIXED_STRING ||
                    type == AttributeType.VAR_STRING) {
                int length = attributeInfo.getLength();

                if (length != 0) {
                    columnAnnotation.addParameter("length", AnnotationParameterFactory.intParameter(length));
                }
            }

            retValue.addAnnotation(columnAnnotation);
        }

        return retValue;
    }

    /**
     * 处理关联关系端 A 对应的字段
     *
     * @param relationship 关联关系对象
     * @return 用于端 A 的字段信息
     */
    private FieldInfo handleEndAField(EntityRelationship relationship) {
        EntityInfo endA = relationship.getEndA();
        EntityInfo endB = relationship.getEndB();

        boolean handleByEndA =
                (relationship.getRelationHandler().getName().equals(endA.getName()));
        boolean handleByEndB =
                (relationship.getRelationHandler().getName().equals(endB.getName()));

        // 1. 判断关联关系是否含有 A 端到 B 端的方向
        if (!relationship.hasAToB()) {
            return null;
        }

        // 2. 根据关联关系创建相应的类型
        RelationshipType type = relationship.getType();
        TypeInfo endBType = this.typeConverter.fromEntity(endB);
        FieldInfo retValue = new FieldInfo();

        switch (type) {
            case ONE_TO_ONE:
            case MANY_TO_ONE:
                retValue.setType(endBType);
                break;
            case ONE_TO_MANY:
            case MANY_TO_MANY:
                TypeInfo listTypeInfo = CollectionsTypeFactory.listWithGeneric(endBType);
                retValue.setType(listTypeInfo);
                break;
        }

        // 3. 根据特定规则生成字段名称
        String endAFieldName = fieldNameFromRelationship(relationship, true);
        retValue.setName(endAFieldName);

        // 4. 根据关系的类型创建相应的注解类型
        // 这里做的东西有两项
        //    1. 生成关联关系的注解
        //    2. 生成相应的维护方数据，mappedBy
        String endBFieldName = fieldNameFromRelationship(relationship, false);

        switch (type) {
            case ONE_TO_ONE: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.oneToOne();

                if (handleByEndB) {
                    builder.mappedBy(endBFieldName);

                    // B 端维护关系
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), ONE_TO_ONE,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null), ONE_TO_ONE,
                            relationship.isBidirectional());
                }

                retValue.addAnnotation(builder.build());
            }
            break;
            case ONE_TO_MANY: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.oneToMany();

                if (handleByEndB) {
                    builder.mappedBy(endBFieldName);
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), MANY_TO_ONE,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null), ONE_TO_MANY,
                            relationship.isBidirectional());
                }

                retValue.addAnnotation(builder.build());
            }
            break;
            case MANY_TO_MANY: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.manyToMany();

                if (handleByEndB) {
                    builder.mappedBy(endBFieldName);
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), MANY_TO_MANY,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null),
                            MANY_TO_MANY, relationship.isBidirectional());
                }

                retValue.addAnnotation(builder.build());
            }
            break;
            case MANY_TO_ONE: {
                AnnotationInfo manyToOne = JPARelationshipAnnotationBuilder.manyToOne().build();
                retValue.addAnnotation(manyToOne);

                if (handleByEndB) {
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), ONE_TO_MANY,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null),
                            MANY_TO_ONE, relationship.isBidirectional());
                }
            }
            break;
        }

        return retValue;
    }

    /**
     * 处理关联关系端 B 对应的字段
     *
     * @param relationship 关联关系对象
     * @return 用于端 B 的字段信息
     */
    private FieldInfo handleEndBField(EntityRelationship relationship) {
        EntityInfo endA = relationship.getEndA();
        EntityInfo endB = relationship.getEndB();

        boolean handleByEndA =
                (relationship.getRelationHandler().getName().equals(endA.getName()));
        boolean handleByEndB =
                (relationship.getRelationHandler().getName().equals(endB.getName()));

        // 1. 判断关联关系是否有 B 到 A 方向
        if (!relationship.hasBToA()) {
            return null;
        }

        // 2. 根据关联关系创建类型
        RelationshipType type = relationship.getType();
        TypeInfo typeInfo = this.typeConverter.fromEntity(relationship.getEndA());
        FieldInfo retValue = new FieldInfo();

        switch (type) {
            case MANY_TO_MANY:
            case MANY_TO_ONE:
                TypeInfo listType = CollectionsTypeFactory.listWithGeneric(typeInfo);
                retValue.setType(listType);
                break;
            case ONE_TO_MANY:
            case ONE_TO_ONE:
                retValue.setType(typeInfo);
                break;
        }

        // 3. 根据特定的规则生成字段的名称
        String endBFieldName = fieldNameFromRelationship(relationship, false);
        String endAFieldName = fieldNameFromRelationship(relationship, true);

        retValue.setName(endBFieldName);

        // 4. 根据关联关系创建注解信息
        switch (type) {
            case ONE_TO_ONE: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.oneToOne();

                if (handleByEndA) {
                    builder.mappedBy(endAFieldName);
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null), ONE_TO_ONE,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), ONE_TO_ONE,
                            relationship.isBidirectional());
                }

                retValue.addAnnotation(builder.build());
            }
            break;
            case ONE_TO_MANY: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.manyToOne();
                retValue.addAnnotation(builder.build());

                if (handleByEndA) {
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null), MANY_TO_ONE,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), ONE_TO_MANY,
                            relationship.isBidirectional());
                }
            }
            break;
            case MANY_TO_MANY: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.manyToMany();

                if (handleByEndA) {
                    builder.mappedBy(endAFieldName);
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null), MANY_TO_MANY,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), MANY_TO_MANY,
                            relationship.isBidirectional());
                }

                retValue.addAnnotation(builder.build());
            }
            break;
            case MANY_TO_ONE: {
                JPARelationshipAnnotationBuilder builder = JPARelationshipAnnotationBuilder.oneToMany();

                if (handleByEndA) {
                    builder.mappedBy(endAFieldName);
                    dataContext.saveRelationHandlerInfo(
                            endA.getName(), endB.getName(), endAFieldName,
                            (relationship.hasBToA() ? endBFieldName : null), ONE_TO_MANY,
                            relationship.isBidirectional());
                } else {
                    dataContext.saveRelationHandlerInfo(
                            endB.getName(), endA.getName(), endBFieldName,
                            (relationship.hasAToB() ? endAFieldName : null), MANY_TO_ONE,
                            relationship.isBidirectional());
                }

                retValue.addAnnotation(builder.build());
            }
            break;
        }

        return retValue;
    }

    @Override
    public FieldInfo fromRelationship(EntityRelationship relationship, boolean forEndA) {
        preHandleRelationship(relationship);
        checkRelationship(relationship);

        if (forEndA) {
            return handleEndAField(relationship);
        } else {
            return handleEndBField(relationship);
        }
    }
}
