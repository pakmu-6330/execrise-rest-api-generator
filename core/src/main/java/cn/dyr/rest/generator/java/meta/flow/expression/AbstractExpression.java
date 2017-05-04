package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.meta.factory.ParameterValueFactory;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 这个类提供 Expression 对象的一些默认实现方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public abstract class AbstractExpression implements IValueExpression {

    @Override
    public IValueExpression invokeMethod(String name, ParameterValue[] parameters) {
        ChainValueExpression retValue = new ChainValueExpression();
        retValue.setParentValue(this)
                .setGenerateType(ChainValueExpression.GENERATE_TYPE_METHOD_INVOCATION)
                .setName(name)
                .setParameterValues(parameters);

        if (this instanceof ClassStaticValueExpression) {
            retValue.setGenerateType(ChainValueExpression.GENERATE_TYPE_STATIC_METHOD_INVOCATION);
        }

        return retValue;
    }

    @Override
    public IValueExpression invokeMethod(String name, Object[] parameters) {
        return this.invokeMethod(name, ParameterValueFactory.fromObjects(parameters));
    }

    @Override
    public IValueExpression invokeMethod(String name) {
        return this.invokeMethod(name, new Object[]{});
    }

    @Override
    public IValueExpression invokeMethod(String name, Object parameter) {
        return invokeMethod(name, new Object[]{parameter});
    }

    @Override
    public IValueExpression accessField(String name) {
        ChainValueExpression retValue = new ChainValueExpression();
        retValue.setParentValue(this)
                .setGenerateType(
                        (this instanceof ClassStaticValueExpression ?
                                ChainValueExpression.GENERATE_TYPE_STATIC_FIELD_ACCESS :
                                ChainValueExpression.GENERATE_TYPE_FIELD_ACCESS))
                .setName(name)
                .setParameterValues(null);

        return retValue;
    }

    @Override
    public void fillImportOperations(ImportContext context) {

    }

    @Override
    public boolean isSimpleExpression() {
        return true;
    }
}
