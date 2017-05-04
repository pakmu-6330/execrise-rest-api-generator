package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.TypeInfo;

/**
 * 一个表示类表达式，用作静态调用时的引入值
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassStaticValueExpression extends AbstractExpression {

    private TypeInfo typeInfo;

    public ClassStaticValueExpression(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public ClassStaticValueExpression setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        super.fillImportOperations(context);

        context.addImportOperation(typeInfo, ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);
    }
}
