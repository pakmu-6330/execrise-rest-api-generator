package cn.dyr.rest.generator.project;

import cn.dyr.rest.generator.entity.EntityInfo;
import cn.dyr.rest.generator.entity.EntityRelationship;
import cn.dyr.rest.generator.entity.RelationshipType;
import cn.dyr.rest.generator.io.SourceWriterTest;
import cn.dyr.rest.generator.project.database.JdbcConfigFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * 用于对项目级别的代码生成环境进行测试的测试用例
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class ProjectGenerationContextTest {

    @Test
    public void testProjectGeneration() throws IOException {
        ProjectGenerationContext context = new ProjectGenerationContext();

        Project project = new Project()
                .setProjectName("book")
                .setBasePackage("cn.dyr.book")
                .setAuthor("dyr")
                .setVersion("0.1.0001")
                .setJdbcConfig(
                        JdbcConfigFactory.mySql("localhost", "spring", "root", "popkart.alex9498"));
        context.setProject(project);

        EntityInfo personEntity = SourceWriterTest.createPersonEntity();
        EntityInfo bookEntity = SourceWriterTest.createBookEntity();
        EntityInfo publisherEntity = SourceWriterTest.createPublisherEntity();

        EntityRelationship authorRelationship = new EntityRelationship()
                .setAToB()
                .setEndA(bookEntity)
                .setEndB(personEntity)
                .setEndAAttributeName("authors")
                .setType(RelationshipType.MANY_TO_MANY)
                .setRelationHandler(bookEntity);
        EntityRelationship translatorsRelationship = new EntityRelationship()
                .setAToB()
                .setEndA(bookEntity)
                .setEndB(personEntity)
                .setEndAAttributeName("translators")
                .setType(RelationshipType.MANY_TO_MANY)
                .setRelationHandler(bookEntity);
        EntityRelationship publisherRelationship = new EntityRelationship()
                .setAToB()
                .setBToA()
                .setEndA(bookEntity)
                .setEndB(publisherEntity)
                .setEndAAttributeName("publisher")
                .setType(RelationshipType.MANY_TO_ONE)
                .setRelationHandler(bookEntity);

        context.addEntityInfo(personEntity);
        context.addEntityInfo(bookEntity);
        context.addEntityInfo(publisherEntity);
        context.addEntityRelationship(authorRelationship);
        context.addEntityRelationship(publisherRelationship);
        context.addEntityRelationship(translatorsRelationship);

        context.generate(new File("F://rest-test"));
    }

}