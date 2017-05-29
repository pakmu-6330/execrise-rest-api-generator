package cn.dyr.rest.generator.java.meta;

import cn.dyr.rest.generator.exception.DuplicatedAnnotationException;
import cn.dyr.rest.generator.exception.DuplicatedNameException;
import cn.dyr.rest.generator.java.generator.analysis.IImportProcessor;
import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import cn.dyr.rest.generator.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static cn.dyr.rest.generator.util.TypeInfoAssertUtils.assertNotArrayType;
import static cn.dyr.rest.generator.util.TypeInfoAssertUtils.assertNotPrimitiveType;

/**
 * 表示一个类的信息
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ClassInfo implements IImportProcessor {

    private static final AtomicLong counter;
    private static Logger logger;

    static {
        counter = new AtomicLong();
        logger = LoggerFactory.getLogger(ClassInfo.class);
    }

    private ModifierInfo modifier;
    private TypeInfo parent;
    private Set<TypeInfo> interfaces;
    private String packageName;
    private String className;

    private Map<String, FieldInfo> fields;
    private Map<String, AnnotationInfo> annotations;
    private List<MethodInfo> methods;

    private List<ConstructorMethodInfo> constructors;
    private List<TypeInfo> genericPlaceHolders;

    public ClassInfo() {
        this.modifier = new ModifierInfo();

        this.fields = new HashMap<>();
        this.annotations = new HashMap<>();
        this.methods = new ArrayList<>();
        this.constructors = new ArrayList<>();

        this.genericPlaceHolders = new ArrayList<>();

        this.parent = TypeInfoFactory.objectType();
        this.interfaces = new LinkedHashSet<>();

        this.modifier.setPublic();
        this.packageName = "";
        this.className = String.format("Class_%d", counter.incrementAndGet());
    }

    /**
     * 设置类全名
     *
     * @param classFullName 类全名
     * @return 这个类信息本身
     */
    public ClassInfo fullName(String classFullName) {
        String[] fields = classFullName.split("\\.");
        this.className = fields[fields.length - 1];
        this.packageName = classFullName.substring(0, classFullName.length() - this.className.length() - 1);

        return this;
    }

    /**
     * 获得这个类的的修饰符信息
     *
     * @return 这个类的修饰符信息
     */
    public ModifierInfo getModifier() {
        return modifier;
    }

    /**
     * 获得这个类所属包名
     *
     * @return 这个类所属包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置这个类所属包名
     *
     * @param packageName 这个类所属包名
     * @return 这个类信息本身
     */
    public ClassInfo setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * 获得这个类的类名
     *
     * @return 这个类的类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置这个类的类名
     *
     * @param className 这个类的类名
     * @return 这个类信息的本身
     */
    public ClassInfo setClassName(String className) {
        this.className = className;
        return this;
    }

    /**
     * 将这个类的访问修饰符设置为 public
     *
     * @return 这个类信息本身
     */
    public ClassInfo setPublic() {
        this.modifier.setPublic();
        return this;
    }

    /**
     * 将这个类的访问修饰符设置为 default
     *
     * @return 这个类信息本身
     */
    public ClassInfo setDefault() {
        this.modifier.setDefault();
        return this;
    }

    /**
     * 设置这个类是否为抽象类
     *
     * @param isAbstract 表示这个类是否为抽象类
     * @return 这个类信息本身
     */
    public ClassInfo setAbstract(boolean isAbstract) {
        this.modifier.setAbstract(isAbstract);
        return this;
    }

    /**
     * 设置这个类是否为 final 类
     *
     * @param isFinal 表示这个类是否会被 final 修饰
     * @return 这个类信息本身
     */
    public ClassInfo setFinal(boolean isFinal) {
        this.modifier.setFinal(isFinal);
        return this;
    }

    /**
     * 设置这个类是否为接口
     *
     * @param isInterface 表示这个类是否为接口的布尔值
     * @return 这个类信息本身
     */
    public ClassInfo setInterface(boolean isInterface) {
        this.modifier.setInterface(isInterface);
        return this;
    }

    /**
     * 获得表示当前类类型的 TypeInfo 对象
     *
     * @return 这个类对应类型的 TypeInfo 对象
     */
    public TypeInfo getType() {
        return TypeInfoFactory.fromClass(this.getFullName());
    }

    /**
     * 设置这个类的父类
     *
     * @param parentClass 要设置的父类
     * @return 这个类信息本身
     */
    public ClassInfo extendClass(TypeInfo parentClass) {
        if (parentClass == null) {
            parentClass = TypeInfoFactory.objectType();
        }

        this.parent = parentClass;
        return this;
    }

    /**
     * 如果这个类还没有实现该接口，对这个接口进行实现
     *
     * @param interfaceInfo 要实现的接口类型
     * @return 这个类信息本身
     */
    public ClassInfo implementInterface(TypeInfo interfaceInfo) {
        if (this.interfaces.contains(interfaceInfo)) {
            return this;
        }

        this.interfaces.add(interfaceInfo);
        return this;
    }

    /**
     * 获得这个类的基类类型
     *
     * @return 这个类的基类类型
     */
    public TypeInfo getParent() {
        return this.parent;
    }

    /**
     * 获得这个类实现的接口
     *
     * @return 这个类实现的接口的列表
     */
    public Set<TypeInfo> getInterfaces() {
        return this.interfaces;
    }

    /**
     * 用于判断这个类是否处于默认包当中
     *
     * @return 如果这个类处于默认包当中，则返回 true；否则返回 false
     */
    public boolean isDefaultPackage() {
        return (this.packageName == null || "".equals(this.packageName.trim()));
    }

    /**
     * 获得这个类的泛型占位符列表
     *
     * @return 这个类泛型占位符的列表
     */
    public List<TypeInfo> getGenericPlaceHolders() {
        return genericPlaceHolders;
    }

    /**
     * 设置这个类的泛型占位符列表
     *
     * @param genericPlaceHolders 这个类的泛型占位符列表
     * @return 这个类本身
     */
    public ClassInfo setGenericPlaceHolders(List<TypeInfo> genericPlaceHolders) {
        this.genericPlaceHolders = genericPlaceHolders;
        return this;
    }

    /**
     * 给这个类添加一个泛型类型的占位符
     *
     * @param typeInfo 要添加到类当中的泛型占位符
     * @return 这个类本身
     */
    public ClassInfo addGenericPlaceHolder(TypeInfo typeInfo) {
        if (!typeInfo.isPlaceHolder()) {
            return this;
        }

        if (this.genericPlaceHolders == null) {
            this.genericPlaceHolders = new ArrayList<>();
        }

        this.genericPlaceHolders.add(typeInfo);
        return this;
    }

    /**
     * 获得指定位置上的泛型类型占位符对象
     *
     * @param index 位置信息
     * @return 特定位置上的占位符对象；如果位置信息无效，则返回 null
     */
    public TypeInfo getGenericPlaceHolder(int index) {
        if (index < 0 || index >= this.genericPlaceHolders.size()) {
            return null;
        }

        return this.genericPlaceHolders.get(index);
    }

    /**
     * 根据占位符名称获得占位符对象
     *
     * @param name 占位符名称
     * @return 找到的占位符对象；如果没有满足条件的占位符对象，则返回 null
     */
    public TypeInfo getGenericPlaceHolder(String name) {
        for (TypeInfo typeInfo : this.genericPlaceHolders) {
            if (typeInfo.isPlaceHolder() && typeInfo.getName().equals(name)) {
                return typeInfo;
            }
        }

        return null;
    }

    /**
     * 为这个类添加一个字段
     *
     * @param field 要添加到类当中的字段
     * @return 这个类对象本身
     */
    public ClassInfo addField(FieldInfo field) {
        FieldInfo existField = this.fields.get(field.getName());
        if (existField != null) {
            throw new DuplicatedNameException(field.getName());
        }

        this.fields.put(field.getName(), field);
        return this;
    }

    /**
     * 为这个类添加一个类级别的注解
     *
     * @param annotationInfo 要添加到类的注解
     * @return 这个类对象本身
     */
    public ClassInfo addAnnotation(AnnotationInfo annotationInfo) {
        String fullName = annotationInfo.getType().getFullName();

        AnnotationInfo existsAnnotation = this.annotations.get(fullName);
        if (existsAnnotation != null) {
            throw new DuplicatedAnnotationException(this.getFullName(), fullName);
        }

        this.annotations.put(fullName, annotationInfo);
        return this;
    }

    /**
     * 往这个类当中添加一个方法
     *
     * @param methodInfo 要添加到类当中的方法
     * @return 这个类对象本身
     */
    public ClassInfo addMethod(MethodInfo methodInfo) {
        this.methods.add(methodInfo);
        return this;
    }

    /**
     * 获得一个用于迭代类中所有字段的一个迭代器
     *
     * @return 用于迭代类中所有字段的迭代器
     */
    public Iterator<FieldInfo> iterateFields() {
        return this.fields.values().iterator();
    }

    /**
     * 获得一个用于迭代这个类级别之上的注解
     *
     * @return 用于迭代类中所有类级别上的注解
     */
    public Iterator<AnnotationInfo> iterateAnnotations() {
        return this.annotations.values().iterator();
    }

    /**
     * 获得一个用于迭代类当中方法信息的迭代器
     *
     * @return 用于迭代类当中方法信息的迭代器
     */
    public Iterator<MethodInfo> iterateMethods() {
        return this.methods.iterator();
    }

    /**
     * 获得一个用于迭代类构造方法的迭代器
     *
     * @return 用于迭代类当中构造方法信息的迭代器
     */
    public Iterator<ConstructorMethodInfo> iterateConstructorMethods() {
        return this.constructors.iterator();
    }

    /**
     * 获得这个类的类全名
     *
     * @return 这个类的类全名
     */
    public String getFullName() {
        if (this.packageName == null || "".equals(this.packageName.trim())) {
            return this.className;
        } else {
            return String.format("%s.%s", this.packageName, this.className);
        }
    }

    /**
     * 创建一个这个类无参的构造方法
     *
     * @return 这个新创建的无参构造方法
     */
    public MethodInfo createConstructorMethod() {
        ConstructorMethodInfo constructor = new ConstructorMethodInfo(this.getType());

        this.constructors.add(constructor);

        return constructor;
    }

    @Override
    public void fillImportOperations(ImportContext context) {
        // 引入基类和接口的信息
        if (this.parent != null) {
            context.addImportOperation(this.parent, ImportedOperation.TARGET_TYPE_PARENT_INTERFACE);
        }

        if (this.interfaces != null && this.interfaces.size() > 0) {
            for (TypeInfo interfaceType : this.interfaces) {
                context.addImportOperation(interfaceType, ImportedOperation.TARGET_TYPE_PARENT_INTERFACE);
            }
        }

        // 引入类级别上的注解信息
        if (this.annotations != null && this.annotations.size() > 0) {
            for (AnnotationInfo annotationInfo : this.annotations.values()) {
                annotationInfo.fillImportOperations(context);
            }
        }

        // 引入字段的信息
        if (this.fields != null && this.fields.size() > 0) {
            for (FieldInfo fieldInfo : this.fields.values()) {
                fieldInfo.fillImportOperations(context);
            }
        }

        // 引入这个类当中方法的一些信息
        if (this.constructors != null && this.constructors.size() > 0) {
            for (MethodInfo constructor : this.constructors) {
                constructor.fillImportOperations(context);
            }
        }

        if (this.methods != null && this.methods.size() > 0) {
            for (MethodInfo method : this.methods) {
                method.fillImportOperations(context);
            }
        }
    }

    /**
     * 寻找当前类当中被特定类型注解修饰的所以字段信息
     *
     * @param annotationType 注解类型
     * @return 被特定注解类型修饰的字段的列表
     */
    public List<FieldInfo> findFieldsByAnnotationType(TypeInfo annotationType) {
        assertNotPrimitiveType(annotationType);
        assertNotArrayType(annotationType);

        List<FieldInfo> resultList = new ArrayList<>();

        for (FieldInfo fieldInfo : this.fields.values()) {
            Iterator<AnnotationInfo> annotationInfoIterator = fieldInfo.iterateAnnotations();
            while (annotationInfoIterator.hasNext()) {
                AnnotationInfo annotationInfo = annotationInfoIterator.next();

                if (annotationType.getFullName().equals(annotationInfo.getType().getFullName())) {
                    resultList.add(fieldInfo);
                }
            }
        }

        return resultList;
    }

    /**
     * 获得这个类当中特定字段的 get 方法
     *
     * @param fieldName 要获得 get 方法的字段
     * @return 这个字段对应的 get 方法
     */
    public MethodInfo getterMethod(String fieldName) {
        String getName = "get" + StringUtils.upperFirstLatter(fieldName);
        String isName = "is" + StringUtils.upperFirstLatter(fieldName);

        for (MethodInfo methodInfo : this.methods) {
            if (methodInfo.getName().equals(getName) || methodInfo.getName().equals(isName)) {
                return methodInfo;
            }
        }

        return null;
    }

    /**
     * 获得这个类当中特定字段的 set 方法
     *
     * @param fieldName 要获得 set 方法的字段
     * @return 这个字段对应的 set 方法
     */
    public MethodInfo setterMethod(String fieldName) {
        String setName = "set" + StringUtils.upperFirstLatter(fieldName);

        for (MethodInfo methodInfo : this.methods) {
            if (methodInfo.getName().equals(setName)) {
                return methodInfo;
            }
        }

        return null;
    }

    /**
     * 寻找类中指定方法名称的所有方法信息
     *
     * @param name 要查找的方法的名称
     * @return 含有这个名称的方法信息的集合；如果这个类中含有多个同名方法，那么这些方法都会储存在集合类里面；如果这个类中不存在指定名称的方法，则返回一个空列表而不是 null
     */
    public List<MethodInfo> findMethodByName(String name) {
        List<MethodInfo> list = new ArrayList<>();

        if (this.methods != null && this.methods.size() > 0) {
            for (MethodInfo methodInfo : this.methods) {
                if (methodInfo.getName().equals(name)) {
                    list.add(methodInfo);
                }
            }
        }

        return list;
    }

    /**
     * 根据相应的 get/set 方法名称获得相应的字段
     *
     * @param methodName 操作方法名称
     * @return 对应的字段信息
     */
    public FieldInfo findByPropertyMethodName(String methodName) {
        String fieldName = null;

        if (methodName.startsWith("get")) {
            fieldName = StringUtils.lowerFirstLatter(methodName.substring(3));
        } else if (methodName.startsWith("set")) {
            fieldName = StringUtils.lowerFirstLatter(methodName.substring(3));
        } else if (methodName.startsWith("is")) {
            fieldName = StringUtils.lowerFirstLatter(methodName.substring(2));
        }

        if (fieldName == null) {
            logger.warn("invalid method name: {}", methodName);
            return null;
        } else {
            return findFieldByName(fieldName);
        }
    }

    private FieldInfo findFieldByName(String name) {
        if (fields == null || fields.size() == 0) {
            return null;
        }

        return this.fields.get(name);
    }
}
