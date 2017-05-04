package cn.dyr.rest.generator.exception;

/**
 * 所有代码生成过程中的异常都会抛出这个异常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CodeGenerationException extends RuntimeException {

    /**
     * 这个枚举类用来表示是在代码生成的哪一个环节出现问题
     */
    public enum CodeGenerationField {
        /**
         * 表示在生成类级别的代码时出现异常
         */
        CLASS,
        /**
         * 表示在生成方法级别的代码时出现异常
         */
        METHOD,
        /**
         * 表示在生成语句级别的代码时出现异常
         */
        STATEMENT,
        /**
         * 表示在生成标识符时出现异常
         */
        TOKEN
    }

    private CodeGenerationField generationField;

    public CodeGenerationException(String message) {
        super(message);
    }

    public CodeGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeGenerationField getGenerationField() {
        return generationField;
    }

    public CodeGenerationException setGenerationField(CodeGenerationField generationField) {
        this.generationField = generationField;
        return this;
    }
}
