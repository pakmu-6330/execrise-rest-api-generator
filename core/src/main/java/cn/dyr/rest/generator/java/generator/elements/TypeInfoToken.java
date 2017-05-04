package cn.dyr.rest.generator.java.generator.elements;

import cn.dyr.rest.generator.java.generator.ImportedTypeManager;
import cn.dyr.rest.generator.java.generator.statement.IStatement;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.util.GenericTypeStringBuilder;

import java.util.List;

/**
 * 用于表示一个类型的元素
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TypeInfoToken implements IToken {

    private TypeInfo typeInfo;

    public TypeInfoToken(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public TypeInfoToken setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
        return this;
    }

    @Override
    public String toString() {
        // 对于 void, 基本数据类型和占位符直接返回 name 即可
        if (this.typeInfo == TypeInfoFactory.voidType() ||
                this.typeInfo.isPrimitiveType() ||
                this.typeInfo.isPlaceHolder()) {
            return this.typeInfo.getName();
        }

        ImportedTypeManager typeManager = ImportedTypeManager.get();
        String header;

        if (typeManager.isTypeImported(typeInfo)) {
            header = typeInfo.getName();
        } else {
            header = typeInfo.getFullName();
        }

        List<TypeInfo> parameterizeType = typeInfo.getParameterizeType();
        if (parameterizeType == null || parameterizeType.size() == 0) {
            return header;
        }

        GenericTypeStringBuilder builder = new GenericTypeStringBuilder();
        for (TypeInfo type : parameterizeType) {
            builder.addTypeInfo(type);
        }

        return header + builder.toString();
    }
}
