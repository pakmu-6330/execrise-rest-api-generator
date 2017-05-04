package cn.dyr.rest.generator.java.meta;

import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 表示类当中的一个字段
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class FieldInfo implements IImportProcessor {

    private static final AtomicLong counter;

    static {
        counter = new AtomicLong();
    }

    private ModifierInfo modifier;
    private TypeInfo type;
    private String name;

    private List<AnnotationInfo> annotationInfoList;

    public FieldInfo() {
        this.modifier = new ModifierInfo();

        this.modifier.setPrivate();
        this.type = TypeInfoFactory.objectType();
        this.name = String.format("field-%d", counter.incrementAndGet());
        this.annotationInfoList = new ArrayList<>();
    }

    /**
     * 设置这个字段的修饰符对象
     *
     * @param modifier 要设置的修饰符对象
     * @return 这个字段本身
     */
    public FieldInfo setModifier(ModifierInfo modifier) {
        this.modifier = modifier;
        return this;
    }

    /**
     * 将这个字段的访问修饰符设置为 public
     *
     * @return 这个字段本身
     */
    public FieldInfo setPublic() {
        modifier.setPublic();
        return this;
    }

    /**
     * 将这个字段的访问修饰符设置为 private
     *
     * @return 这个字段本身
     */
    public FieldInfo setPrivate() {
        modifier.setPrivate();
        return this;
    }

    /**
     * 将这个字段的访问修饰符设置为 protect
     *
     * @return 这个字段本身
     */
    public FieldInfo setProtected() {
        modifier.setProtected();
        return this;
    }

    /**
     * 将这个字段的访问修饰符设置为缺省
     *
     * @return 这个字段本身
     */
    public FieldInfo setDefault() {
        modifier.setDefault();
        return this;
    }

    /**
     * 设置这个字段是否为 static
     *
     * @param isStatic 这个字段是否为 static
     * @return 这个字段本身
     */
    public FieldInfo setStatic(boolean isStatic) {
        modifier.setStatic(isStatic);
        return this;
    }

    /**
     * 设置这个字段是否为 final
     *
     * @param isFinal 这个字段是否为 final
     * @return 这个字段本身
     */
    public FieldInfo setFinal(boolean isFinal) {
        modifier.setFinal(isFinal);
        return this;
    }

    /**
     * 获得这个字段的类型
     *
     * @return 这个字段的数据类型
     */
    public TypeInfo getType() {
        return type;
    }

    /**
     * 设置这个字段的类型
     *
     * @param type 设置这个字段的数据类型
     * @return 这个字段本身
     */
    public FieldInfo setType(TypeInfo type) {
        this.type = type;
        return this;
    }

    /**
     * 获得字段的名称
     *
     * @return 该字段的名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置这个字段的名称
     *
     * @param name 要设置的字段的名称
     * @return 这个字段本身
     */
    public FieldInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 获得这个字段的修饰符
     *
     * @return 这个字段的修饰符
     */
    public ModifierInfo getModifier() {
        return modifier;
    }

    /**
     * 为这个字段添加一个注解信息
     *
     * @param annotationInfo 要添加到字段上面的注解信息
     * @return 这个字段信息本身
     */
    public FieldInfo addAnnotation(AnnotationInfo annotationInfo) {
        this.annotationInfoList.add(annotationInfo);
        return this;
    }

    /**
     * 迭代这个字段级别的的注解信息
     *
     * @return 用于迭代这个字段级别上注解的迭代器
     */
    public Iterator<AnnotationInfo> iterateAnnotations() {
        return this.annotationInfoList.iterator();
    }

    /**
     * 判断这个字段是否被特定类型的注解修饰
     *
     * @param type 表示注解的类型
     * @return 表示这个字段是否被特定类型的注解修饰
     */
    public boolean isAnnotatedByType(TypeInfo type) {
        for (AnnotationInfo annotationInfo : this.annotationInfoList) {
            TypeInfo annotationInfoType = annotationInfo.getType();

            if (annotationInfoType.getFullName().equals(type.getFullName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 导入字段的类型信息
        context.addImportOperation(this.type, ImportedOperation.TARGET_TYPE_CLASS_INTERFACE);

        // 导入注解上的字段信息
        for (AnnotationInfo annotationInfo : this.annotationInfoList) {
            annotationInfo.fillImportOperations(context);
        }
    }
}
