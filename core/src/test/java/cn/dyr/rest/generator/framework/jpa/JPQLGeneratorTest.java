package cn.dyr.rest.generator.framework.jpa;

import org.junit.Assert;
import org.junit.Test;

/**
 * 用于测试 JPQL 的生成
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class JPQLGeneratorTest {

    @Test
    public void testFindBookByAuthor() {
        String jpql = JPQLGenerator.getByAnotherEntityInRelationship(
                "Book", "Person", "authors", "id");
        Assert.assertEquals("select book from Book book inner join book.authors person where person.id=?1", jpql);
    }

    @Test
    public void testFindBookByTranslators() {
        String jpql = JPQLGenerator.getByAnotherEntityInRelationship(
                "Book", "Person", "translators", "id");
        Assert.assertEquals("select book from Book book inner join book.translators person where person.id=?1", jpql);
    }

}