package cn.dyr.rest.generator.ui.swing.exception;

/**
 * 表示实体当中缺少唯一标识符
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityIdentifierNotFoundException extends RuntimeException {

    private String entityName;

    public EntityIdentifierNotFoundException(String entityName) {
        super(String.format("实体 %s 当中没有唯一标识符", entityName));

        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public EntityIdentifierNotFoundException setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
}
