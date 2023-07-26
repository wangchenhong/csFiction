package io.github.xxyopen.novel.core.common.exception;

import io.github.xxyopen.novel.core.common.constant.ErrorCodeEnum;
import io.github.xxyopen.novel.core.common.resp.RestResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 通用的异常处理器
 *
 * @author ChenXi
 * @date 2022/5/11
 */
@Slf4j
/**
 * @RestControllerAdvice是Spring框架提供的一个注解，用于定义全局性的异常处理和响应处理逻辑。
 * 当应用程序中的控制器抛出异常时，@RestControllerAdvice注解标记的类将被自动触发，从而对异常进行处理。
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * 处理数据校验异常
     */
    @ExceptionHandler(BindException.class)
    public RestResp<Void> handlerBindException(BindException e) {
        log.error(e.getMessage(), e);
        return RestResp.fail(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public RestResp<Void> handlerBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return RestResp.fail(e.getErrorCodeEnum());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public RestResp<Void> handlerException(Exception e) {
        log.error(e.getMessage(), e);
        return RestResp.error();
    }
}
