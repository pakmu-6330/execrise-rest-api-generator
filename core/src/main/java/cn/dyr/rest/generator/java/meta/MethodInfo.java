package cn.dyr.rest.generator.java.meta;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 表示一个方法的信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class MethodInfo implements IImportProcessor {

    private static final AtomicLong counter;

    static {
        counter = new AtomicLong();
    }

    /**
     * 表示这个方法信息是一个普通的方法信息
     */
    public static final int METHOD_TYPE_COMMON = 1;

    /**
     * 表示这个方法信息是一个构造方法信息
     */
    public static final int METHOD_TYPE_CONSTRUCTOR = 2;

    /**
     * 表示这个是一个静态代码块
     */
    public static final int METHOD_TYPE_STATIC = 3;

    private ModifierInfo modifier;
    private TypeInfo returnValueType;
    private String name;
    private List<Parameter> parameters;
    private List<AnnotationInfo> annotationInfoList;

    private List<TypeInfo> throwable;

    private IInstruction rootInstruction;

    private boolean defineOnly;

    public MethodInfo() {
        this.defineOnly = false;

        this.modifier = new ModifierInfo().setPublic();
        this.returnValueType = TypeInfoFactory.voidType();
        this.parameters = new ArrayList<>();
        this.annotationInfoList = new ArrayList<>();
        this.name = String.format("method_%d", counter.incrementAndGet());

        this.throwable = new ArrayList<>();

        this.rootInstruction = null;
    }

    /**
     * 将这个方法的访问修饰符设置为 public
     *
     * @return 这个方法本身
     */
    public MethodInfo setPublic() {
        this.modifier.setPublic();
        return this;
    }

    /**
     * 将这个方法的访问修饰符设置为 private
     *
     * @return 这个方法本身
     */
    public MethodInfo setPrivate() {
        this.modifier.setPrivate();
        return this;
    }

    /**
     * 将这个方法的访问修饰符设置为 protected
     *
     * @return 这个方法本身
     */
    public MethodInfo setProtected() {
        this.modifier.setProtected();
        return this;
    }

    /**
     * 将这个方法的访问修饰符设置为缺省
     *
     * @return 这个方法本身
     */
    public MethodInfo setDefault() {
        this.modifier.setDefault();
        return this;
    }

    /**
     * 将这个方法设置为 static 方法
     *
     * @return 这个方法本身
     */
    public MethodInfo setStatic() {
        this.modifier.setStatic(true);
        return this;
    }

    /**
     * 取消这个方法的 static 标记
     *
     * @return 这个方法本身
     */
    public MethodInfo unsetStatic() {
        this.modifier.setStatic(false);
        return this;
    }

    public IInstruction getRootInstruction() {
        return rootInstruction;
    }

    public MethodInfo setRootInstruction(IInstruction rootInstruction) {
        this.rootInstruction = rootInstruction;
        return this;
    }

    public String getName() {
        return name;
    }

    public MethodInfo setName(String name) {
        this.name = name;
        return this;
    }

    public ModifierInfo getModifier() {
        return modifier;
    }

    public MethodInfo setModifier(ModifierInfo modifier) {
        this.modifier = modifier;
        return this;
    }

    public TypeInfo getReturnValueType() {
        return returnValueType;
    }

    public MethodInfo setReturnValueType(TypeInfo returnValueType) {
        this.returnValueType = returnValueType;
        return this;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public MethodInfo setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * 往这个方法当中增加一个参数
     *
     * @param parameter 要增加到方法当中的参数
     * @return 这个方法本身
     */
    public MethodInfo addParameter(Parameter parameter) {
        this.parameters.add(parameter);
        return this;
    }

    public List<AnnotationInfo> getAnnotationInfoList() {
        return annotationInfoList;
    }

    public MethodInfo setAnnotationInfoList(List<AnnotationInfo> annotationInfoList) {
        this.annotationInfoList = annotationInfoList;
        return this;
    }

    /**
     * 往这个方法之上添加注解信息
     *
     * @param annotationInfo 要添加到方法上面的注解信息
     * @return 这个方法信息本身
     */
    public MethodInfo addAnnotationInfo(AnnotationInfo annotationInfo) {
        this.annotationInfoList.add(annotationInfo);
        return this;
    }

    /**
     * 获得这个方法有可能抛出的异常
     *
     * @return 这个方法有可能抛出异常类型的列表
     */
    public List<TypeInfo> getThrowable() {
        return throwable;
    }

    /**
     * 设置这个方法有可能抛出的异常
     *
     * @param throwable 这个方法有可能抛出异常的列表
     * @return 这个方法本身
     */
    public MethodInfo setThrowable(List<TypeInfo> throwable) {
        this.throwable = throwable;
        return this;
    }

    /**
     * 给这个设置增加一个可能抛出的异常信息
     *
     * @param typeInfo 要增加到方法当中的异常信息
     * @return 这个方法本身
     */
    public MethodInfo addThrowable(TypeInfo typeInfo) {
        if (this.throwable == null) {
            this.throwable = new ArrayList<>();
        }

        this.throwable.add(typeInfo);

        return this;
    }

    /**
     * 获得这个方法是否仅为方法的定义
     *
     * @return 表示这个方法是否仅仅为方法定义的布尔值
     */
    public boolean isDefineOnly() {
        return defineOnly;
    }

    /**
     * 设置这个方法是否仅仅为方法的定义
     *
     * @param defineOnly 这个方法是否仅仅是一个方法的定义
     * @return 这个方法本身
     */
    public MethodInfo setDefineOnly(boolean defineOnly) {
        this.defineOnly = defineOnly;
        return this;
    }

    /**
     * 获得一个用于迭代方法当中注解信息的迭代器
     *
     * @return 一个用于迭代方法当中注解信息的迭代器
     */
    public Iterator<AnnotationInfo> iterateAnnotations() {
        return this.annotationInfoList.iterator();
    }

    /**
     * 获得这个方法的方法类型
     *
     * @return 表示这个方法方法类型的一个整数
     */
    public int getMethodType() {
        return METHOD_TYPE_COMMON;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 添加返回值的类型信息
        context.addImportOperation(this.returnValueType, ImportedOperation.TARGET_TYPE_RETURN_VALUE);

        // 添加可能抛出异常类的类型信息
        if (this.throwable != null && this.throwable.size() > 0) {
            for (TypeInfo typeInfo : this.throwable) {
                context.addImportOperation(typeInfo, ImportedOperation.TARGET_TYPE_RETURN_VALUE);
            }
        }

        // 添加参数的类型信息
        if (this.parameters != null && this.parameters.size() > 0) {
            for (Parameter parameter : parameters) {
                parameter.fillImportOperations(context);
            }
        }

        // 添加注解相关的 import 类型
        if (this.annotationInfoList != null && this.annotationInfoList.size() > 0) {
            for (AnnotationInfo annotationInfo : this.annotationInfoList) {
                annotationInfo.fillImportOperations(context);
            }
        }

        // 处理主逻辑的 import 类型
        if (this.rootInstruction != null) {
            this.rootInstruction.fillImportOperations(context);
        }
    }
}
