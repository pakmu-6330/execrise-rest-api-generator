package cn.dyr.rest.generator.java.generator;

import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.java.meta.FieldInfo;
import cn.dyr.rest.generator.java.meta.MethodInfo;
import cn.dyr.rest.generator.java.meta.factory.MethodInfoFactory;
import cn.dyr.rest.generator.java.meta.factory.TypeInfoFactory;
import org.junit.Test;

/**
 * 测试 po 类的生成
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class BeanClassGenerationTest {

    @Test
    public void testBuilderGeneration() {
        ClassInfo classInfo = new ClassInfo()
                .setClassName("Book")
                .setPackageName("cn.dyr.library.management.po")
                .setPublic();

        FieldInfo idField = new FieldInfo()
                .setPrivate()
                .setName("id")
                .setType(TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG));

        MethodInfo getIdMethod = MethodInfoFactory.getter(idField);
        MethodInfo setIdMethod = MethodInfoFactory.builderSetter(idField, classInfo.getType());

        classInfo.addField(idField)
                .addMethod(getIdMethod)
                .addMethod(setIdMethod);

        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        String code = generator.generateSingleClass(classInfo);
        System.out.println(code);
    }

    @Test
    public void testGeneration() {
        ClassInfo classInfo = new ClassInfo()
                .setClassName("Book")
                .setPackageName("cn.dyr.library.management.po")
                .setPublic();

        FieldInfo idField = new FieldInfo()
                .setPrivate()
                .setName("id")
                .setType(TypeInfoFactory.primitive(TypeInfoFactory.PRIMITIVE_LONG));

        MethodInfo getIdMethod = MethodInfoFactory.getter(idField);
        MethodInfo setIdMethod = MethodInfoFactory.setter(idField);

        classInfo.addField(idField)
                .addMethod(getIdMethod)
                .addMethod(setIdMethod);

        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        String code = generator.generateSingleClass(classInfo);
        System.out.println(code);
    }

}
