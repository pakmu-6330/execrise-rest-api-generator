package cn.dyr.rest.generator.java.meta.factory;

import cn.dyr.rest.generator.java.meta.TypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类型工厂，用于快速创建一个类型对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class TypeInfoFactory {

    private static Map<String, String> PRIMITIVES_TO_WRAPPER;
    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(TypeInfoFactory.class);

        PRIMITIVES_TO_WRAPPER = new HashMap<>();
        PRIMITIVES_TO_WRAPPER.put("byte", "java.lang.Byte");
        PRIMITIVES_TO_WRAPPER.put("short", "java.lang.Short");
        PRIMITIVES_TO_WRAPPER.put("int", "java.lang.Integer");
        PRIMITIVES_TO_WRAPPER.put("long", "java.lang.Long");
        PRIMITIVES_TO_WRAPPER.put("float", "java.lang.Float");
        PRIMITIVES_TO_WRAPPER.put("double", "java.lang.Double");
        PRIMITIVES_TO_WRAPPER.put("boolean", "java.lang.Boolean");
        PRIMITIVES_TO_WRAPPER.put("char", "java.lang.Character");
    }

    /**
     * 创建一个 long 的基本数据类型
     *
     * @return 数据类型
     */
    public static TypeInfo longType() {
        return primitive(TypeInfoFactory.PRIMITIVE_LONG);
    }

    /**
     * 创建一个 int 的基本数据类型
     *
     * @return 数据类型
     */
    public static TypeInfo intType() {
        return primitive(TypeInfoFactory.PRIMITIVE_INT);
    }

    /**
     * 创建一个 boolean 的基本数据类型
     *
     * @return 数据类型
     */
    public static TypeInfo booleanType() {
        return primitive(TypeInfoFactory.PRIMITIVE_BOOLEAN);
    }

    public static class ReferenceType implements TypeInfo {

        private String name;
        private String packageName;
        private List<TypeInfo> parameterizeType;

        public ReferenceType(String name, String packageName) {
            this.name = name;
            this.packageName = packageName;
            this.parameterizeType = new ArrayList<>();
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getFullName() {
            if (packageName == null || "".equals(packageName.trim())) {
                return name;
            } else {
                return String.format("%s.%s", packageName, name);
            }
        }

        @Override
        public String getPackageName() {
            return packageName;
        }

        @Override
        public boolean isPlaceHolder() {
            return false;
        }

        @Override
        public boolean isPrimitiveType() {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public List<TypeInfo> getParameterizeType() {
            return parameterizeType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ReferenceType that = (ReferenceType) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (packageName != null ? !packageName.equals(that.packageName) : that.packageName != null) return false;
            return parameterizeType != null ? parameterizeType.equals(that.parameterizeType) : that.parameterizeType == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
            result = 31 * result + (parameterizeType != null ? parameterizeType.hashCode() : 0);
            return result;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            ReferenceType referenceType = new ReferenceType(name, packageName);
            List<TypeInfo> parameterizeType = new ArrayList<>();

            if (this.parameterizeType != null && this.parameterizeType.size() > 0) {
                for (TypeInfo typeInfo : this.parameterizeType) {
                    TypeInfo newTypeInfo = TypeInfoFactory.cloneTypeInfo(typeInfo);
                    parameterizeType.add(newTypeInfo);
                }

                referenceType.parameterizeType = parameterizeType;
            }

            return referenceType;
        }
    }

    public static class ReferenceArrayType implements TypeInfo {

        private ReferenceType referenceType;

        public ReferenceArrayType(TypeInfo typeInfo) {
            if (typeInfo == null) {
                throw new NullPointerException("base type cannot be null");
            }

            if (!(typeInfo instanceof ReferenceType)) {
                throw new IllegalArgumentException("base type must be reference type");
            }

            this.referenceType = (ReferenceType) typeInfo;
        }

        @Override
        public String getName() {
            return referenceType.getName();
        }

        @Override
        public String getFullName() {
            return referenceType.getFullName();
        }

        @Override
        public String getPackageName() {
            return referenceType.getPackageName();
        }

        @Override
        public boolean isPlaceHolder() {
            return false;
        }

        @Override
        public boolean isPrimitiveType() {
            return false;
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public List<TypeInfo> getParameterizeType() {
            return referenceType.getParameterizeType();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ReferenceArrayType that = (ReferenceArrayType) o;

            return referenceType != null ? referenceType.equals(that.referenceType) : that.referenceType == null;
        }

        @Override
        public int hashCode() {
            return referenceType != null ? referenceType.hashCode() : 0;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new ReferenceArrayType((TypeInfo) referenceType.clone());
        }
    }

    public static class VoidType implements TypeInfo {

        @Override
        public String getName() {
            return "void";
        }

        @Override
        public String getFullName() {
            return null;
        }

        @Override
        public String getPackageName() {
            return null;
        }

        @Override
        public boolean isPlaceHolder() {
            return false;
        }

        @Override
        public boolean isPrimitiveType() {
            return true;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public List<TypeInfo> getParameterizeType() {
            return null;
        }

        @Override
        public String toString() {
            return "void";
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException("void type cannot be clone");
        }
    }

    public static class PrimitiveType implements TypeInfo {

        private String name;

        public PrimitiveType(String name) {
            this.name = name;
        }

        private void setName(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getFullName() {
            return name;
        }

        @Override
        public String getPackageName() {
            return null;
        }

        @Override
        public boolean isPlaceHolder() {
            return false;
        }

        @Override
        public boolean isPrimitiveType() {
            return true;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<TypeInfo> getParameterizeType() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PrimitiveType that = (PrimitiveType) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new PrimitiveType(name);
        }
    }

    public static class PrimitiveArrayType implements TypeInfo {

        private PrimitiveType primitiveTypeInfo;

        public PrimitiveArrayType(TypeInfo typeInfo) {
            if (typeInfo == null) {
                throw new NullPointerException("base type cannot be null");
            }

            if (!(typeInfo instanceof PrimitiveType)) {
                throw new IllegalArgumentException("base type must be primitive type");
            }

            this.primitiveTypeInfo = (PrimitiveType) typeInfo;
        }

        @Override
        public String getName() {
            return this.primitiveTypeInfo.getName();
        }

        @Override
        public String getFullName() {
            return this.primitiveTypeInfo.getFullName();
        }

        @Override
        public String getPackageName() {
            return this.primitiveTypeInfo.getPackageName();
        }

        @Override
        public boolean isPlaceHolder() {
            return false;
        }

        @Override
        public boolean isPrimitiveType() {
            return true;
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public List<TypeInfo> getParameterizeType() {
            return this.primitiveTypeInfo.getParameterizeType();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PrimitiveArrayType that = (PrimitiveArrayType) o;

            return primitiveTypeInfo != null ? primitiveTypeInfo.equals(that.primitiveTypeInfo) : that.primitiveTypeInfo == null;
        }

        @Override
        public int hashCode() {
            return primitiveTypeInfo != null ? primitiveTypeInfo.hashCode() : 0;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return new PrimitiveArrayType((TypeInfo) primitiveTypeInfo.clone());
        }
    }

    /**
     * 泛型类泛型占位符
     */
    private static class GenericTypePlaceHolder implements TypeInfo {

        private String name;

        public GenericTypePlaceHolder(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getFullName() {
            return this.name;
        }

        @Override
        public String getPackageName() {
            return "";
        }

        @Override
        public boolean isPlaceHolder() {
            return true;
        }

        @Override
        public boolean isPrimitiveType() {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public List<TypeInfo> getParameterizeType() {
            return Collections.emptyList();
        }
    }

    public static final int PRIMITIVE_BYTE = 1;
    public static final int PRIMITIVE_SHORT = 2;
    public static final int PRIMITIVE_INT = 3;
    public static final int PRIMITIVE_LONG = 4;

    public static final int PRIMITIVE_FLOAT = 5;
    public static final int PRIMITIVE_DOUBLE = 6;

    public static final int PRIMITIVE_BOOLEAN = 7;
    public static final int PRIMITIVE_CHAR = 8;

    public static final int SPECIAL_VOID = 9;
    private static final TypeInfo TYPE_INFO_VOID = new VoidType();

    public static final String CLASS_NAME_OBJECT = "java.lang.Object";
    public static final String CLASS_NAME_STRING = "java.lang.String";

    private static final TypeInfo TYPE_INFO_OBJECT = fromClass(CLASS_NAME_OBJECT);

    public static TypeInfo objectType() {
        return TYPE_INFO_OBJECT;
    }

    public static TypeInfo stringType() {
        return fromClass(CLASS_NAME_STRING);
    }

    public static TypeInfo voidType() {
        return TYPE_INFO_VOID;
    }

    public static TypeInfo fromClass(String className) {
        String[] fields = className.split("\\.");
        String name = fields[fields.length - 1];
        String packageName = className.substring(0, className.length() - name.length() - 1);

        return new ReferenceType(name, packageName);
    }

    public static TypeInfo referenceArray(String className) {
        return new ReferenceArrayType(fromClass(className));
    }

    /**
     * 创建一个带泛型的类型信息
     *
     * @param raw               原始的类型信息，即该类不含有泛型信息的类型对象
     * @param parameterizeTypes 泛型类
     * @return 带有泛型类的类型信息
     */
    public static TypeInfo wrapGenerics(TypeInfo raw, TypeInfo[] parameterizeTypes) {
        if (raw instanceof PrimitiveType) {
            throw new IllegalArgumentException("primitive cannot be wrapped with generics");
        } else if (raw instanceof ReferenceType) {
            ReferenceType referenceType = new ReferenceType(raw.getName(), raw.getPackageName());

            List<TypeInfo> parameterizeType = referenceType.getParameterizeType();
            if (parameterizeType.size() > 0) {
                parameterizeType.clear();
            }

            Collections.addAll(parameterizeType, parameterizeTypes);

            return referenceType;
        } else {
            throw new IllegalArgumentException("generics is not supported by input type");
        }
    }

    /**
     * 创建一个带泛型的类型信息
     *
     * @param raw               原始的类型信息，即该类不包含有泛型信息的类型对象
     * @param parameterizeTypes 泛型类型
     * @return 带有泛型类的类型信息
     */
    public static TypeInfo wrapGenerics(TypeInfo raw, TypeInfo parameterizeTypes) {
        return wrapGenerics(raw, new TypeInfo[]{parameterizeTypes});
    }

    /**
     * 根据基本的数据类型产生相应的包装类
     *
     * @param type 基本数据类型
     * @return 包装类
     */
    public static TypeInfo primitiveWrapper(int type) {
        switch (type) {
            case PRIMITIVE_BYTE:
                return fromClass("java.lang.Byte");
            case PRIMITIVE_SHORT:
                return fromClass("java.lang.Short");
            case PRIMITIVE_INT:
                return fromClass("java.lang.Integer");
            case PRIMITIVE_LONG:
                return fromClass("java.lang.Long");
            case PRIMITIVE_FLOAT:
                return fromClass("java.lang.Float");
            case PRIMITIVE_DOUBLE:
                return fromClass("java.lang.Double");
            case PRIMITIVE_BOOLEAN:
                return fromClass("java.lang.Boolean");
            case PRIMITIVE_CHAR:
                return fromClass("java.lang.Character");
            default:
                throw new IllegalArgumentException("type must be primitive");
        }
    }

    /**
     * 将基本数据类型包装成相应的包装类或者相应的数组类型
     *
     * @param type 基本数据类型
     * @return 包装类类型
     */
    public static TypeInfo primitiveWrapper(TypeInfo type) {
        if (type.isPrimitiveType()) {
            String wrapperName = PRIMITIVES_TO_WRAPPER.get(type.getName());
            if (wrapperName == null) {
                throw new IllegalArgumentException("unexpected primitive type: " + type.getName());
            }

            if (type.isArray()) {
                return new ReferenceArrayType(fromClass(wrapperName));
            } else {
                return fromClass(wrapperName);
            }
        } else {
            // 如果不是基本类型，直接返回
            return type;
        }
    }

    /**
     * 产生基本类型的基本变量类型
     *
     * @param type 基本数据类型
     * @return 这个基本数据类型的 TypeInfo 对象
     */
    public static TypeInfo primitive(int type) {
        switch (type) {
            case PRIMITIVE_BYTE:
                return new PrimitiveType("byte");
            case PRIMITIVE_SHORT:
                return new PrimitiveType("short");
            case PRIMITIVE_INT:
                return new PrimitiveType("int");
            case PRIMITIVE_LONG:
                return new PrimitiveType("long");
            case PRIMITIVE_FLOAT:
                return new PrimitiveType("float");
            case PRIMITIVE_DOUBLE:
                return new PrimitiveType("double");
            case PRIMITIVE_BOOLEAN:
                return new PrimitiveType("boolean");
            case PRIMITIVE_CHAR:
                return new PrimitiveType("char");
            default:
                throw new IllegalArgumentException("type must be primitive");
        }
    }

    public static TypeInfo primitiveArray(int type) {
        return new PrimitiveArrayType(primitive(type));
    }

    /**
     * 对现有的类型数据对象进行深拷贝
     *
     * @param typeInfo 要进行深拷贝的类型对象
     * @return 这个对象的深拷贝
     */
    public static TypeInfo cloneTypeInfo(TypeInfo typeInfo) {
        if (typeInfo instanceof VoidType) {
            return TYPE_INFO_VOID;
        }

        // 利用反射，调用 clone 方法获得深拷贝
        TypeInfo clone = null;
        try {
            Class<? extends TypeInfo> typeInfoClass = typeInfo.getClass();
            Method cloneMethod = typeInfoClass.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);

            clone = (TypeInfo) cloneMethod.invoke(typeInfo);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("sth wrong during reflecting", e);
        }

        return clone;
    }

    /**
     * 创建一个泛型类型的占位符，用于泛型类的定义
     *
     * @param name 占位符的名称
     * @return 表示这个占位符的类型
     */
    public static TypeInfo placeHolder(String name) {
        return new GenericTypePlaceHolder(name);
    }
}
