package cn.dyr.rest.generator.ui.swing.context;

import cn.dyr.rest.generator.ui.swing.SwingUIApplication;
import cn.dyr.rest.generator.ui.swing.exception.DuplicatedNameException;
import cn.dyr.rest.generator.ui.swing.exception.EntityNotFoundException;
import cn.dyr.rest.generator.ui.swing.exception.RelationshipConstraintException;
import cn.dyr.rest.generator.ui.swing.model.AttributeModel;
import cn.dyr.rest.generator.ui.swing.model.BasicInfoModel;
import cn.dyr.rest.generator.ui.swing.model.DBInfoModel;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.ProjectModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;
import cn.dyr.rest.generator.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 用作整个工程的上下文对象
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectContext {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(ProjectContext.class);
    }

    private static class RelatedRelationshipData {
        List<RelationshipModel> handles;
        List<RelationshipModel> handled;

        RelatedRelationshipData() {
            this.handled = new LinkedList<>();
            this.handles = new LinkedList<>();
        }
    }

    private List<ProjectContextEventListener> listeners;
    private ProjectModel projectModel;

    // --- 关联关系相关区域 ---
    private HashMap<String, RelationshipModel> relationshipById;
    private HashMap<String, RelationshipModel> relationshipByName;
    private HashMap<String, RelatedRelationshipData> relationshipDataByEntityId;

    // --- 实体相关的变量区域 ---
    private HashMap<String, EntityModel> entityMapByName;
    private HashMap<String, EntityModel> entityMapById;
    private List<String> entityNameList;

    public ProjectContext(ProjectModel projectModel) {
        this.listeners = new ArrayList<>();

        this.entityMapByName = new HashMap<>();
        this.entityMapById = new HashMap<>();
        this.entityNameList = new ArrayList<>();

        this.relationshipById = new HashMap<>();
        this.relationshipByName = new HashMap<>();
        this.relationshipDataByEntityId = new HashMap<>();

        this.projectModel = projectModel;
    }

    public void addListener(ProjectContextEventListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public List<ProjectContextEventListener> getListeners() {
        return listeners;
    }

    public ProjectContext setListeners(List<ProjectContextEventListener> listeners) {
        this.listeners = listeners;
        return this;
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void removeListener(ProjectContextEventListener listener) {
        this.listeners.remove(listener);
    }

    public Iterator<String> allEntityNames() {
        return this.entityNameList.iterator();
    }

    public int getEntityCount() {
        return this.entityNameList.size();
    }

    public EntityModel getEntityByName(String name) {
        return this.entityMapByName.get(name);
    }

    private void addEntity(EntityModel entityModel, Component component) {
        // 1. 检查这个实体是否已经存在于工程当中
        if (this.entityMapByName.get(entityModel.getName()) != null) {
            throw new DuplicatedNameException("Entity", entityModel.getName());
        }

        if (this.entityMapById.get(entityModel.getId()) != null) {
            return;
        }

        // 2. 将这个实体存放到工程和容器当中
        this.projectModel.getEntityList().add(entityModel);
        this.entityMapByName.put(entityModel.getName(), entityModel);
        this.entityMapById.put(entityModel.getId(), entityModel);
        this.relationshipDataByEntityId.put(entityModel.getId(), new RelatedRelationshipData());

        this.entityNameList.add(entityModel.getName());

        // 3. 调用相应的事件监听器
        SwingUIApplication.getInstance().runBackground(new EntityAddEventNotifier(entityModel, component));

        // 4. print log
        logger.debug("entity {} added!", entityModel.getName());
    }

    /**
     * 往工程当中保存一个实体信息
     *
     * @param entityModel 要保存到工程当中的实体信息
     * @param component   执行保存操作的组件
     */
    public void saveEntity(EntityModel entityModel, Component component) {
        // 1. 判断这个实体名称是否以及存在，如果存在，在直接覆盖；否则创建新实体对象
        EntityModel existedEntityModel = this.entityMapById.get(entityModel.getId());
        if (existedEntityModel != null) {
            if (existedEntityModel != entityModel) {
                BeanUtils.copyProperties(entityModel, existedEntityModel);
            }

            // 2. 调用相应的监听器通知
            SwingUIApplication.getInstance().runBackground(new EntityUpdateEventNotifier(existedEntityModel, entityModel, component));

        } else {
            addEntity(entityModel, component);
        }
    }

    /**
     * 判断关联关系是否存在从 A 端实体到 B 端实体的方向的关系
     *
     * @param relationshipModel 要进行判断的关联关系对象
     * @return 如果关联关系存在从 A 端实体到 B 端实体方向的关联，则返回 true；否则返回 false
     */
    private boolean relationshipHasAToB(RelationshipModel relationshipModel) {
        return (relationshipModel.getDirection() & RelationshipModel.DIRECTION_A_TO_B) == RelationshipModel.DIRECTION_A_TO_B;
    }

    /**
     * 判断关联关系是否存在从 B 端实体到 A 端实体的方向的关系
     *
     * @param relationshipModel 要进行判断的关联关系对象
     * @return 如果关联关系存在从 B 端实体到 A 端实体方向的关联，则返回 true；否则返回 false
     */
    private boolean relationshipHasBToA(RelationshipModel relationshipModel) {
        return (relationshipModel.getDirection() & RelationshipModel.DIRECTION_B_TO_A) == RelationshipModel.DIRECTION_B_TO_A;
    }

    /**
     * 判断属性名是否存在与关联关系的数据当中
     *
     * @param data          关联关系数据
     * @param attributeName 属性名称
     * @param forA          如果这个值为 true，则仅仅会在关联关系的 A 端实体进行查找；如果为 false，则仅仅会在关联关系的 B 端实体进行查找
     * @return 如果属性名存在与关联关系的维护属性当中，则返回 true；否则返回 false
     */
    private boolean isAttributeExistsInRelationshipData(RelatedRelationshipData data, String attributeName, boolean forA) {
        for (RelationshipModel relationshipModel : data.handles) {
            String valueToBeCheck = (forA ? relationshipModel.getEndAAttributeName() : relationshipModel.getEndBAttributeName());
            if (valueToBeCheck.equalsIgnoreCase(attributeName)) {
                return true;
            }
        }

        for (RelationshipModel relationshipModel : data.handled) {
            String valueToBeCheck = (forA ? relationshipModel.getEndAAttributeName() : relationshipModel.getEndBAttributeName());
            if (valueToBeCheck.equalsIgnoreCase(attributeName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 自动根据关联关系的状态决定保存还是添加一个关联关系数据
     *
     * @param relationshipModel 要进行保存或者修改的关联关系数据
     * @param component         执行这个操作的组件
     */
    public void saveRelationship(RelationshipModel relationshipModel, Component component) {
        // 1. 判断这个属性是否已经存在，如果存在，直接覆盖。否则创建一个新对象
        RelationshipModel existsModel = this.relationshipById.get(relationshipModel.getId());
        if (existsModel != null) {
            if (existsModel != relationshipModel) {
                BeanUtils.copyProperties(relationshipModel, existsModel, "id");
            }

            // 2. 调用相应的监听器
            SwingUIApplication.getInstance().runBackground(new RelationshipUpdatedEventNotifier(existsModel, relationshipModel, component));
        } else {
            this.addRelationship(relationshipModel, component);
        }

        // 3. print log
        logger.debug("relationship {} saved!", relationshipModel.getId());
    }

    /**
     * 往工程当中添加一个关联关系
     *
     * @param relationshipModel 要保存到工程当中的关联关系
     * @param component         执行关联关系保存操作的组件
     */
    public void addRelationship(RelationshipModel relationshipModel, Component component) {
        // 1. 判断两端的关系是否存在
        EntityModel entityA = this.entityMapByName.get(relationshipModel.getEntityModelA().getName());
        if (entityA == null) {
            throw new EntityNotFoundException(relationshipModel.getEntityModelA().getName());
        }

        EntityModel entityB = this.entityMapByName.get(relationshipModel.getEntityModelB().getName());
        if (entityB == null) {
            throw new EntityNotFoundException(relationshipModel.getEntityModelB().getName());
        }

        // 2. 检查主控是否起作用
        boolean relationshipHandledByEntityA =
                relationshipModel.getEntityModelHandler() == relationshipModel.getEntityModelA();

        if (relationshipHandledByEntityA && !relationshipHasAToB(relationshipModel)) {
            throw new RelationshipConstraintException("handler has no attributes related to another entity");
        }

        if (relationshipModel.getEntityModelHandler() == relationshipModel.getEntityModelB() &&
                !relationshipHasBToA(relationshipModel)) {
            throw new RelationshipConstraintException("handler has no attribute related to another entity");
        }

        // 3. 检查属性名是否与原有的属性名称重复
        // 3.0. 检查关联关系的名称是否已经重复
        String relationshipName = relationshipModel.getName();

        if (this.relationshipByName.get(relationshipName) != null) {
            throw new DuplicatedNameException(DuplicatedNameException.TYPE_RELATIONSHIP, relationshipName);
        }

        // 3.1. 检查这个实体本身的属性
        String endAAttributeName = relationshipModel.getEndAAttributeName();
        String endBAttributeName = relationshipModel.getEndBAttributeName();

        if (relationshipHasAToB(relationshipModel)) {
            if (StringUtils.isStringEmpty(endAAttributeName)) {
                throw new RelationshipConstraintException("invalid attribute name: " + endAAttributeName);
            }

            for (AttributeModel attribute : entityA.getAttributeModelList()) {
                if (attribute.getName().equalsIgnoreCase(endAAttributeName)) {
                    throw new DuplicatedNameException(DuplicatedNameException.TYPE_ATTRIBUTE, endAAttributeName);
                }
            }
        }

        if (relationshipHasBToA(relationshipModel)) {
            if (StringUtils.isStringEmpty(endBAttributeName)) {
                throw new RelationshipConstraintException("invalid attribute name: " + endBAttributeName);
            }

            for (AttributeModel attribute : entityB.getAttributeModelList()) {
                if (attribute.getName().equalsIgnoreCase(endBAttributeName)) {
                    throw new DuplicatedNameException(DuplicatedNameException.TYPE_ATTRIBUTE, endBAttributeName);
                }
            }
        }

        // 3.2.prepare 准备关联关系相关的数据结构
        RelatedRelationshipData aRelationship =
                this.relationshipDataByEntityId.computeIfAbsent(entityA.getId(), k -> new RelatedRelationshipData());
        RelatedRelationshipData bRelationship =
                this.relationshipDataByEntityId.computeIfAbsent(entityB.getId(), k -> new RelatedRelationshipData());

        // 3.2. 检查这个关联属性是否被其他的关联关系所使用
        if (relationshipHasAToB(relationshipModel) &&
                isAttributeExistsInRelationshipData(aRelationship, endAAttributeName, true)) {
            throw new DuplicatedNameException(DuplicatedNameException.TYPE_ATTRIBUTE, endAAttributeName);
        }

        if (relationshipHasBToA(relationshipModel) &&
                isAttributeExistsInRelationshipData(bRelationship, endBAttributeName, false)) {
            throw new DuplicatedNameException(DuplicatedNameException.TYPE_ATTRIBUTE, endBAttributeName);
        }

        // 4. 重新组装一个关联关系的对象
        RelationshipModel newModel = new RelationshipModel();
        BeanUtils.copyProperties(relationshipModel, newModel, "id");
        newModel.setEntityModelA(entityA);
        newModel.setEntityModelB(entityB);

        if (relationshipHandledByEntityA) {
            newModel.setEntityModelHandler(entityA);
        } else {
            newModel.setEntityModelHandler(entityB);
        }

        // 5. 将关联关系的数据保存到相关的数据结构当中
        this.relationshipByName.put(relationshipName, newModel);
        this.relationshipById.put(newModel.getId(), newModel);

        if (relationshipHandledByEntityA) {
            aRelationship.handles.add(newModel);
            bRelationship.handled.add(newModel);
        } else {
            aRelationship.handled.add(newModel);
            aRelationship.handles.add(newModel);
        }

        // 6. 保存到工程模型当中
        projectModel.getRelationshipList().add(newModel);

        // 7. 调用相应的回调函数
        SwingUIApplication.getInstance().runBackground(new RelationshipAddedNotifier(newModel, component));

        // 8. 打印日志
        logger.debug("relationship {} saved!", newModel.getName());
    }

    public RelationshipModel findRelationshipByName(String name) {
        return this.relationshipByName.get(name);
    }

    /**
     * 删除关联关系
     *
     * @param relationshipModel 要删除的关联关系
     * @param component         执行关联关系删除的组件
     */
    public void deleteRelationship(RelationshipModel relationshipModel, Component component) {
        // 1. 判断这个关联关系对象是否存在
        RelationshipModel existsRelationship = this.relationshipById.get(relationshipModel.getId());
        if (existsRelationship == null) {
            return;
        }

        // 2. 删除相关数据结构上的数据
        EntityModel entityModelA = relationshipModel.getEntityModelA();
        EntityModel entityModelB = relationshipModel.getEntityModelB();
        EntityModel entityModelHandler = relationshipModel.getEntityModelHandler();

        boolean handledByA = (entityModelHandler == entityModelA);

        RelatedRelationshipData aRelationship = relationshipDataByEntityId.get(entityModelA.getId());
        RelatedRelationshipData bRelationship = relationshipDataByEntityId.get(entityModelB.getId());

        if (handledByA) {
            aRelationship.handles.remove(existsRelationship);
            bRelationship.handled.remove(existsRelationship);
        } else {
            aRelationship.handled.remove(existsRelationship);
            bRelationship.handles.remove(existsRelationship);
        }

        // 3. 从工程当中删除这个关联关系
        projectModel.getRelationshipList().remove(existsRelationship);

        // 4. 调用相应的回调方法
        SwingUIApplication.getInstance().runBackground(new RelationshipDeletedEventNotifier(existsRelationship, component));

        // 5. 打印日志
        logger.debug("relationship {} deleted!", relationshipModel.getName());
    }

    /**
     * 删除实体信息
     *
     * @param entityModel 要删除的实体信息
     * @param component   执行删除操作的组件
     */
    public void deleteEntity(EntityModel entityModel, Component component) {
        // 1. 判断这个实体是否存在与项目当中，如果不存在则会不会执行任何操作
        EntityModel existsEntityModel = this.entityMapById.get(entityModel.getId());
        if (existsEntityModel == null) {
            return;
        }

        // 2. 检查是否存在与这个实体相关的关联关系
        RelatedRelationshipData relatedRelationshipData = this.relationshipDataByEntityId.get(existsEntityModel.getId());
        if (relatedRelationshipData.handled.size() != 0) {
            throw new RelationshipConstraintException(String.format("实体 %s 存在相关的关联关系，删除前需要先删除相应的关联关系", existsEntityModel.getName()));
        }

        // 3. 执行删除操作
        this.projectModel.getEntityList().remove(existsEntityModel);
        this.entityMapByName.remove(existsEntityModel.getName());
        this.entityMapById.remove(existsEntityModel.getId());

        this.relationshipDataByEntityId.remove(existsEntityModel.getName());

        this.entityNameList.remove(entityModel.getName());

        // 3.1. 删除这个实体维护的关联关系
        List<RelationshipModel> tmpList = new ArrayList<>();
        tmpList.addAll(relatedRelationshipData.handles);

        for (RelationshipModel relationshipModel : tmpList) {
            this.deleteRelationship(relationshipModel, component);
        }

        // 4. 调用回调方法
        SwingUIApplication.getInstance().runBackground(new EntityDeleteEventNotifier(existsEntityModel, component));

        // 5. print log
        logger.debug("entity {} deleted!", entityModel.getName());
    }

    public boolean isEntityExists(String entityName) {
        return (this.entityMapByName.get(entityName) != null);
    }

    /**
     * 获得这个实体对象维护的关联关系的列表
     *
     * @param entityModel 实体对象
     * @return 这个实体对象维护的关联关系的列表
     */
    public List<RelationshipModel> getRelationshipListEntityHandles(EntityModel entityModel) {
        RelatedRelationshipData relationshipData = this.relationshipDataByEntityId.get(entityModel.getId());
        return relationshipData.handles;
    }

    /**
     * 设置工程的数据库信息
     *
     * @param dbInfo    数据库信息
     * @param component 修改数据库信息的组件
     */
    public void setDBInfo(DBInfoModel dbInfo, Component component) {
        // 1. 设置工程数据库信息
        projectModel.setDbInfo(dbInfo);

        // 2. 执行相应的回调通知其他的组件
        SwingUIApplication.getInstance().runBackground(new DBChangeEventNotifier(dbInfo, component));

        // 3. 打印日志
        logger.debug("database info saved!");
    }

    /**
     * 设置工程的基本信息
     *
     * @param basicInfo 工程的基本信息
     * @param component 执行基本信息修改的组件
     */
    public void setBasicInfo(BasicInfoModel basicInfo, Component component) {
        // 1. 设置工程基本信息
        projectModel.setBasicInfo(basicInfo);

        // 2. 执行相应的回调通知其他的组件
        SwingUIApplication.getInstance().runBackground(new BasicInfoChangeEventNotifier(basicInfo, component));

        // 3. print log
        logger.debug("entity basic info saved!");
    }

    private class RelationshipAddedNotifier implements Runnable {

        private RelationshipModel model;
        private Component component;

        RelationshipAddedNotifier(RelationshipModel model, Component component) {
            this.model = model;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onRelationshipAdded(model, component);
                }
            }
        }
    }

    private class RelationshipDeletedEventNotifier implements Runnable {

        private RelationshipModel relationshipModel;
        private Component component;

        RelationshipDeletedEventNotifier(RelationshipModel relationshipModel, Component component) {
            this.relationshipModel = relationshipModel;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onRelationshipDeleted(relationshipModel, component);
                }
            }
        }
    }

    private class EntityDeleteEventNotifier implements Runnable {

        private EntityModel entityModel;
        private Component component;

        EntityDeleteEventNotifier(EntityModel entityModel, Component component) {
            this.entityModel = entityModel;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onEntityDeleted(entityModel, component);
                }
            }
        }
    }

    private class EntityUpdateEventNotifier implements Runnable {

        private EntityModel oldEntityModel;
        private EntityModel newEntityModel;
        private Component component;

        EntityUpdateEventNotifier(EntityModel oldEntityModel, EntityModel newEntityModel, Component component) {
            this.oldEntityModel = oldEntityModel;
            this.newEntityModel = newEntityModel;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onEntityUpdated(oldEntityModel, newEntityModel, component);
                }
            }
        }
    }

    private class BasicInfoChangeEventNotifier implements Runnable {

        private BasicInfoModel basicInfoModel;
        private Component component;

        BasicInfoChangeEventNotifier(BasicInfoModel basicInfoModel, Component component) {
            this.basicInfoModel = basicInfoModel;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onBasicInfoChange(basicInfoModel, component);
                }
            }
        }
    }

    private class RelationshipUpdatedEventNotifier implements Runnable {

        private RelationshipModel oldRelationship;
        private RelationshipModel newRelationship;
        private Component component;

        RelationshipUpdatedEventNotifier(
                RelationshipModel oldRelationship, RelationshipModel newRelationship,
                Component component) {
            this.oldRelationship = oldRelationship;
            this.newRelationship = newRelationship;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onRelationshipUpdated(oldRelationship, newRelationship, component);
                }
            }
        }
    }

    private class DBChangeEventNotifier implements Runnable {

        private DBInfoModel dbInfoModel;
        private Component component;

        DBChangeEventNotifier(DBInfoModel dbInfoModel, Component component) {
            this.dbInfoModel = dbInfoModel;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onDBInfoChange(dbInfoModel, component);
                }
            }
        }
    }

    private class EntityAddEventNotifier implements Runnable {

        private EntityModel entityModel;
        private Component component;

        EntityAddEventNotifier(EntityModel entityModel, Component component) {
            this.entityModel = entityModel;
            this.component = component;
        }

        @Override
        public void run() {
            if (listeners != null) {
                for (ProjectContextEventListener listener : listeners) {
                    listener.onEntityAdded(entityModel, component);
                }
            }
        }
    }
}
