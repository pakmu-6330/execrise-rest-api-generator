package cn.dyr.rest.generator.util;

import cn.dyr.rest.generator.java.meta.ModifierInfo;
import cn.dyr.rest.generator.java.meta.parameters.Parameter;

/**
 * 这个类用于进行数据的克隆
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class CloneUtils {

    /**
     * 克隆一个修饰符对象
     *
     * @param modifierInfo 要进行克隆的修饰符对象
     * @return 克隆后的修饰符对象
     */
    public static ModifierInfo cloneModifier(ModifierInfo modifierInfo) {
        return modifierInfo.copy();
    }

    /**
     * 不考虑注解列表对参数信息进行克隆
     *
     * @param parameter 原始的参数数据
     * @return 克隆后的不带注解的参数
     */
    public static Parameter cloneParameterWithoutAnnotations(Parameter parameter) {
        return new Parameter()
                .setName(parameter.getName())
                .setTypeInfo(parameter.getTypeInfo());
    }

}
