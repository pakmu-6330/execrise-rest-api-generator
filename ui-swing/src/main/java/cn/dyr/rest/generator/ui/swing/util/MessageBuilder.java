package cn.dyr.rest.generator.ui.swing.util;

import cn.dyr.rest.generator.ui.swing.model.RelationshipModel;

import java.util.List;

/**
 * 用于构建提示消息的工具类
 *
 * @author DENG YURONG
 */
public class MessageBuilder {

    /**
     * 创建这个实体处于关系当中被维护方而无法删除的错误消息
     *
     * @param relationshipModelList 相关关联关系的列表
     * @return 对应的错误消息
     */
    public static String entityDeletedDueToHandled(List<RelationshipModel> relationshipModelList) {
        StringBuilder builder = new StringBuilder();

        builder.append("当前实体在下面关系中处于被维护方：");
        builder.append(System.lineSeparator());

        if (relationshipModelList != null && relationshipModelList.size() > 0) {
            for (RelationshipModel relationshipModel : relationshipModelList) {
                builder.append(relationshipModel.getName());
                builder.append(System.lineSeparator());
            }
        }

        builder.append("当前实体不能被删除，请处理相关的关联关系以后再进行删除操作！");

        return builder.toString();
    }

    /**
     * 创建这个实体删除时会级联删除关联关系的确认信息
     *
     * @param relationshipModelList 相关的关联关系对象
     * @return 对应的确认信息
     */
    public static String confirmMsgForHandles(List<RelationshipModel> relationshipModelList) {
        StringBuilder builder = new StringBuilder();

        builder.append("当前实体维护着下面的关联关系：");
        builder.append(System.lineSeparator());

        if (relationshipModelList != null && relationshipModelList.size() > 0) {
            for (RelationshipModel relationshipModel : relationshipModelList) {
                builder.append(relationshipModel.getName());
                builder.append(System.lineSeparator());
            }
        }

        builder.append("如果删除实体，上面的关联关系也会随之删除，是否继续？");

        return builder.toString();
    }
}
