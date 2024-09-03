package com.ytl.crm.common.config;


import com.ytl.crm.common.base.UgcCmrServiceRespCodeEnum;
import com.ytl.crm.common.exception.UgcCrmServiceException;
import com.ytl.crm.domain.common.BaseException;
import com.ytl.crm.domain.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Set;

/**
 * @author xuyc
 * @version 1.0
 * @description Springboot WEB应用全局异常处理
 * @date Created in 2023/05/14/ 15:38
 * @since JDK 8.0
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 1.BaseException 异常捕获处理
     *
     * @param ex 自定义BaseException异常类型
     * @return Result
     */
    @ExceptionHandler
    public BaseResponse handleException(BaseException ex) {
        log.error("程序异常：" + ex.toString());
        return BaseResponse.responseFail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }


    /**
     * 业务异常
     */
    @ExceptionHandler(UgcCrmServiceException.class)
    public BaseResponse businessException(UgcCrmServiceException e) {
        if (Objects.isNull(e.getCode())) {
            return BaseResponse.responseFail(e.getMessage());
        }
        return BaseResponse.responseFail(e.getCode(), e.getMessage());
    }

    /**
     * FileNotFoundException,NoHandlerFoundException 异常捕获处理
     *
     * @param exception 自定义FileNotFoundException异常类型
     * @return Result
     */
    @ExceptionHandler({FileNotFoundException.class, NoHandlerFoundException.class})
    public BaseResponse noFoundException(Exception exception) {
        log.error("程序异常==>errorCode:{}, exception:{}", HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return BaseResponse.responseFail(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }


    /**
     * NullPointerException 空指针异常捕获处理
     *
     * @param ex 自定义NullPointerException异常类型
     * @return Result
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse handleException(NullPointerException ex) {
        ex.printStackTrace();
        log.error("程序异常：{}" + ex.toString());
        return BaseResponse.responseFail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }


    /**
     * 通用Exception异常捕获
     * 此异常捕获为兜底，如果其他异常处理器没捕获到，最后才走这里
     *
     * @param ex 自定义Exception异常类型
     * @return Result
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleException(Exception ex) {
        log.error("程序异常：" + ex.toString());
        ex.printStackTrace();
        String message = "内部错误，请联系管理员";
        return BaseResponse.responseFail(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public BaseResponse<String> bindExceptionExceptionHandler(BindException bindException) {
        log.warn("bindException [{}]");
        if (bindException.hasErrors()) {
            if (bindException.getFieldError() == null) {
                throw new BaseException("bindException.getFieldError() 不能为空");
            }
            return BaseResponse.responseFail(UgcCmrServiceRespCodeEnum.VALIDATE_ERROR.getCode(), bindException.getFieldError().getDefaultMessage());
        }
        return BaseResponse.responseFail(UgcCmrServiceRespCodeEnum.VALIDATE_ERROR.getCode(), bindException.getMessage());
    }


//    /**
//     * @param exception
//     * @return
//     */
//    @ResponseStatus(HttpStatus.OK)
//    @ExceptionHandler(ValidationException.class)
//    public BaseResponse<String> validationExceptionHandler(ValidationException exception) {
//        log.warn("validationExceptionHandle [{}]");
//        if (exception instanceof ConstraintViolationException) {
//            ConstraintViolationException exs = (ConstraintViolationException) exception;
//            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
//            StringBuilder sb = new StringBuilder();
//            for (ConstraintViolation<?> item : violations) {
//                sb.append(item.getMessage() + ",");
//            }
//            return BaseResponse.responseFail(UgcCmrServiceRespCodeEnum.VALIDATE_ERROR.getCode(), sb.toString());
//        }
//        return BaseResponse.responseFail(UgcCmrServiceRespCodeEnum.VALIDATE_ERROR.getCode(), exception.getMessage());
//    }


    /**
     * 方法参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handleValidationExceptions(
            MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: " + e.getMessage());
        StringBuffer sb = new StringBuffer();
        sb.append("参数不符合规则-- ");
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            sb.append(fieldName).append(": ").append(errorMessage).append("  ");
        });
        //提示所有错误
        return BaseResponse.responseFail(sb.toString());
    }


}
