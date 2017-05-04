package cn.dyr.rest.generator.io;

import cn.dyr.rest.generator.converter.ConverterContext;
import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.entity.factory.AttributeInfoFactory;
import cn.dyr.rest.generator.java.generator.DefaultCodeGenerator;
import cn.dyr.rest.generator.java.meta.ClassInfo;
import cn.dyr.rest.generator.project.Project;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * 测试代码写入到文件是否正常
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SourceWriterTest {

    @Test
    public void testWrite() throws IOException {
        Project project = new Project();
        project.setBasePackage("cn.dyr.test");

        ConverterContext converterContext = new ConverterContext(project);
        SourceWriter writer = new SourceWriter();
        DefaultCodeGenerator generator = new DefaultCodeGenerator();

        writer.init(new File("F:\\rest-test"));

        EntityInfo bookEntity = createBookEntity();
        converterContext.addEntityInfo(bookEntity);

        EntityInfo personEntity = createPersonEntity();
        converterContext.addEntityInfo(personEntity);

        EntityInfo publisherEntity = createPublisherEntity();
        converterContext.addEntityInfo(publisherEntity);

        EntityRelationship authorRelationship = new EntityRelationship()
                .setAToB()
                .setEndA(bookEntity)
                .setEndB(personEntity)
                .setEndAAttributeName("authors")
                .setType(RelationshipType.MANY_TO_MANY);
        converterContext.addEntityRelationship(authorRelationship);

        EntityRelationship publisherRelationship = new EntityRelationship()
                .setAToB()
                .setBToA()
                .setEndA(bookEntity)
                .setEndB(publisherEntity)
                .setEndAAttributeName("publisher")
                .setType(RelationshipType.MANY_TO_ONE)
                .setRelationHandler(bookEntity);
        converterContext.addEntityRelationship(publisherRelationship);

        converterContext.generate();

        Iterator<ClassInfo> classInfoIterator = converterContext.iterateClass();
        while (classInfoIterator.hasNext()) {
            ClassInfo classInfo = classInfoIterator.next();

            writer.writeSource(classInfo, generator.generateSingleClass(classInfo));
        }
    }

    public static EntityInfo createPublisherEntity() {
        EntityInfo publisherEntity = new EntityInfo();
        publisherEntity.setName("Publisher");
        publisherEntity.addAttribute(AttributeInfoFactory.createLongId("id"));
        publisherEntity.addAttribute(AttributeInfoFactory.varString("name"));
        publisherEntity.addAttribute(AttributeInfoFactory.varString("location"));
        publisherEntity.addAttribute(AttributeInfoFactory.varString("telephone"));
        return publisherEntity;
    }

    public static EntityInfo createPersonEntity() {
        EntityInfo personEntity = new EntityInfo();
        personEntity.setName("Person");
        personEntity.addAttribute(AttributeInfoFactory.createLongId("id"));
        personEntity.addAttribute(AttributeInfoFactory.varString("name"));
        return personEntity;
    }

    public static EntityInfo createBookEntity() {
        EntityInfo bookEntity = new EntityInfo();
        bookEntity.setName("Book");
        bookEntity.addAttribute(AttributeInfoFactory.createLongId("id"));
        bookEntity.addAttribute(AttributeInfoFactory.fixedString("name", 255).setAsSelectCondition(true));
        bookEntity.addAttribute(AttributeInfoFactory.floatAttribute("price"));
        bookEntity.addAttribute(AttributeInfoFactory.varString("isbn"));
        bookEntity.addAttribute(AttributeInfoFactory.varString("category"));
        bookEntity.addAttribute(AttributeInfoFactory.dateTimeAttribute("publishDate"));
        return bookEntity;
    }
}