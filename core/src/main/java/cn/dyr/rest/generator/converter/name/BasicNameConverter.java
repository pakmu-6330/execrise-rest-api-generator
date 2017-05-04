package cn.dyr.rest.generator.converter.name;

import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

/**
 * DAO，业务类，控制器类名转换的默认实现
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public abstract class BasicNameConverter implements INameConverter {

    @Override
    public String controllerNameFromEntityName(String entityName) {
        return StringUtils.upperFirstLatter(entityName) + "Controller";
    }

    @Override
    public String serviceNameFromEntityName(String entityName) {
        return StringUtils.upperFirstLatter(entityName + "Service");
    }

    @Override
    public String daoNameFromEntityName(String entityName) {
        return "I" + StringUtils.upperFirstLatter(entityName + "DAO");
    }

    @Override
    public String hateoasResourceNameFromEntityName(String entityName) {
        return StringUtils.upperFirstLatter(entityName) + "Resource";
    }

    @Override
    public String hateoasResourceAssemblerNameFromEntityName(String entityName) {
        return StringUtils.upperFirstLatter(entityName) + "ResourceAssembler";
    }

    @Override
    public String defaultNameOfVariableName(String className) {
        if (className.charAt(0) == 'I' && Character.isUpperCase(className.charAt(1))) {
            return StringUtils.lowerFirstLatter(className.substring(1));
        }

        return StringUtils.lowerFirstLatter(className);
    }
}
