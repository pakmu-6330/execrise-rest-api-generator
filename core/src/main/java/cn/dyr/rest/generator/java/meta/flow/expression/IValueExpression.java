package cn.dyr.rest.generator.java.meta.flow.expression;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.meta.parameters.value.ParameterValue;

/**
 * 表示一个值表达式
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IValueExpression extends IImportProcessor {

    /**
     * 如果这个值可以继续调用方法，则返回表示调用方法返回的值；如果当前这个值不支持方法的执行，则会抛出 InvocationNotSupportException 的异常
     *
     * @param name       要调用的方法的名称
     * @param parameters 调用方法时传进的实参列表
     * @return 表示方法返回值的值对象
     */
    IValueExpression invokeMethod(String name, ParameterValue[] parameters);

    /**
     * 如果这个值可以继续调用方法，则返回表示调用方法返回的值；如果当前这个值不支持方法的执行，则会抛出 InvocationNotSupportException 的异常
     *
     * @param name       要调用的方法的名称
     * @param parameters 调用方法时传进的实参列表
     * @return 表示方法返回值的值对象
     */
    IValueExpression invokeMethod(String name, Object[] parameters);

    /**
     * 如果这个值可以继续调用方法，则返回表示调用方法返回的值；如果当前这个值不支持方法的执行，则会抛出 InvocationNotSupportException 的异常
     *
     * @param name      要调用的方法的名称
     * @param parameter 调用方法时传进的实参
     * @return 表示方法返回值的值对象
     */
    IValueExpression invokeMethod(String name, Object parameter);

    /**
     * 如果这个值可以继续调用方法，则返回表示调用方法返回的值；如果当前这个值不支持方法的执行，则会抛出 InvocationNotSupportException
     *
     * @param name 要调用的方法的名称
     * @return 表示方法返回的值的对象
     */
    IValueExpression invokeMethod(String name);

    /**
     * 如果可以访问这个值当中的字段，则返回表示这个字段的值；否则抛出 InvocationNotSupported 异常
     *
     * @param name 要访问的字段的名称
     * @return 这个字段表示的值
     */
    IValueExpression accessField(String name);

    /**
     * 用于描述这个表达式是否为简单表达式的布尔值
     * <p>
     * 所谓简单表达式，就是这个表达式使用在嵌套场合下是否需要使用括号。如果不需要，则为简单表达式；否则即为非简单表达式
     *
     * @return 表示这个表达式是否为简单表达式的布尔值
     */
    boolean isSimpleExpression();
}
