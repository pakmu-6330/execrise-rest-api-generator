package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.generator.elements.TypeInfoToken;
import cn.dyr.rest.generator.java.meta.TypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理泛型相关的字符串
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class GenericTypeStringBuilder {

    private List<TypeInfo> genericTypeList;

    public GenericTypeStringBuilder() {
        this.genericTypeList = new ArrayList<>();
    }

    public void addTypeInfo(TypeInfo typeInfo) {
        genericTypeList.add(typeInfo);
    }

    @Override
    public String toString() {
        if (genericTypeList == null || genericTypeList.size() == 0) {
            return "";
        }

        boolean first = true;
        StringBuilder builder = new StringBuilder();
        builder.append("<");

        for (TypeInfo type : this.genericTypeList) {
            TypeInfoToken token = new TypeInfoToken(type);

            if (first) {
                first = false;
            } else {
                builder.append(", ");
            }

            builder.append(token.toString());
        }

        builder.append(">");
        return builder.toString();
    }
}
