package cn.dyr.rest.generator.framework.spring.hateoas;

import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.TypeInfo;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;

/**
 * Spring HATEOAS 框架相关对象的工厂
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringHATEOASTypeFactory {

    /**
     * 创建一个 ResourceSupport 类型
     *
     * @return ResourceSupport 类型对象
     */
    public static TypeInfo resourceSupportType() {
        return TypeInfoFactory.fromClass(SpringHATEOASConstant.RESOURCE_SUPPORT_CLASS);
    }

    /**
     * 创建一个 ResourceAssembler 类
     *
     * @param entityType   实体类型
     * @param resourceType 实体对应的 HATEOAS 资源类型
     * @return 含有上面两个类型泛型信息的 ResourceAssembler对象
     */
    public static TypeInfo resourceAssemblerSupport(TypeInfo entityType, TypeInfo resourceType) {
        TypeInfo baseTypeInfo = TypeInfoFactory.fromClass(SpringHATEOASConstant.RESOURCE_ASSEMBLER_SUPPORT_CLASS);
        return TypeInfoFactory.wrapGenerics(baseTypeInfo, new TypeInfo[]{entityType, resourceType});
    }

    /**
     * 创建一个 Link 类型
     *
     * @return Link 类型对象
     */
    public static TypeInfo linkType() {
        return TypeInfoFactory.fromClass(SpringHATEOASConstant.RESOURCE_LINK_CLASS);
    }

    /**
     * 创建一个 ControllerLinkBuilder 类型
     *
     * @return ControllerLinkBuilder 类型
     */
    public static TypeInfo controllerLinkBuilderType() {
        return TypeInfoFactory.fromClass(SpringHATEOASConstant.CONTROLLER_LINK_BUILDER_CLASS);
    }
}
