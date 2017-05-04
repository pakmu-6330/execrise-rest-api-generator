package cn.dyr.rest.generator.ui.swing.exception;

/**
 * 表示未能在工程当中找到实体模型抛出的异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName) {
        super(String.format("entity named %s not found!", entityName));
    }
}
