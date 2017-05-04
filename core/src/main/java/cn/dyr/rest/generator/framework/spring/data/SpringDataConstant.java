package cn.dyr.rest.generator.framework.spring.data;

/**
 * Spring Data 相关的一些常量
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringDataConstant {

    /**
     * PagingAndSortingRepository 接口的类全名
     */
    public static final String INTERFACE_PAGING_AND_SORTING_REPOSITORY = "org.springframework.data.repository.PagingAndSortingRepository";

    /**
     * CrudRepository　接口的类全名
     */
    public static final String INTERFACE_CRUD_REPOSITORY = "org.springframework.data.repository.CrudRepository";

    /**
     * EnableJpaRepositories 注解的类全名
     */
    public static final String ENABLE_JPA_REPOSITORY_ANNOTATION = "org.springframework.data.jpa.repository.config.EnableJpaRepositories";

    /**
     * Spring Data Page 类的类全名
     */
    public static final String INTERFACE_PAGE = "org.springframework.data.domain.Page";

    /**
     * Spring Data Pageable 接口的类全名
     */
    public static final String INTERFACE_PAGEABLE = "org.springframework.data.domain.Pageable";

    /**
     * Spring Data Query 注解的类全名
     */
    public static final String QUERY_ANNOTATION = "org.springframework.data.jpa.repository.Query";

}
