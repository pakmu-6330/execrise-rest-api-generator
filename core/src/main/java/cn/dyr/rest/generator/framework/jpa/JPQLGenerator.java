package cn.dyr.rest.generator.framework.jpa;

import cn.dyr.rest.generator.util.StringUtils;

/**
 * 用于生成 JPQL 查询语句的工具类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPQLGenerator {

    /**
     * 创建根据另外一方查询本实体对象的 JPQL
     *
     * @param thisEntityName       本实体的名称
     * @param relatedEntityName    关联实体的名称
     * @param relatedEntityField   本实体到关联实体关系的字段名称
     * @param relatedEntityIdField 关联实体的唯一标识符的字段名称
     * @return 对应的 JPQL
     */
    public static String getByAnotherEntityInRelationship(
            String thisEntityName, String relatedEntityName,
            String relatedEntityField, String relatedEntityIdField
    ) {
        String thisNameAlias = StringUtils.lowerFirstLatter(thisEntityName);
        if (thisNameAlias.equals(thisEntityName)) {
            thisNameAlias = thisNameAlias + "alias";
        }

        String relatedNameAlias = StringUtils.lowerFirstLatter(relatedEntityName);
        if (relatedNameAlias.equals(relatedEntityName)) {
            relatedNameAlias = relatedNameAlias + "alias";
        }

        return String.format("select %s from %s %s inner join %s.%s %s where %s.%s=?1",
                thisNameAlias, thisEntityName, thisNameAlias,
                thisNameAlias, relatedEntityField, relatedNameAlias,
                relatedNameAlias, relatedEntityIdField);
    }

}
