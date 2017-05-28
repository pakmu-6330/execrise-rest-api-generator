package cn.dyr.rest.generator.converter.schema;

import cn.dyr.rest.generator.converter.clazz.IControllerConverter;
import cn.dyr.rest.generator.converter.clazz.IDAOConverter;
import cn.dyr.rest.generator.converter.clazz.IEntityClassConverter;
import cn.dyr.rest.generator.converter.clazz.IResourceAssemblerConverter;
import cn.dyr.rest.generator.converter.clazz.IResourceClassConverter;
import cn.dyr.rest.generator.converter.clazz.IServiceConverter;
import cn.dyr.rest.generator.converter.field.IFieldConverter;
import cn.dyr.rest.generator.converter.instruction.IServiceInstructionConverter;
import cn.dyr.rest.generator.converter.method.IControllerMethodConverter;
import cn.dyr.rest.generator.converter.name.INameConverter;
import cn.dyr.rest.generator.converter.type.ITypeConverter;

/**
 * 用于组织一个转换方案下面的转换器集合的集合类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SchemaConverterList {

    private Class<? extends INameConverter> nameConverter;
    private Class<? extends ITypeConverter> typeConverter;
    private Class<? extends IFieldConverter> fieldConverter;
    private Class<? extends IServiceInstructionConverter> serviceInstructionConverter;
    private Class<? extends IControllerConverter> controllerConverter;
    private Class<? extends IControllerMethodConverter> controllerMethodConverter;
    private Class<? extends IResourceAssemblerConverter> resourceAssemblerConverter;
    private Class<? extends IDAOConverter> daoConverter;
    private Class<? extends IServiceConverter> serviceConverter;
    private Class<? extends IEntityClassConverter> entityClassConverter;
    private Class<? extends IResourceClassConverter> resourceClassConverter;

    public Class<? extends INameConverter> getNameConverter() {
        return nameConverter;
    }

    public SchemaConverterList setNameConverter(Class<? extends INameConverter> nameConverter) {
        this.nameConverter = nameConverter;
        return this;
    }

    public Class<? extends ITypeConverter> getTypeConverter() {
        return typeConverter;
    }

    public SchemaConverterList setTypeConverter(Class<? extends ITypeConverter> typeConverter) {
        this.typeConverter = typeConverter;
        return this;
    }

    public Class<? extends IFieldConverter> getFieldConverter() {
        return fieldConverter;
    }

    public SchemaConverterList setFieldConverter(Class<? extends IFieldConverter> fieldConverter) {
        this.fieldConverter = fieldConverter;
        return this;
    }

    public Class<? extends IServiceInstructionConverter> getServiceInstructionConverter() {
        return serviceInstructionConverter;
    }

    public SchemaConverterList setServiceInstructionConverter(Class<? extends IServiceInstructionConverter> serviceInstructionConverter) {
        this.serviceInstructionConverter = serviceInstructionConverter;
        return this;
    }

    public Class<? extends IControllerConverter> getControllerConverter() {
        return controllerConverter;
    }

    public SchemaConverterList setControllerConverter(Class<? extends IControllerConverter> controllerConverter) {
        this.controllerConverter = controllerConverter;
        return this;
    }

    public Class<? extends IControllerMethodConverter> getControllerMethodConverter() {
        return controllerMethodConverter;
    }

    public SchemaConverterList setControllerMethodConverter(Class<? extends IControllerMethodConverter> controllerMethodConverter) {
        this.controllerMethodConverter = controllerMethodConverter;
        return this;
    }

    public Class<? extends IResourceAssemblerConverter> getResourceAssemblerConverter() {
        return resourceAssemblerConverter;
    }

    public SchemaConverterList setResourceAssemblerConverter(Class<? extends IResourceAssemblerConverter> resourceAssemblerConverter) {
        this.resourceAssemblerConverter = resourceAssemblerConverter;
        return this;
    }

    public Class<? extends IDAOConverter> getDaoConverter() {
        return daoConverter;
    }

    public SchemaConverterList setDaoConverter(Class<? extends IDAOConverter> daoConverter) {
        this.daoConverter = daoConverter;
        return this;
    }

    public Class<? extends IServiceConverter> getServiceConverter() {
        return serviceConverter;
    }

    public SchemaConverterList setServiceConverter(Class<? extends IServiceConverter> serviceConverter) {
        this.serviceConverter = serviceConverter;
        return this;
    }

    public Class<? extends IEntityClassConverter> getEntityClassConverter() {
        return entityClassConverter;
    }

    public SchemaConverterList setEntityClassConverter(Class<? extends IEntityClassConverter> entityClassConverter) {
        this.entityClassConverter = entityClassConverter;
        return this;
    }

    public Class<? extends IResourceClassConverter> getResourceClassConverter() {
        return resourceClassConverter;
    }

    public SchemaConverterList setResourceClassConverter(Class<? extends IResourceClassConverter> resourceClassConverter) {
        this.resourceClassConverter = resourceClassConverter;
        return this;
    }
}
