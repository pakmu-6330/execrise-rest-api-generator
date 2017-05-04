package cn.dyr.rest.generator.converter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;

/**
 * 表示一个转换器的配置
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConverterConfig {

    private static Logger logger;

    static {
        logger = LoggerFactory.getLogger(ConverterConfig.class);
    }

    private String nameConverterClass;
    private String typeConverterClass;
    private String fieldConverterClass;

    private String entityConverterClass;

    private String daoConverterClass;

    private String serviceInstructionConverterClass;
    private String serviceConverterClass;

    private String controllerConverterClass;
    private String controllerMethodConverterClass;

    private String hateoasResourceConverterClass;
    private String hateoasResourceAssemblerConverterClass;

    private String poPackageName;
    private String daoPackageName;
    private String servicePackageName;
    private String controllerPackageName;
    private String resourceClassPackageName;
    private String resourceAssemblerPackageName;
    private String commonPackageName;
    private String exceptionPackageName;

    private String tablePrefix;
    private String uriPrefix;

    private int defaultPageSize;

    private boolean builderStyleSetter;
    private boolean pagingEnabled;

    /**
     * 创建一个携带有默认配置的配置
     */
    public ConverterConfig() {
        this.nameConverterClass = "cn.dyr.rest.generator.converter.name.KeywordFilterConverter";
        this.typeConverterClass = "cn.dyr.rest.generator.converter.type.DefaultTypeConverter";
        this.fieldConverterClass = "cn.dyr.rest.generator.converter.field.DefaultFieldConverter";
        this.serviceInstructionConverterClass = "cn.dyr.rest.generator.converter.instruction.DefaultServiceInstructionConverter";
        this.controllerConverterClass = "cn.dyr.rest.generator.converter.clazz.DefaultControllerConverter";
        this.controllerMethodConverterClass = "cn.dyr.rest.generator.converter.method.DefaultControllerMethodConverter";
        this.hateoasResourceAssemblerConverterClass = "cn.dyr.rest.generator.converter.clazz.DefaultResourceAssemblerConverter";
        this.daoConverterClass = "cn.dyr.rest.generator.converter.clazz.DefaultDAOConverter";
        this.serviceConverterClass = "cn.dyr.rest.generator.converter.clazz.DefaultServiceConverter";
        this.entityConverterClass = "cn.dyr.rest.generator.converter.clazz.DefaultEntityClassConverter";
        this.hateoasResourceConverterClass = "cn.dyr.rest.generator.converter.clazz.DefaultResourceClassConverter";

        this.poPackageName = "po";
        this.daoPackageName = "dao";
        this.servicePackageName = "package";
        this.controllerPackageName = "controller";
        this.resourceClassPackageName = "hateoas.resource";
        this.resourceAssemblerPackageName = "hateoas.assembler";
        this.commonPackageName = "common";
        this.exceptionPackageName = "exception";

        this.tablePrefix = "";

        this.defaultPageSize = 5;

        this.builderStyleSetter = false;
        this.pagingEnabled = true;
    }

    /**
     * 获得名称转换器类的类名
     *
     * @return 名称转换器类的类名
     */
    public String getNameConverterClass() {
        return nameConverterClass;
    }

    /**
     * 设置名称转换器的类名
     *
     * @param nameConverterClass 名称转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setNameConverterClass(String nameConverterClass) {
        this.nameConverterClass = nameConverterClass;
        return this;
    }

    /**
     * 获得类型转换器的类名
     *
     * @return 这个配置信息本身
     */
    public String getTypeConverterClass() {
        return typeConverterClass;
    }

    /**
     * 设置类型转换器的类名
     *
     * @param typeConverterClass 类型转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setTypeConverterClass(String typeConverterClass) {
        this.typeConverterClass = typeConverterClass;
        return this;
    }

    /**
     * 获得字段转换器的类名
     *
     * @return 字段转换器的类名
     */
    public String getFieldConverterClass() {
        return fieldConverterClass;
    }

    /**
     * 设置字段转换器的类名
     *
     * @param fieldConverterClass 字段转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setFieldConverterClass(String fieldConverterClass) {
        this.fieldConverterClass = fieldConverterClass;
        return this;
    }

    /**
     * 获得指令转换器的类名
     *
     * @return 指令转换器的类名
     */
    public String getServiceInstructionConverterClass() {
        return serviceInstructionConverterClass;
    }

    /**
     * 设置指令转换器的类名
     *
     * @param serviceInstructionConverterClass 指令转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setServiceInstructionConverterClass(String serviceInstructionConverterClass) {
        this.serviceInstructionConverterClass = serviceInstructionConverterClass;
        return this;
    }

    /**
     * 获得 HATEOAS 资源装配类转换器的类名
     *
     * @return HATEOAS 资源装配类转换器的类名
     */
    public String getHateoasResourceAssemblerConverterClass() {
        return hateoasResourceAssemblerConverterClass;
    }

    /**
     * 设置 HATEOAS 资源装配类转换器的类名
     *
     * @param hateoasResourceAssemblerConverterClass HATEOAS 资源装配类的类名
     * @return 配置信息本身额
     */
    public ConverterConfig setHateoasResourceAssemblerConverterClass(String hateoasResourceAssemblerConverterClass) {
        this.hateoasResourceAssemblerConverterClass = hateoasResourceAssemblerConverterClass;
        return this;
    }

    /**
     * 获得控制器方法转换类的类名
     *
     * @return 控制器方法转换类的类名
     */
    public String getControllerMethodConverterClass() {
        return controllerMethodConverterClass;
    }

    /**
     * 设置控制器方法转换类的类名
     *
     * @param controllerMethodConverterClass 控制其方法转换类的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setControllerMethodConverterClass(String controllerMethodConverterClass) {
        this.controllerMethodConverterClass = controllerMethodConverterClass;
        return this;
    }

    /**
     * 获得 DAO 类元信息转换器的类名
     *
     * @return DAO 类元信息转换器的类名
     */
    public String getDaoConverterClass() {
        return daoConverterClass;
    }

    /**
     * 设置 DAO 类元信息转换器的类名
     *
     * @param daoConverterClass DAO 类元信息转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setDaoConverterClass(String daoConverterClass) {
        this.daoConverterClass = daoConverterClass;
        return this;
    }

    /**
     * 获得 Service 类元信息转换器的类名
     *
     * @return Service 类元信息转换器的类名
     */
    public String getServiceConverterClass() {
        return serviceConverterClass;
    }

    /**
     * 设置 Service 类元信息转换器的类名
     *
     * @param serviceConverterClass Service 类元信息转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setServiceConverterClass(String serviceConverterClass) {
        this.serviceConverterClass = serviceConverterClass;
        return this;
    }

    /**
     * 获得 Controller 类转换器的类名
     *
     * @return Controller 类转换器的类名
     */
    public String getControllerConverterClass() {
        return controllerConverterClass;
    }

    /**
     * 设置 Controller 类转换器的类名
     *
     * @param controllerConverterClass 转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setControllerConverterClass(String controllerConverterClass) {
        this.controllerConverterClass = controllerConverterClass;
        return this;
    }

    /**
     * 获得实体类转换器的类名
     *
     * @return 实体类转换器的类名
     */
    public String getEntityConverterClass() {
        return entityConverterClass;
    }

    /**
     * 设置实体类转换器的类名
     *
     * @param entityConverterClass 实体类转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setEntityConverterClass(String entityConverterClass) {
        this.entityConverterClass = entityConverterClass;
        return this;
    }

    /**
     * 获得 HATEOAS 资源类转换器的类名
     *
     * @return HATEOAS 资源类转换器的类名
     */
    public String getHateoasResourceConverterClass() {
        return hateoasResourceConverterClass;
    }

    /**
     * 设置 HATEOAS 资源类转换器的类名
     *
     * @param hateoasResourceConverterClass HATEOAS 资源转换器的类名
     * @return 这个配置信息本身
     */
    public ConverterConfig setHateoasResourceConverterClass(String hateoasResourceConverterClass) {
        this.hateoasResourceConverterClass = hateoasResourceConverterClass;
        return this;
    }

    /**
     * 获得 PO 类所在包的包名
     *
     * @return PO 类所在包的包名
     */
    public String getPoPackageName() {
        return poPackageName;
    }

    /**
     * 设置 PO 类所在包的包名
     *
     * @param poPackageName PO 类所在包的包名
     * @return 这个配置本身
     */
    public ConverterConfig setPoPackageName(String poPackageName) {
        this.poPackageName = poPackageName;
        return this;
    }

    /**
     * 获得 DAO 类所在包的包名
     *
     * @return DAO 类所在包的包名
     */
    public String getDaoPackageName() {
        return daoPackageName;
    }

    /**
     * 设置 DAO 类所在包的包名
     *
     * @param daoPackageName DAO 类所在包的包名
     * @return 这个配置本身
     */
    public ConverterConfig setDaoPackageName(String daoPackageName) {
        this.daoPackageName = daoPackageName;
        return this;
    }

    /**
     * 获得 Service 类所在包的包名
     *
     * @return Service 类所在包的包名
     */
    public String getServicePackageName() {
        return servicePackageName;
    }

    /**
     * 设置 Service 类所在包的包名
     *
     * @param servicePackageName Service 类所在包的包名
     * @return 配置对象本身
     */
    public ConverterConfig setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
        return this;
    }

    /**
     * 获得 Controller 类所在包的包名
     *
     * @return Controller 类所在包的包名
     */
    public String getControllerPackageName() {
        return controllerPackageName;
    }

    /**
     * 设置 Controller 类所在包的包名
     *
     * @param controllerPackageName Controller 所在包的包名
     * @return 配置信息本身
     */
    public ConverterConfig setControllerPackageName(String controllerPackageName) {
        this.controllerPackageName = controllerPackageName;
        return this;
    }

    /**
     * 获得 HATEOAS 资源类的包名
     *
     * @return HATEOAS 资源类的包名
     */
    public String getResourceClassPackageName() {
        return resourceClassPackageName;
    }

    /**
     * 设置 HATEOAS 资源类的包名
     *
     * @param resourceClassPackageName HATEOAS 资源类的包名
     */
    public void setResourceClassPackageName(String resourceClassPackageName) {
        this.resourceClassPackageName = resourceClassPackageName;
    }

    /**
     * 获得 HATEOAS 资源组装类的包名
     *
     * @return HATEOAS 资源组装类所在包的包名
     */
    public String getResourceAssemblerPackageName() {
        return resourceAssemblerPackageName;
    }

    /**
     * 设置 HATEOAS 资源组装类所在包的包名
     *
     * @param resourceAssemblerPackageName HATEOAS 资源组装类所在包的包名
     */
    public void setResourceAssemblerPackageName(String resourceAssemblerPackageName) {
        this.resourceAssemblerPackageName = resourceAssemblerPackageName;
    }

    /**
     * 获得用于存放公用类的包的包名
     *
     * @return 用于存放公用类包的包名
     */
    public String getCommonPackageName() {
        return commonPackageName;
    }

    /**
     * 设置用于存放公用类包的包名
     *
     * @param commonPackageName 用于存放公用类包的包名
     * @return 配置信息对象本身
     */
    public ConverterConfig setCommonPackageName(String commonPackageName) {
        this.commonPackageName = commonPackageName;
        return this;
    }

    /**
     * 获得用于存放异常对象的包名
     *
     * @return 存放异常对象的包名
     */
    public String getExceptionPackageName() {
        return exceptionPackageName;
    }

    /**
     * 设置异常对象存放包的包名
     *
     * @param exceptionPackageName 异常对象存放的包名
     * @return 这个配置本身
     */
    public ConverterConfig setExceptionPackageName(String exceptionPackageName) {
        this.exceptionPackageName = exceptionPackageName;
        return this;
    }

    /**
     * 判断是否启用分页功能
     *
     * @return 一个表示分页功能是否启动的布尔值
     */
    public boolean isPagingEnabled() {
        return pagingEnabled;
    }

    /**
     * 设置生成的 DAO 是否启用分页功能
     *
     * @param pagingEnabled 生成的 DAO 是否启动分页功能
     * @return 配置本身
     */
    public ConverterConfig setPagingEnabled(boolean pagingEnabled) {
        this.pagingEnabled = pagingEnabled;
        return this;
    }

    /**
     * 获得表名前缀
     *
     * @return 表名前缀
     */
    public String getTablePrefix() {
        return tablePrefix;
    }

    /**
     * 设置表前缀
     *
     * @param tablePrefix 要设置的表前缀
     * @return 这个配置对象本身
     */
    public ConverterConfig setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    /**
     * 是否采用 Builder 风格生成类的 setter 方法
     *
     * @return 表示是否采用 builder 风格生成 setter 方法的布尔值
     */
    public boolean isBuilderStyleSetter() {
        return builderStyleSetter;
    }

    /**
     * 设置是否在采用 Builder 风格生成 setter 方法
     *
     * @param builderStyleSetter 表示是否采用 builder 风格生成 setter 方法的布尔值
     * @return 这个配置信息类本身
     */
    public ConverterConfig setBuilderStyleSetter(boolean builderStyleSetter) {
        this.builderStyleSetter = builderStyleSetter;
        return this;
    }

    /**
     * 获得 uri 前缀
     *
     * @return uri 前缀
     */
    public String getUriPrefix() {
        return uriPrefix;
    }

    /**
     * 设置 uri 前缀
     *
     * @param uriPrefix uri 前缀
     * @return 配置对象本身
     */
    public ConverterConfig setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
        return this;
    }

    /**
     * 获得默认的分页大小
     *
     * @return 表示默认分页大小的整型值
     */
    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    /**
     * 设置默认分页的大小
     *
     * @param defaultPageSize 默认分页的大小整型值
     * @return 配置对象本身
     */
    public ConverterConfig setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
        return this;
    }

    /**
     * 获得一个默认的配置
     *
     * @return 默认的配置
     */
    public static ConverterConfig getDefault() {
        return new ConverterConfig();
    }

    /**
     * 将字符串转换成布尔值
     *
     * @param value 要转换成布尔值的字符串
     * @return 这个字符串对应的布尔值
     * @throws IllegalArgumentException 如果传入的字符串为空或者不为表示布尔值的字符串，则会抛出这个异常
     */
    private static boolean booleanFromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }

        value = value.toLowerCase();
        if (value.equals("true")) {
            return true;
        } else if (value.equals("false")) {
            return false;
        } else {
            throw new IllegalArgumentException(String.format("failed to convert %s to boolean", value));
        }
    }

    /**
     * 将字符串转换为整型值
     *
     * @param value 要转换的字符串
     * @return 这个字符串对应的整型值
     */
    private static int intFromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("value cannot be converted into number");
        }
    }

    /**
     * 从外部的配置文件中读取相应的配置信息
     *
     * @param inputStream 用于读入配置文件的流对象
     * @return 这个配置文件里面的配置信息
     */
    public static ConverterConfig fromFile(InputStream inputStream) {
        ConverterConfig retValue = null;
        SAXReader reader = new SAXReader();

        try {
            Document document = reader.read(inputStream);
            retValue = new ConverterConfig();

            DefaultElement configNode = (DefaultElement) document.selectSingleNode("/converter-config");

            // 转换器的配置信息
            Node convertersNode = configNode.selectSingleNode("./converters");
            List converterElements = ((DefaultElement) convertersNode).elements();
            for (Object converterNode : converterElements) {
                if (converterNode instanceof DefaultElement) {
                    DefaultElement converterElement = (DefaultElement) converterNode;
                    String nodeName = converterElement.getName();
                    String converterClassName = converterElement.getStringValue().trim();

                    switch (nodeName) {
                        case "name-converter":
                            retValue.setNameConverterClass(converterClassName);
                            break;
                        case "type-converter":
                            retValue.setTypeConverterClass(converterClassName);
                            break;
                        case "field-converter":
                            retValue.setFieldConverterClass(converterClassName);
                            break;
                        case "instruction-converter":
                            retValue.setServiceInstructionConverterClass(converterClassName);
                            break;
                        case "controller-method-converter":
                            retValue.setControllerMethodConverterClass(converterClassName);
                            break;
                        case "hateoas-assembler-class-converter":
                            retValue.setHateoasResourceAssemblerConverterClass(converterClassName);
                            break;
                        case "dao-class-converter":
                            retValue.setDaoConverterClass(converterClassName);
                            break;
                        case "service-class-converter":
                            retValue.setServiceConverterClass(converterClassName);
                            break;
                        case "controller-class-converter":
                            retValue.setControllerConverterClass(converterClassName);
                            break;
                        case "entity-class-converter":
                            retValue.setEntityConverterClass(converterClassName);
                            break;
                        case "hateoas-resource-class-converter":
                            retValue.setHateoasResourceConverterClass(converterClassName);
                            break;
                    }
                }
            }

            // 转换器的包名配置信息
            Node packagesNode = configNode.selectSingleNode("./packages");
            List packageElements = ((DefaultElement) packagesNode).elements();
            for (Object packageNode : packageElements) {
                if (packageNode instanceof DefaultElement) {
                    DefaultElement packageElement = (DefaultElement) packageNode;
                    String packageName = packageElement.getText();
                    String nodeName = packageElement.getName();

                    switch (nodeName) {
                        case "po-package":
                            retValue.setPoPackageName(packageName);
                            break;
                        case "dao-package":
                            retValue.setDaoPackageName(packageName);
                            break;
                        case "service-package":
                            retValue.setServicePackageName(packageName);
                            break;
                        case "controller-package":
                            retValue.setControllerPackageName(packageName);
                            break;
                        case "resource-class-package":
                            retValue.setResourceClassPackageName(packageName);
                            break;
                        case "resource-assembler-package":
                            retValue.setResourceAssemblerPackageName(packageName);
                            break;
                        case "common":
                            retValue.setCommonPackageName(packageName);
                            break;
                        case "exception-package":
                            retValue.setExceptionPackageName(packageName);
                            break;
                    }
                }
            }

            // 获得一些参数的配置信息
            Node parameterNode = configNode.selectSingleNode("./parameters");
            if (parameterNode != null) {
                List parameterNodeList = ((DefaultElement) parameterNode).elements();
                for (Object rawParameterNode : parameterNodeList) {
                    if (rawParameterNode instanceof DefaultElement) {
                        DefaultElement parameterElement = (DefaultElement) rawParameterNode;
                        String text = parameterElement.getText();
                        String key = parameterElement.getName();

                        switch (key) {
                            case "table-prefix":
                                retValue.setTablePrefix(text);
                                break;
                            case "builder-style-setter":
                                retValue.setBuilderStyleSetter(booleanFromString(text));
                                break;
                            case "paging-enabled":
                                retValue.setPagingEnabled(booleanFromString(text));
                                break;
                            case "uri-prefix":
                                retValue.setUriPrefix(text);
                                break;
                            case "default-page-size":
                                retValue.setDefaultPageSize(intFromString(text));
                                break;
                        }
                    }
                }
            }

        } catch (DocumentException e) {
            logger.warn("failed to parse XML config, using default config", e);
            retValue = new ConverterConfig();
        }

        return retValue;
    }
}
