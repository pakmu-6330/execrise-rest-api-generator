package cn.dyr.rest.generator.converter.instruction;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.IConverter;
import cn.dyr.rest.generator.java.meta.flow.IInstruction;

/**
 * 这个接口定义了常用的一些指令生成方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public interface IServiceInstructionConverter extends IConverter {

    /**
     * 用于生成一对一关联关系级联保存的指令
     *
     * @param handler            关系信息
     * @param entityVariableName 本实体类在方法参数中的名称
     * @return 一对一关系级联保存的操作指令
     */
    IInstruction oneToOneHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariableName);

    /**
     * 用于生成一对多关联关系级联保存的指令
     *
     * @param handler            关联关系信息
     * @param entityVariableName 本实体类在方法参数中的名称
     * @return 一对多关联级联保存的操作指令
     */
    IInstruction oneToManyHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariableName);

    /**
     * 用于生成多对一关联关系级联保存的指令
     *
     * @param handler            关联关系信息
     * @param entityVariableName 本实体类在方法参数中的名称
     * @return 多对一关联级联保存的操作指令
     */
    IInstruction manyToOneHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariableName);

    /**
     * 用于生成多对多关联关系级联保存的指令
     *
     * @param handler            关联关系信息
     * @param entityVariableName 本实体类在方法参数中的名称
     * @return 多对多关联级联保存的操作指令
     */
    IInstruction manyToManyHandlerServiceSave(ConvertDataContext.RelationshipHandler handler, String entityVariableName);

    /**
     * 用于处理一对关联关系的删除指令
     *
     * @param handler        关联关系信息
     * @param idVariableName 实体唯一标识符的变量名
     * @return 处理一对一关联关系删除的操作指令
     */
    IInstruction oneToOneHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName);

    /**
     * 用于处理一对多关联关系的删除指令
     *
     * @param handler        关联关系信息
     * @param idVariableName 实体唯一标识符的变量名
     * @return 处理一对多关联关系删除的操作指令
     */
    IInstruction oneToManyHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName);

    /**
     * 用于处理多对一关联关系的删除指令
     *
     * @param handler        关联关系信息
     * @param idVariableName 实体唯一标识符的变量名
     * @return 处理多对一关联关系删除的操作指令
     */
    IInstruction manyToOneHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName);

    /**
     * 用于处理多对多关联关系的删除之灵
     *
     * @param handler        关联关系信息
     * @param idVariableName 实体唯一标识符的变量名
     * @return 处理多对多关联关系删除的操作指令
     */
    IInstruction manyToManyHandledServiceDelete(ConvertDataContext.RelationshipHandler handler, String idVariableName);

    /**
     * 用于在一方中所维护的关联关系中创建一个多方对象
     *
     * @param handler        关联关系信息
     * @param idVariable     一方对象唯一标识符变量名
     * @param entityVariable 多方对象对象变量名
     * @return 处理这个多方对象创建的指令
     */
    IInstruction entityToManyCreatedInstructionForHandler(ConvertDataContext.RelationshipHandler handler,
                                                          String idVariable, String entityVariable);

    /**
     * 用于在作为被维护方的一方中添加一个多方对象的指令生成
     *
     * @param handler               关联关系信息
     * @param handledIdVariable     一方对象唯一标识符变量名
     * @param handlerEntityVariable 多方对象对象变量名
     * @return 处理这个多方对象创建的指令
     */
    IInstruction entityManyToOneCreatedInstructionForHandled(ConvertDataContext.RelationshipHandler handler,
                                                             String handledIdVariable, String handlerEntityVariable);

    /**
     * 用于关系维护方修改对一中的一方对象
     *
     * @param handler               关联关系信息
     * @param handlerIdVariable     维护方实体的唯一标识符
     * @param handledEntityVariable 被维护方的实体对象
     * @return 处理这个一方对象修改的指令
     */
    IInstruction entityToOneUpdateInstructionForHandler(ConvertDataContext.RelationshipHandler handler,
                                                        String handlerIdVariable, String handledEntityVariable);
}
