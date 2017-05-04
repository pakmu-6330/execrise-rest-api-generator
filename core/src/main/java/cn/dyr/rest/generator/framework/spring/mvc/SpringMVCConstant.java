package cn.dyr.rest.generator.framework.spring.mvc;

/**
 * 这个类中含有 SpringMVC 当中一些常用类和注解的类型
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
public class SpringMVCConstant {

    /**
     * RequestMapping 注解的类全名
     */
    public static final String REQUEST_MAPPING_ANNOTATION = "org.springframework.web.bind.annotation.RequestMapping";

    /**
     * RequestParam 注解的类全名
     */
    public static final String REQUEST_PARAM_ANNOTATION = "org.springframework.web.bind.annotation.RequestParam";

    /**
     * RequestMethod 枚举类的类全名
     */
    public static final String REQUEST_METHOD_ENUM_CLASS = "org.springframework.web.bind.annotation.RequestMethod";

    /**
     * RestController 注解的类全名
     */
    public static final String REST_CONTROLLER_ANNOTATION = "org.springframework.web.bind.annotation.RestController";

    /**
     * ControllerAdvice 注解的类全名
     */
    public static final String CONTROLLER_ADVICE_ANNOTATION = "org.springframework.web.bind.annotation.ControllerAdvice";

    /**
     * GetMapping 注解的类全名
     */
    public static final String GET_MAPPING_ANNOTATION = "org.springframework.web.bind.annotation.GetMapping";

    /**
     * PostMapping 注解的类全名
     */
    public static final String POST_MAPPING_ANNOTATION = "org.springframework.web.bind.annotation.PostMapping";

    /**
     * PutMapping 注解的类全名
     */
    public static final String PUT_MAPPING_ANNOTATION = "org.springframework.web.bind.annotation.PutMapping";

    /**
     * DeleteMapping 注解的类全名
     */
    public static final String DELETE_MAPPING_ANNOTATION = "org.springframework.web.bind.annotation.DeleteMapping";

    /**
     * HttpEntity 类的类全名
     */
    public static final String HTTP_ENTITY_CLASS = "org.springframework.http.HttpEntity";

    /**
     * ResponseEntity 类的类全名
     */
    public static final String RESPONSE_ENTITY_CLASS = "org.springframework.http.ResponseEntity";

    /**
     * HttpStatus 枚举的类全名
     */
    public static final String HTTP_STATUS_ENUM_CLASS = "org.springframework.http.HttpStatus";

    /**
     * ResponseBody 类的类全名
     */
    public static final String RESPONSE_BODY_CLASS = "org.springframework.web.bind.annotation.ResponseBody";

    /**
     * PageableDefault 注解的类全名
     */
    public static final String PAGEABLE_DEFAULT_ANNOTATION = "org.springframework.data.web.PageableDefault";

    /**
     * PathVariable 注解的类全名
     */
    public static final String PATH_VARIABLE_ANNOTATION = "org.springframework.web.bind.annotation.PathVariable";

    /**
     * RequestBody 注解的类全名
     */
    public static final String REQUEST_BODY_ANNOTATION = "org.springframework.web.bind.annotation.RequestBody";

    /**
     * ServletUriComponentsBuilder 类的类全名
     */
    public static final String SERVLET_URI_COMPONENTS_BUILDER_CLASS = "org.springframework.web.servlet.support.ServletUriComponentsBuilder";

    /**
     * ResponseStatus 注解的类全名
     */
    public static final String RESPONSE_STATUS_ANNOTATION = "org.springframework.web.bind.annotation.ResponseStatus";

    /**
     * ExceptionHandler 注解的类全名
     */
    public static final String EXCEPTION_HANDLER_ANNOTATION = "org.springframework.web.bind.annotation.ExceptionHandler";

    /**
     * RequestMethod.GET
     */
    public static final String REQUEST_METHOD_MEMBER_GET = "GET";

    /**
     * RequestMethod.POST
     */
    public static final String REQUEST_METHOD_MEMBER_POST = "POST";

    /**
     * RequestMethod.HEAD
     */
    public static final String REQUEST_METHOD_MEMBER_HEAD = "HEAD";

    /**
     * RequestMethod.PUT
     */
    public static final String REQUEST_METHOD_MEMBER_PUT = "PUT";

    /**
     * RequestMethod.PATCH
     */
    public static final String REQUEST_METHOD_MEMBER_PATCH = "PATCH";

    /**
     * RequestMethod.DELETE
     */
    public static final String REQUEST_METHOD_MEMBER_DELETE = "DELETE";

    /**
     * RequestMethod.OPTIONS
     */
    public static final String REQUEST_METHOD_MEMBER_OPTIONS = "OPTIONS";

    /**
     * RequestMethod.TRACE
     */
    public static final String REQUEST_METHOD_MEMBER_TRACE = "TRACE";

    /**
     * 表示 HTTP 200 的枚举成员 OK
     */
    public static final String HTTP_STATUS_MEMBER_OK = "OK";

    /**
     * 表示 HTTP 201 的枚举成员 CREATED
     */
    public static final String HTTP_STATUS_MEMBER_CREATED = "CREATED";

    /**
     * 表示 HTTP 400 的枚举成员 BAD_REQUEST
     */
    public static final String HTTP_STATUS_MEMBER_BAD_REQUEST = "BAD_REQUEST";

    /**
     * 表示 HTTP 404 的枚举成员 NOT_FOUND
     */
    public static final String HTTP_STATUS_MEMBER_NOT_FOUND = "NOT_FOUND";
}
