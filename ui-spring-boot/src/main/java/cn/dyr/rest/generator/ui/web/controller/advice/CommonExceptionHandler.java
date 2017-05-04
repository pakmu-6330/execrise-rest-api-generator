package cn.dyr.rest.generator.ui.web.controller.advice;

import cn.dyr.rest.generator.ui.web.common.APIResponse;
import cn.dyr.rest.generator.ui.web.common.ResponseMeta;
import cn.dyr.rest.generator.ui.web.common.auth.exception.AuthenticationException;
import cn.dyr.rest.generator.ui.web.common.factory.ResponseMetaFactory;
import cn.dyr.rest.generator.ui.web.exception.BadParameterError;
import cn.dyr.rest.generator.ui.web.exception.DuplicatedConstraintException;
import cn.dyr.rest.generator.ui.web.exception.ResourceNotFoundException;
import cn.dyr.rest.generator.ui.web.util.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 用于统一处理异常的控制器增强类
 *
 * @author DENG YURONG
 * @version 0.1.0001
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(BadParameterError.class)
    public HttpEntity<APIResponse<?>> handleBadParameterException(BadParameterError badParameterError) {
        ResponseMeta meta = ResponseMetaFactory.badParameterMeta(badParameterError.getDesc());
        APIResponse<?> retValue = new APIResponse<>(meta, new HashMap<String, Object>());
        return new ResponseEntity<APIResponse<?>>(retValue, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public HttpEntity<APIResponse<?>> handleAuthException() {
        ResponseMeta meta = ResponseMetaFactory.authFailed();
        APIResponse<?> retValue = new APIResponse<>(meta, new HashMap<String, Object>());
        return new ResponseEntity<APIResponse<?>>(retValue, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public HttpEntity<APIResponse<?>> handleResourceNotFoundException(
            HttpServletRequest request, ResourceNotFoundException exception
    ) {
        String detailMsg = StringUtils.isStringEmpty(exception.getDesc()) ?
                request.getRequestURL().toString() :
                exception.getDesc();

        ResponseMeta meta = ResponseMetaFactory.notFound();
        meta.setDetailed(detailMsg);

        APIResponse<?> retValue = new APIResponse<>(meta, new HashMap<String, Object>());
        return new ResponseEntity<APIResponse<?>>(retValue, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedConstraintException.class)
    public HttpEntity<APIResponse<?>> handleDuplicatedConstraintException(
            HttpServletRequest request, DuplicatedConstraintException exception
    ) {
        String detailMsg = StringUtils.isStringEmpty(exception.getDesc()) ?
                request.getRequestURL().toString() :
                exception.getDesc();

        ResponseMeta meta = ResponseMetaFactory.duplicateMeta(detailMsg);
        APIResponse<?> retValue = new APIResponse<>(meta, new HashMap<String, Object>());

        return new ResponseEntity<APIResponse<?>>(retValue, HttpStatus.BAD_REQUEST);
    }

    public HttpEntity<APIResponse<?>> handleWrongProjectStatusException() {
        return null;
    }

}
