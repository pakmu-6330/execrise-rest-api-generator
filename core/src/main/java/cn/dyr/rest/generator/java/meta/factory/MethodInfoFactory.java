package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;
import cn.dyr.rest.generator.util.StringUtils;

/**
 * 用于创建一些常见方法的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodInfoFactory {

    /**
     * 创建一个用作程序主入口的 main 方法
     *
     * @return main 方法对应的对象
     */
    public static MethodInfo createMainMethod() {
        Parameter argsParameter = new Parameter()
                .setName("args")
                .setTypeInfo(TypeInfoFactory.referenceArray("java.lang.String"));

        return new MethodInfo()
                .setName("main")
                .setPublic()
                .setStatic()
                .addParameter(argsParameter);
    }

    /**
     * 创建字段的 get 方法
     *
     * @param fieldInfo 要创建 get 方法的字段
     * @return 这个字段对应的 get 方法
     */
    public static MethodInfo getter(FieldInfo fieldInfo) {
        String fieldName = fieldInfo.getName();
        TypeInfo type = fieldInfo.getType();
        boolean isBoolean = (type.isPrimitiveType() && type.getName().equals("boolean") ||
                (!type.isPrimitiveType() && type.getName().equals("java.lang.Boolean")));

        String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String prefix = isBoolean ? "is" : "get";

        return new MethodInfo()
                .setName(prefix + name)
                .setReturnValueType(type)
                .setRootInstruction(
                        InstructionFactory.returnInstruction(
                                ValueExpressionFactory.thisReference().accessField(fieldName))
                );
    }

    /**
     * 创建字段的 set 方法
     *
     * @param fieldInfo 要创建 set 方法的字段
     * @return set 方法对象
     */
    public static MethodInfo setter(FieldInfo fieldInfo) {
        String fieldName = fieldInfo.getName();
        String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        TypeInfo type = fieldInfo.getType();

        String valueVariable = StringUtils.lowerFirstLatter(name);

        return new MethodInfo()
                .setName("set" + name)
                .setReturnValueType(TypeInfoFactory.voidType())
                .addParameter(
                        ParameterFactory.create(type, valueVariable)
                )
                .setRootInstruction(
                        InstructionFactory.assignment(
                                ValueExpressionFactory.thisReference().accessField(fieldName),
                                ValueExpressionFactory.variable(valueVariable)
                        )
                );
    }

    /**
     * 创建 builder 形式的 set 方法
     *
     * @param fieldInfo 要创建 set 方法的字段
     * @param thisType  类的类型
     * @return set 方法对象
     */
    public static MethodInfo builderSetter(FieldInfo fieldInfo, TypeInfo thisType) {
        String fieldName = fieldInfo.getName();
        String name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        TypeInfo fieldType = fieldInfo.getType();

        String valueVariable = StringUtils.lowerFirstLatter(name);

        return new MethodInfo()
                .setName("set" + name)
                .setReturnValueType(thisType)
                .addParameter(ParameterFactory.create(fieldType, valueVariable))
                .setRootInstruction(
                        InstructionFactory.sequence(
                                InstructionFactory.assignment(
                                        ValueExpressionFactory.thisReference().accessField(fieldName),
                                        ValueExpressionFactory.variable(valueVariable)
                                ),
                                InstructionFactory.returnInstruction(
                                        ValueExpressionFactory.thisReference()
                                )
                        )
                );
    }
}
