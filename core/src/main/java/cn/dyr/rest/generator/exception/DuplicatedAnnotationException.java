package cn.dyr.rest.generator.exception;

/**
 * 表示在一个范围当中添加了重复的注解
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DuplicatedAnnotationException extends RuntimeException {

    private String fullName;
    private String objectBeAnnotated;

    public DuplicatedAnnotationException(String fullName, String objectBeAnnotated) {
        super(String.format("Annotation %s duplicated in %s", fullName, objectBeAnnotated));

        this.fullName = fullName;
        this.objectBeAnnotated = objectBeAnnotated;
    }

    public String getFullName() {
        return fullName;
    }

    public String getObjectBeAnnotated() {
        return objectBeAnnotated;
    }
}
