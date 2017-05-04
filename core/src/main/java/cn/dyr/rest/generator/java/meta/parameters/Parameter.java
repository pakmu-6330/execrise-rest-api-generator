package cn.dyr.rest.generator.java.meta.parameters;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.AnnotationInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用于表示一个参数，这个参数可以出现在函数体以及函数的形参之中
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class Parameter implements IImportProcessor {

    private static final AtomicLong counter;

    static {
        counter = new AtomicLong();
    }

    private TypeInfo typeInfo;
    private String name;
    private List<AnnotationInfo> annotationInfoList;

    public Parameter() {
        this.typeInfo = TypeInfoFactory.objectType();
        this.name = String.format("parameter_%d", counter.incrementAndGet());
        this.annotationInfoList = new ArrayList<>();
    }

    /**
     * 获得参数的类型
     *
     * @return 这个参数的类型
     */
    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    /**
     * 设置这个参数的参数类型
     *
     * @param typeInfo 参数类型
     * @return 这个参数信息本身
     */
    public Parameter setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
        return this;
    }

    /**
     * 获得参数名称
     *
     * @return 这个参数的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置这个参数的名称
     *
     * @param name 参数的名称
     * @return 这个参数值本身
     */
    public Parameter setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 获得这个参数上面的注解信息
     *
     * @return 这个参数上面的注解信息
     */
    public List<AnnotationInfo> getAnnotationInfoList() {
        return annotationInfoList;
    }

    /**
     * 为这个注解设置一个注解列表
     *
     * @param annotationInfoList 要设置到参数的注解的列表
     * @return 这个参数信息本身
     */
    public Parameter setAnnotationInfoList(List<AnnotationInfo> annotationInfoList) {
        this.annotationInfoList = annotationInfoList;
        return this;
    }

    /**
     * 向这个参数添加一个注解对象
     *
     * @param annotationInfo 要添加到这个参数之上的注解信息
     * @return 这个参数信息本身
     */
    public Parameter addAnnotationInfo(AnnotationInfo annotationInfo) {
        this.annotationInfoList.add(annotationInfo);
        return this;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 添加参数本身的类型信息
        context.addImportOperation(this.typeInfo, ImportedOperation.TARGET_TYPE_METHOD_PARAMETER_TYPE);

        // 处理参数级别上的注解信息
        if (this.annotationInfoList != null && this.annotationInfoList.size() > 0) {
            for (AnnotationInfo annotationInfo : annotationInfoList) {
                annotationInfo.fillImportOperations(context);
            }
        }
    }
}
