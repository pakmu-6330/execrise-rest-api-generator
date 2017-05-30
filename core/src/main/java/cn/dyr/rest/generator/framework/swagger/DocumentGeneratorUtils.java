package cn.dyr.rest.generator.framework.swagger;

/**
 * 这个类当中封装了用于产生文档当中字符串的各种方法
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class DocumentGeneratorUtils {

    /**
     * HTTP GET!!! 没有任何参数
     *
     * @param entityName 实体名称
     * @return 对应的字符串
     */
    public static String listGotten(String entityName) {
        return String.format("获得%s列表", entityName);
    }

    /**
     * 获得 Controller 的信息
     *
     * @param entityName 实体名称
     * @return 用于 Controller 级别的描述字符串
     */
    public static String getApiDocument(String entityName) {
        return String.format("%s相关操作", entityName);
    }

    /**
     * 获得通过唯一标识符获取对象的提示方法
     *
     * @param entityName 实体名称
     * @return 通过唯一标识符获得实体对象方法的接口说明文字
     */
    public static String getById(String entityName) {
        return String.format("根据唯一标识符获得%s", entityName);
    }

    /**
     * 未找到满足条件的对象时的说明文字
     *
     * @param entityName 实体名称
     * @return 未找到满足条件对象的接口说明文字
     */
    public static String notFound(String entityName) {
        return String.format("未找到指定的%s", entityName);
    }

    /**
     * 表示成功获得的说明文字
     *
     * @param entityName 实体名字
     * @return 成功找到满足条件数据的接口说明文字
     */
    public static String gotten(String entityName) {
        return String.format("成功获得%s", entityName);
    }

    /**
     * 返回创建新对象的说明文字
     *
     * @param entityName 实体文字
     * @return 成功创建对象的接口说明文字
     */
    public static String created(String entityName) {
        return String.format("创建一个%s", entityName);
    }

    /**
     * 返回成功创建了一个新对象的说明文字
     *
     * @param entityName 实体文字
     * @return 对象创建成功的接口说明文字
     */
    public static String createdSuccess(String entityName) {
        return String.format("成功了创建一个%s", entityName);
    }

    /**
     * 创建保存或者创建实体对象的接口说明文字
     *
     * @param entityName 进行操作的实体对象名
     * @return 保存或创建实体对象的接口说明文字
     */
    public static String updateOrCreate(String entityName) {
        return String.format("创建或者变更%s", entityName);
    }

    /**
     * 获得一个修改成功的接口说明文字
     *
     * @param entityName 进行操作的实体对象名
     * @return 修改成功以后接口的说明文字
     */
    public static String updated(String entityName) {
        return String.format("成功修改了%s", entityName);
    }

    /**
     * 根据唯一标识符删除实体对象的接口说明文字
     *
     * @param entityName 进行操作的实体对象名
     * @return 根据唯一标识符删除实体对象接口的说明文字
     */
    public static String deletedById(String entityName) {
        return String.format("根据唯一标示符删除%s", entityName);
    }

    /**
     * 删除成功的接口说明文字
     *
     * @param entityName 进行操作的实体对象名
     * @return 删除成功的接口说明文字
     */
    public static String deleteSuccess(String entityName) {
        return String.format("成功删除指定的%s", entityName);
    }

    /**
     * 获得关联对象的接口说明文字
     *
     * @param entityName        实体名称
     * @param relatedEntityName 关联的实体名称
     * @return 获得关联对象的接口说明文字
     */
    public static String getRelatedEntityInfo(String entityName, String relatedEntityName) {
        return String.format("获得指定%s的%s", entityName, relatedEntityName);
    }

    /**
     * 获得尝试删除一个被其他对象引用的对象的错误接口说明文字
     *
     * @param entityName 实体名称
     * @return 获得删除一个被引用的对象错误的说明文字
     */
    public static String getRelatedEntityExists(String entityName) {
        return String.format("这个%s被其他对象引用，不能删除", entityName);
    }

    /**
     * 获得在某个对象持有的另外一方对象的集合中添加一个对象
     *
     * @param item       要添加的对象
     * @param parentItem 持有这个对象集合的对象
     * @return 对应的说明文字
     */
    public static String createSthInSpecifiedSth(String item, String parentItem) {
        return String.format("在指定的%s中创建一个%s", parentItem, item);
    }
}
