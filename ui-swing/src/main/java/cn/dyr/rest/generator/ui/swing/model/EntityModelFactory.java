package cn.dyr.rest.generator.ui.swing.model;

/**
 * 创建常用实体的工厂类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityModelFactory {

    public static EntityModel userModel() {
        EntityModel retValue = new EntityModel();

        retValue.setName("User");
        retValue.setDescription("用户");

        AttributeModel idAttribute = new AttributeModel()
                .setMandatory(true)
                .setExpose(true)
                .setAsSelectionCondition(true)
                .setPrimaryIdentifier(true)
                .setType("long")
                .setName("id")
                .setDescription("唯一标识符");
        retValue.addAttributeModel(idAttribute);

        AttributeModel usernameAttribute = new AttributeModel()
                .setMandatory(false)
                .setExpose(true)
                .setAsSelectionCondition(true)
                .setType("varchar")
                .setName("user")
                .setDescription("用户名");
        retValue.addAttributeModel(usernameAttribute);

        AttributeModel passwordAttribute = new AttributeModel()
                .setMandatory(false)
                .setExpose(false)
                .setAsSelectionCondition(true)
                .setType("varchar")
                .setName("password")
                .setDescription("密码");
        retValue.addAttributeModel(passwordAttribute);

        return retValue;
    }

}
