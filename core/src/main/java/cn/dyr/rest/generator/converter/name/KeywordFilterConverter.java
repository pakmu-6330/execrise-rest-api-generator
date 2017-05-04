package cn.dyr.rest.generator.converter.name;

import cn.dyr.rest.generator.converter.ConvertDataContext;
import cn.dyr.rest.generator.converter.ConverterConfig;
import cn.dyr.rest.generator.converter.ConverterInject;
import cn.dyr.rest.generator.converter.ConverterInjectType;
import cn.dyr.rest.generator.converter.DataInject;
import cn.dyr.rest.generator.converter.DataInjectType;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.util.ClassInfoUtils;
import cn.dyr.rest.generator.util.StringUtils;
import net.oschina.util.Inflector;

import static cn.dyr.rest.generator.util.StringUtils.lowerFirstLatter;
import static cn.dyr.rest.generator.util.StringUtils.upperFirstLatter;

/**
 * 带有关键字过滤功能的名称转换器
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class KeywordFilterConverter extends BasicNameConverter {

    @DataInject(DataInjectType.CONFIG)
    private ConverterConfig config;

    @DataInject(DataInjectType.DATA_CONTEXT)
    private ConvertDataContext convertDataContext;

    private KeywordHolder keywordHolder;

    public KeywordFilterConverter() {
        this.keywordHolder = KeywordHolder.getHolder();
    }

    @Override
    public String classNameFromEntityName(String entityName) {
        if (keywordHolder.isKeyword(entityName)) {
            return String.format("%sEntity", upperFirstLatter(entityName));
        } else {
            return upperFirstLatter(entityName);
        }
    }

    @Override
    public String fieldNameFromAttributeName(String attributeName) {
        if (keywordHolder.isKeyword(attributeName)) {
            return String.format("%sField", lowerFirstLatter(attributeName));
        } else {
            return lowerFirstLatter(attributeName);
        }
    }

    @Override
    public String tableNameFromEntityName(String entityName) {
        String rawTableName = lowerFirstLatter(entityName);

        return String.format("%s%s", config.getTablePrefix(), rawTableName);
    }

    @Override
    public String daoMethodNameFindByAnotherID(String relatedName, String idFieldName) {
        return String.format("findBy%s%s",
                StringUtils.upperFirstLatter(Inflector.getInstance().singularize(relatedName)),
                StringUtils.upperFirstLatter(idFieldName));
    }

    @Override
    public String reversedDAOQueryMethodName(ConvertDataContext.RelationshipHandler handler) {
        String handledEntityName = handler.getToBeHandled();
        ClassInfo handledEntityClass = this.convertDataContext.getClassByEntityAndType(handledEntityName, ConvertDataContext.TYPE_ENTITY_CLASS);
        FieldInfo handledIdField = ClassInfoUtils.findSingleId(handledEntityClass);
        return daoMethodNameFindByAnotherID(handler.getHandlerFieldName(), handledIdField.getName());
    }
}
