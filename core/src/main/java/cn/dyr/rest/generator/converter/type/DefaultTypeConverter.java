package cn.dyr.rest.generator.converter.type;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.AttributeType;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * 类型转换逻辑
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DefaultTypeConverter implements ITypeConverter {

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext dataContext;

    @Override
    public TypeInfo convertTypeInfo(AttributeInfo attributeInfo) {
        AttributeType type = attributeInfo.getType();

        switch (type) {
            case BYTE:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_BYTE);
            case SHORT:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_SHORT);
            case INT:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_INT);
            case LONG:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG);
            case FLOAT:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_FLOAT);
            case DOUBLE:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_DOUBLE);
            case FIXED_STRING:
            case VAR_STRING:
                return TypeInfoFactory.stringType();
            case BOOLEAN:
                return TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_BOOLEAN);
            case DATETIME:
                return TypeInfoFactory.fromClass("java.util.Date");
        }

        throw new IllegalArgumentException(String.format("unsupported attributeInfo: %s", type.toString()));
    }

    @Override
    public TypeInfo fromEntity(EntityInfo entityInfo) {
        return dataContext.findTypeInfoByEntityName(entityInfo.getName());
    }

}
