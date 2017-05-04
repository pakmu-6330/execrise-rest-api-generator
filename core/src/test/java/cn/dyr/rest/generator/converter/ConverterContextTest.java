package cn.dyr.rest.generator.converter;

import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.factory.AttributeInfoFactory;
import cn.dyr.rest.generator.java.generator.DefaultCodeGenerator;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.project.Project;
import org.junit.Test;

import java.util.Iterator;

/**
 * 测试数据分析模块
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ConverterContextTest {

    @Test
    public void testConvert() {
        Project project = new Project();
        project.setBasePackage("cn.dyr.rest.generator.test");

        DefaultCodeGenerator generator = new DefaultCodeGenerator();
        ConverterContext converterContext = new ConverterContext(project);

        EntityInfo bookEntity = new EntityInfo();
        bookEntity.setName("Book");
        bookEntity.addAttribute(AttributeInfoFactory.createLongId("id"));
        bookEntity.addAttribute(AttributeInfoFactory.fixedString("name", 255));
        bookEntity.addAttribute(AttributeInfoFactory.floatAttribute("price"));

        converterContext.addEntityInfo(bookEntity);

        converterContext.generate();

        Iterator<ClassInfo> classInfoIterator = converterContext.iterateClass();
        while (classInfoIterator.hasNext()) {
            ClassInfo classInfo = classInfoIterator.next();

            String code = generator.generateSingleClass(classInfo);
            System.out.println(code);
        }
    }

}