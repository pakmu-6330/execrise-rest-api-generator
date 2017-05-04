package cn.dyr.rest.generator.java.meta.parameters.annotation;

import cn.dyr.rest.generator.java.generator.analysis.ImportContext;
import cn.dyr.rest.generator.java.generator.analysis.ImportedOperation;

import java.util.Set;

/**
 * 注解参数的抽象实现类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
abstract class AbstractAnnotationParameter implements AnnotationParameter {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void fillImportOperations(ImportContext context) {

    }
}
