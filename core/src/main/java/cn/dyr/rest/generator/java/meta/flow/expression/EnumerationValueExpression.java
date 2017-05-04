package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.flow.expression.AbstractExpression;

/**
 * 这个表示一个枚举成员的表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EnumerationValueExpression extends AbstractExpression {

    private TypeInfo enumerationTypeInfo;
    private String member;

    public EnumerationValueExpression(TypeInfo enumerationTypeInfo, String member) {
        this.enumerationTypeInfo = enumerationTypeInfo;
        this.member = member;
    }

    public TypeInfo getEnumerationTypeInfo() {
        return enumerationTypeInfo;
    }

    public EnumerationValueExpression setEnumerationTypeInfo(TypeInfo enumerationTypeInfo) {
        this.enumerationTypeInfo = enumerationTypeInfo;
        return this;
    }

    public String getMember() {
        return member;
    }

    public EnumerationValueExpression setMember(String member) {
        this.member = member;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        if (this.enumerationTypeInfo != null) {
            context.addImportOperation(this.enumerationTypeInfo, ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);
        }
    }
}
