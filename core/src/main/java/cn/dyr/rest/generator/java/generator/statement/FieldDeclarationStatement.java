package cn.dyr.rest.generator.java.generator.statement;

import cn.dyr.rest.generator.java.generator.IndentManager;
import cn.dyr.rest.generator.java.generator.elements.AnnotationToken;
import cn.dyr.rest.generator.java.generator.elements.ElementsConstant;
import cn.dyr.rest.generator.java.generator.elements.ModifierInfoToken;
import cn.dyr.rest.generator.java.generator.elements.TypeInfoToken;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.ModifierInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用于表示一个字段的声明语句
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class FieldDeclarationStatement implements IStatement {

    private static AtomicLong counter;

    static {
        counter = new AtomicLong();
    }

    private ModifierInfo modifierInfo;
    private String name;
    private TypeInfo typeInfo;

    private FieldInfo fieldInfo;

    private List<AnnotationToken> annotationTokens;

    public FieldDeclarationStatement(FieldInfo fieldInfo) {
        this.name = fieldInfo.getName();
        this.typeInfo = fieldInfo.getType();
        this.modifierInfo = fieldInfo.getModifier();

        this.annotationTokens = new ArrayList<>();

        this.fieldInfo = fieldInfo;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public FieldDeclarationStatement setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
        return this;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public FieldDeclarationStatement setFieldInfo(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        IndentManager indentManager = IndentManager.get();

        List<AnnotationToken> annotationTokens = new ArrayList<>();

        // 处理字段级别上的注解
        Iterator<AnnotationInfo> annotations = this.fieldInfo.iterateAnnotations();
        while (annotations.hasNext()) {
            AnnotationInfo annotationInfo = annotations.next();

            AnnotationToken token = new AnnotationToken(annotationInfo);
            annotationTokens.add(token);
        }

        if (annotationTokens.size() > 0) {
            builder.append(ElementsConstant.LINE_SEPARATOR);
            for (AnnotationToken token : annotationTokens) {
                builder.append(indentManager.getIndentString());
                builder.append(token.toString());
                builder.append(ElementsConstant.LINE_SEPARATOR);
            }
        }

        // 生成字段本身的信息
        String typeExpr = new TypeInfoToken(this.fieldInfo.getType()).toString();

        ModifierInfoToken modifierInfoToken = new ModifierInfoToken(this.fieldInfo.getModifier());

        builder.append(String.format("%s%s %s %s;",
                indentManager.getIndentString(),
                modifierInfoToken.generateFieldModifierString(),
                typeExpr, this.name));
        return builder.toString();
    }
}
