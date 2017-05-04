package cn.dyr.rest.generator.ui.swing.exception;

/**
 * 表示往工程中添加一个违反关联约束的关联关系
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class RelationshipConstraintException extends RuntimeException {

    public RelationshipConstraintException(String message) {
        super(message);
    }
}
