package cn.dyr.rest.generator.ui.swing.context;

import cn.dyr.rest.generator.ui.swing.model.BasicInfoModel;
import cn.dyr.rest.generator.ui.swing.model.DBInfoModel;
import cn.dyr.rest.generator.ui.swing.model.EntityModel;
import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;

import java.awt.Component;

/**
 * 当工程信息发生变化时的消息接收者
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface ProjectContextEventListener {

    /**
     * 当工程的基本信息发生变更时调用
     *
     * @param basicInfoModel 新的工程基本信息
     * @param component      执行这个变更的组件
     */
    void onBasicInfoChange(BasicInfoModel basicInfoModel, Component component);

    /**
     * 当工程的数据库信息发生变更时调用
     *
     * @param dbInfoModel 新的数据库信息
     * @param component   执行这个变更的组件
     */
    void onDBInfoChange(DBInfoModel dbInfoModel, Component component);

    /**
     * 当有实体被添加到工程当中执行这个方法
     *
     * @param entityModel 被添加到工程当中的实体
     * @param component   执行这个操作的组件
     */
    void onEntityAdded(EntityModel entityModel, Component component);

    /**
     * 当有实体信息发生更改的时候会执行这个方法
     *
     * @param oldEntityModel 旧的实体数据
     * @param newEntityModel 新的实体数据
     * @param component      执行这个操作的组件
     */
    void onEntityUpdated(EntityModel oldEntityModel, EntityModel newEntityModel, Component component);

    /**
     * 当实体信息被删除时会执行这个方法
     *
     * @param entityModel 被删除的实体信息
     * @param component   执行这个操作的组件
     */
    void onEntityDeleted(EntityModel entityModel, Component component);

    /**
     * 当有关联关系被添加到工程当中时执行这个方法
     *
     * @param relationshipModel 被添加到工程当中的关联关系
     * @param component         执行这个操作的组件
     */
    void onRelationshipAdded(RelationshipModel relationshipModel, Component component);

    /**
     * 当工程当中的关联关系信息发生变更时执行这个方法
     *
     * @param oldRelationship 旧的关联关系数据
     * @param newRelationship 新的关联关系数据
     * @param component       执行这个操作的组件
     */
    void onRelationshipUpdated(RelationshipModel oldRelationship, RelationshipModel newRelationship, Component component);

    /**
     * 当工程当中删除一个关联关系时执行这个方法
     *
     * @param relationshipModel 被删除的关联关系
     * @param component         执行这个操作的组件
     */
    void onRelationshipDeleted(RelationshipModel relationshipModel, Component component);
}
