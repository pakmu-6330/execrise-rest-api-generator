package cn.dyr.rest.generator.converter;

import cn.dyr.rest.generator.entity.AttributeInfo;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 这个对象是用于保存字段的产生原因，属性或者关联关系与字段之间关系的类对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConvertDataContext implements ITypeContext {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(ConvertDataContext.class);
    }

    /**
     * 表示实体类的类型
     */
    public static final String TYPE_ENTITY_CLASS = "entity";

    /**
     * 表示 DAO 接口的类型
     */
    public static final String TYPE_DAO_INTERFACE = "dao";

    /**
     * 表示 Service 类的类型
     */
    public static final String TYPE_SERVICE_CLASS = "service";

    /**
     * 表示 Controller 类的类型
     */
    public static final String TYPE_CONTROLLER_CLASS = "controller";

    /**
     * 表示 HATEOAS 资源类的类型
     */
    public static final String TYPE_RESOURCE_CLASS = "resource";

    /**
     * 表示 HATEOAS 资源装配类的类型
     */
    public static final String TYPE_ASSEMBLER_CLASS = "assembler";

    /**
     * 这个 Map 用于保存类当中字段产生的来源对象
     */
    private Map<FieldMetaKey, FieldMetaValue> valueMap;

    /**
     * 由实体产生的类都放在这个 Map 当中。第一层的 key 是实体名称，第二层的 key 是类的类型
     */
    private Map<String, Map<String, ClassInfo>> classesMap;

    /**
     * 按包进行组织的类信息，Map 对象的 key 是包名，value 是这个包下面拥有的所有类的列表
     */
    private Map<String, List<ClassInfo>> classesListByPackage;

    /**
     * 这个 Map 保存实体名称到实体对象之间的关联关系
     */
    private Map<String, EntityInfo> entityInfoMapByName;

    /**
     * 这个 Map 是根据类全名保存实体信息的关联关系
     */
    private Map<String, EntityInfo> entityInfoFullClassName;

    /**
     * 这个 Map 保存实体之间关系信息的容器
     */
    private Map<RelationshipHandler, Object> handlerMetaInfoMap;

    /**
     * 这个 Map 保存 DAO 默认实例名称
     */
    private Map<String, String> defaultDAOFieldNameByEntity;

    /**
     * 这个 Map 保存 Service 默认实例名称
     */
    private Map<String, String> defaultServiceFieldNameByEntity;

    /**
     * 这个 Map 保存 Assembler 默认实例名称
     */
    private Map<String, String> defaultAssemblerFieldNameByEntity;

    /**
     * 这个 Map 保存一些通用的类
     */
    private Map<String, ClassInfo> commonsClassInfo;

    /**
     * 项目生成过程中保存项目基类
     */
    private String basePackage;

    public ConvertDataContext(String basePackage) {
        this.valueMap = new HashMap<>();
        this.classesMap = new HashMap<>();
        this.classesListByPackage = new HashMap<>();
        this.entityInfoMapByName = new HashMap<>();
        this.handlerMetaInfoMap = new HashMap<>();
        this.entityInfoFullClassName = new HashMap<>();

        this.defaultDAOFieldNameByEntity = new HashMap<>();
        this.defaultServiceFieldNameByEntity = new HashMap<>();
        this.defaultAssemblerFieldNameByEntity = new HashMap<>();

        this.commonsClassInfo = new HashMap<>();

        this.basePackage = basePackage;
    }

    /**
     * 保存非标准类型的类信息，通过 key 对其进行获取
     *
     * @param key       这个类对应的 key 值
     * @param classInfo 类元信息
     */
    public void saveCommonClass(String key, ClassInfo classInfo) {
        this.commonsClassInfo.put(key, classInfo);
        this.saveClassByPackageName(classInfo);
    }

    /**
     * 获得非标准类型的类信息，通过 key 对其进行获取
     *
     * @param key 这个类对应的 key 值
     * @return 如果这个 key 对应的类元信息存在，则返回对应的类元信息；否则返回这个类元信息
     */
    public ClassInfo getCommonClass(String key) {
        return this.commonsClassInfo.get(key);
    }

    /**
     * 保存一个实体相应的 DAO 的默认字段名称
     *
     * @param entityName   实体名称
     * @param daoFieldName 对应 DAO 的默认实例名称
     */
    public void saveDAODefaultFieldName(String entityName, String daoFieldName) {
        this.defaultDAOFieldNameByEntity.put(entityName, daoFieldName);
    }

    /**
     * 保存一个实体相应的 Service 的默认字段名称
     *
     * @param entityName       实体名称
     * @param serviceFieldName 对应 Service 的默认实例名称
     */
    public void saveServiceDefaultFieldName(String entityName, String serviceFieldName) {
        this.defaultServiceFieldNameByEntity.put(entityName, serviceFieldName);
    }

    /**
     * 保存一个实体相应的 Assembler 的默认字段名称
     *
     * @param entityName         实体名称
     * @param assemblerFieldName 对应 Assembler 的默认实例名称
     */
    public void saveAssemblerDefaultFieldName(String entityName, String assemblerFieldName) {
        this.defaultAssemblerFieldNameByEntity.put(entityName, assemblerFieldName);
    }

    /**
     * 获得用作一个实体相应的 DAO 的默认名称
     *
     * @param entityName 实体名称
     * @return 对应 DAO 的默认名称
     */
    public String getDAODefaultFieldName(String entityName) {
        return this.defaultDAOFieldNameByEntity.get(entityName);
    }

    /**
     * 获得实体相应的 Service 类的默认实例名称
     *
     * @param entityName 实体名称
     * @return 对应 Service 的默认名称
     */
    public String getServiceDefaultFieldName(String entityName) {
        return this.defaultServiceFieldNameByEntity.get(entityName);
    }

    /**
     * 获得实体相应的 Assembler 类的默认实例名称
     *
     * @param entityName 实体名称
     * @return 对应 Assembler 的默认名称
     */
    public String getAssemblerDefaultFieldName(String entityName) {
        return this.defaultAssemblerFieldNameByEntity.get(entityName);
    }

    /**
     * 根据关联关系的主控方获得关联关系的数据
     *
     * @param handler 关联关系的主控方
     * @return 以这个实体作为主控方的所有关联关系数据
     */
    public List<RelationshipHandler> findByHandler(String handler) {
        List<RelationshipHandler> retValue = new ArrayList<>();

        Set<RelationshipHandler> relationshipHandlers = this.handlerMetaInfoMap.keySet();
        for (RelationshipHandler handlerObject : relationshipHandlers) {
            if (handlerObject.getHandler().equals(handler)) {
                retValue.add(new RORelationshipHandler(handlerObject));
            }
        }

        return retValue;
    }

    /**
     * 根据关联关系的被控方获得关联关系的数据
     *
     * @param handled 关联关系的被控方
     * @return 以这个实体作为被控方得所有关联关系数据
     */
    public List<RelationshipHandler> findByHandled(String handled) {
        List<RelationshipHandler> retValue = new ArrayList<>();

        Set<RelationshipHandler> relationshipHandlers = this.handlerMetaInfoMap.keySet();
        for (RelationshipHandler handlerObject : relationshipHandlers) {
            if (handlerObject.getToBeHandled().equals(handled)) {
                retValue.add(new RORelationshipHandler(handlerObject));
            }
        }

        return retValue;
    }

    /**
     * 保存一个关联关系的控制数据
     *
     * @param handler          关联关系的主控方
     * @param toBeHandled      关联关系的被控方
     * @param handlerFieldName 关联关系在主控方对象对应的字段名称
     * @param handledFieldName 如果关联关系是双向，则这个字段储存关联关系被控方对象对应主控方对象中字段名称；如果关联关系为单项，则值为 true
     * @param type             这个枚举类型用于表示关联关系对于主控方而言的关联关系
     */
    public void saveRelationHandlerInfo(String handler, String toBeHandled,
                                        String handlerFieldName, String handledFieldName,
                                        RelationshipType type) {
        RelationshipHandler relationshipHandler = new RelationshipHandler()
                .setHandler(handler)
                .setToBeHandled(toBeHandled)
                .setHandlerFieldName(handlerFieldName)
                .setHandledFieldName(handledFieldName)
                .setType(type);

        if (this.handlerMetaInfoMap.putIfAbsent(relationshipHandler, new Object()) == null) {
            logger.debug("relation meta ({} -{}-> {}) saved", handler, handlerFieldName, toBeHandled);
        } else {
            logger.debug("relation meta ({} -{}-> {}) skipped", handler, handlerFieldName, toBeHandled);
        }
    }

    /**
     * 获得一个用于迭代包名的迭代器
     *
     * @return 用于迭代包名的迭代器
     */
    public Iterator<String> iteratePackageName() {
        return this.classesListByPackage.keySet().iterator();
    }

    /**
     * 获得一个用于迭代实体类元信息的迭代器
     *
     * @return 用于迭代实体类元信息的迭代器
     */
    public Iterator<ClassInfo> iterateEntityClassInfo() {
        List<ClassInfo> tmpList = new ArrayList<>();

        for (Map<String, ClassInfo> entityMap : this.classesMap.values()) {
            ClassInfo classInfo = entityMap.get(TYPE_ENTITY_CLASS);
            if (classInfo != null) {
                tmpList.add(classInfo);
            }
        }

        return tmpList.iterator();
    }

    /**
     * 获得当前这个容器当中所有的类信息
     *
     * @return 这个容器当中所有的类信息
     */
    public Collection<ClassInfo> getAllClassInfo() {
        ArrayList<ClassInfo> retValue = new ArrayList<>();

        for (List<ClassInfo> classInfo : this.classesListByPackage.values()) {
            retValue.addAll(classInfo);
        }

        return retValue;
    }

    /**
     * 往这个容器当中保存一条字段到属性的关联关系的记录
     *
     * @param entityClassInfo 实体类元信息
     * @param fieldInfo       字段
     * @param attribute       对应的属性
     */
    public void putAttribute(ClassInfo entityClassInfo, FieldInfo fieldInfo, AttributeInfo attribute) {
        FieldMetaKey key = toKey(entityClassInfo, fieldInfo);
        this.valueMap.put(key, attribute(attribute));
    }

    /**
     * 往这个容器当中保存一条字段到关联关系的记录
     *
     * @param entityClassInto 实体类元信息
     * @param fieldInfo       字段信息
     * @param relationship    关联关系
     * @param isEndA          表示这个字段是否对应着关联关系的 A 端
     */
    public void putRelationship(ClassInfo entityClassInto, FieldInfo fieldInfo,
                                EntityRelationship relationship, boolean isEndA) {
        FieldMetaKey key = toKey(entityClassInto, fieldInfo);
        this.valueMap.put(key, relationship(relationship, isEndA));
    }

    /**
     * 设置这个字段是否自动创建
     *
     * @param classInfo 实体类元信息
     * @param fieldInfo 字段信息
     */
    public void setAutoCreated(ClassInfo classInfo, FieldInfo fieldInfo) {
        FieldMetaKey key = toKey(classInfo, fieldInfo);
        this.valueMap.put(key, autoCreated());
    }


    /**
     * 获得指定字段对应的属性信息
     *
     * @param classInfo 类元信息
     * @param fieldInfo 字段信息
     * @return 对应的属性信息；如果这个字段不是由属性产生，则返回 null
     */
    public AttributeInfo getAttribute(ClassInfo classInfo, FieldInfo fieldInfo) {
        FieldMetaKey key = toKey(classInfo, fieldInfo);
        FieldMetaValue metaValue = this.valueMap.get(key);

        if (metaValue != null) {
            return metaValue.attribute;
        } else {
            return null;
        }
    }

    /**
     * 获得指定字段对应的关联关系
     *
     * @param classInfo 类元信息
     * @param fieldInfo 字段信息
     * @return 对应的属性信息；如果这个字段不是由关联关系产生，则返回 null
     */
    public EntityRelationship getRelationship(ClassInfo classInfo, FieldInfo fieldInfo) {
        FieldMetaKey key = toKey(classInfo, fieldInfo);
        FieldMetaValue metaValue = this.valueMap.get(key);

        if (metaValue != null) {
            return metaValue.relationship;
        } else {
            return null;
        }
    }

    /**
     * 获得这个字段是否表示关联关系的 A 端
     *
     * @param classInfo 类元属性信息
     * @param fieldInfo 字段信息
     * @return 表示这个字段是否为关联关系 A 端的布尔值
     */
    public boolean isEndAField(ClassInfo classInfo, FieldInfo fieldInfo) {
        FieldMetaKey key = toKey(classInfo, fieldInfo);
        FieldMetaValue metaValue = this.valueMap.get(key);

        return metaValue.isRelationshipEndA;
    }

    /**
     * 获得这个字段是否属于自动生成字段
     *
     * @param classInfo 类元信息
     * @param fieldInfo 字段信息
     * @return 表示这个字段是否属于自动生成的布尔值
     */
    public boolean isAutoCreated(ClassInfo classInfo, FieldInfo fieldInfo) {
        FieldMetaKey key = toKey(classInfo, fieldInfo);
        FieldMetaValue value = this.valueMap.get(key);

        return value != null && value.isAutoCreated;
    }

    private FieldMetaKey toKey(ClassInfo entityClassInfo, FieldInfo fieldInfo) {
        FieldMetaKey key = new FieldMetaKey();
        key.className = entityClassInfo.getClassName();
        key.fieldName = fieldInfo.getName();

        return key;
    }

    private FieldMetaValue relationship(EntityRelationship relationship, boolean isEndA) {
        FieldMetaValue value = new FieldMetaValue();
        value.relationship = relationship;
        value.isRelationshipEndA = isEndA;

        return value;
    }

    private FieldMetaValue attribute(AttributeInfo attributeInfo) {
        FieldMetaValue value = new FieldMetaValue();
        value.attribute = attributeInfo;

        return value;
    }

    private FieldMetaValue autoCreated() {
        FieldMetaValue value = new FieldMetaValue();
        value.isAutoCreated = true;

        return value;
    }

    /**
     * 以实体名称和类的类型为键保存类元信息
     *
     * @param entityName 实体名称
     * @param type       类类型
     * @param classInfo  类的元信息
     */
    public void saveClassByEntityAndType(String entityName, String type, ClassInfo classInfo) {
        // 使用实体名称和类型保存
        Map<String, ClassInfo> entityMap = this.classesMap.computeIfAbsent(entityName, k -> new HashMap<>());
        entityMap.putIfAbsent(type, classInfo);

        // 根据包名进行保存
        saveClassByPackageName(classInfo);

        // 如果这个类属于实体类，则保存相关的信息
        this.entityInfoFullClassName.put(classInfo.getFullName(), this.entityInfoMapByName.get(entityName));
    }

    /**
     * 通过实体名称和类的类型获得这个类对应的类元信息对象
     *
     * @param entityName 实体名称
     * @param type       类的类型
     * @return 对应类的类元信息；如果指定的类元信息不存在，则返回 null
     */
    public ClassInfo getClassByEntityAndType(String entityName, String type) {
        Map<String, ClassInfo> entityMap = this.classesMap.get(entityName);
        if (entityMap == null) {
            return null;
        }

        return entityMap.get(type);
    }

    /**
     * 根据名称将实体对象保存起来
     *
     * @param name       实体名称
     * @param entityInfo 实体对象
     * @return 如果原来已经存在同名的实体类信息，则返回旧的实体类信息，否则返回 null
     */
    public EntityInfo saveEntityByName(String name, EntityInfo entityInfo) {
        return this.entityInfoMapByName.put(name, entityInfo);
    }

    /**
     * 根据实体名称获得实体信息对象
     *
     * @param name 实体名称
     * @return 如果这个实体名称对应的实体信息对象存在，则返回这个实体信息对象；否则返回 null
     */
    public EntityInfo getEntityByName(String name) {
        return this.entityInfoMapByName.get(name);
    }


    /**
     * 根据类全名查找实体对象
     *
     * @param fullClassName 类全名
     * @return 这个类全名对应的实体对象；如果实体名称对应的实体对象不存在，则返回 null
     */
    public EntityInfo getEntityByFullClassName(String fullClassName) {
        return this.entityInfoFullClassName.get(fullClassName);
    }

    /**
     * 根据实体名称寻找到对应的类型对象
     *
     * @param entityName 实体名称
     * @return 这个实体名称对应的类型对象
     */
    @Override
    public TypeInfo findTypeInfoByEntityName(String entityName) {
        ClassInfo entityClassInfo = this.classesMap.get(entityName).get(TYPE_ENTITY_CLASS);
        if (entityClassInfo == null) {
            throw new IllegalArgumentException(String.format("entity %s class was not found!", entityName));
        }

        return entityClassInfo.getType();
    }

    /**
     * 将类自动保存到相应的包列表当中
     *
     * @param classInfo 要保存的类元信息
     */
    public void saveClassByPackageName(ClassInfo classInfo) {
        // 获取相同包下面的类的列表
        List<ClassInfo> classInSamePackage = this.classesListByPackage.computeIfAbsent(classInfo.getPackageName(), k -> new ArrayList<>());

        // 判断是否存在同名的类，如果存在，则覆盖
        for (ClassInfo clazz : classInSamePackage) {
            if (clazz.getClassName().equals(classInfo.getClassName())) {
                classInSamePackage.remove(clazz);
                break;
            }
        }

        classInSamePackage.add(classInfo);
    }

    /**
     * 表示字段产生信息的值
     */
    private static final class FieldMetaValue {
        public AttributeInfo attribute;
        public EntityRelationship relationship;

        public boolean isRelationshipEndA;
        public boolean isAutoCreated;

        public FieldMetaValue() {
            this.isAutoCreated = false;
        }
    }

    /**
     * 用作字段产生来源信息的 key
     */
    private static final class FieldMetaKey {
        public String className;
        public String fieldName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FieldMetaKey that = (FieldMetaKey) o;

            if (className != null ? !className.equals(that.className) : that.className != null) return false;
            return fieldName != null ? fieldName.equals(that.fieldName) : that.fieldName == null;
        }

        @Override
        public int hashCode() {
            int result = className != null ? className.hashCode() : 0;
            result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
            return result;
        }
    }

    private static class RORelationshipHandler extends RelationshipHandler {
        private RelationshipHandler handler;

        public RORelationshipHandler(RelationshipHandler handler) {
            this.handler = handler;
        }

        @Override
        public String getHandler() {
            return handler.getHandler();
        }

        @Override
        public RelationshipHandler setHandler(String handler) {
            return this;
        }

        @Override
        public String getToBeHandled() {
            return handler.getToBeHandled();
        }

        @Override
        public RelationshipHandler setToBeHandled(String toBeHandled) {
            return this;
        }

        @Override
        public RelationshipType getType() {
            return this.handler.getType();
        }

        @Override
        public RelationshipHandler setType(RelationshipType type) {
            return this;
        }

        @Override
        public String getHandlerFieldName() {
            return this.handler.getHandlerFieldName();
        }

        @Override
        public RelationshipHandler setHandlerFieldName(String handlerFieldName) {
            return this;
        }

        @Override
        public String getHandledFieldName() {
            return this.handler.getHandledFieldName();
        }

        @Override
        public RelationshipHandler setHandledFieldName(String handledFieldName) {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            RORelationshipHandler that = (RORelationshipHandler) o;

            return handler != null ? handler.equals(that.handler) : that.handler == null;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (handler != null ? handler.hashCode() : 0);
            return result;
        }
    }

    /**
     * 用于表示关联关系的维护方与被维护方信息
     */
    public static class RelationshipHandler {
        private String handler;
        private String toBeHandled;
        private RelationshipType type;
        private String handlerFieldName;
        private String handledFieldName;

        public String getHandler() {
            return handler;
        }

        public RelationshipHandler setHandler(String handler) {
            this.handler = handler;
            return this;
        }

        public String getToBeHandled() {
            return toBeHandled;
        }

        public RelationshipHandler setToBeHandled(String toBeHandled) {
            this.toBeHandled = toBeHandled;
            return this;
        }

        public RelationshipType getType() {
            return type;
        }

        public RelationshipHandler setType(RelationshipType type) {
            this.type = type;
            return this;
        }

        public String getHandlerFieldName() {
            return handlerFieldName;
        }

        public RelationshipHandler setHandlerFieldName(String handlerFieldName) {
            this.handlerFieldName = handlerFieldName;
            return this;
        }

        public String getHandledFieldName() {
            return handledFieldName;
        }

        public RelationshipHandler setHandledFieldName(String handledFieldName) {
            this.handledFieldName = handledFieldName;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RelationshipHandler that = (RelationshipHandler) o;

            if (handler != null ? !handler.equals(that.handler) : that.handler != null) return false;
            if (toBeHandled != null ? !toBeHandled.equals(that.toBeHandled) : that.toBeHandled != null) return false;
            return handlerFieldName != null ? handlerFieldName.equals(that.handlerFieldName) : that.handlerFieldName == null;
        }

        @Override
        public int hashCode() {
            int result = handler != null ? handler.hashCode() : 0;
            result = 31 * result + (toBeHandled != null ? toBeHandled.hashCode() : 0);
            result = 31 * result + (handlerFieldName != null ? handlerFieldName.hashCode() : 0);
            return result;
        }
    }
}
