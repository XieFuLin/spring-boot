package com.xfl.boot.common.exception;

import com.xfl.boot.entity.ResponseData;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by XFL
 * time on 2017/6/20 21:53
 * description:统一异常处理
 */
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    @ResponseBody
    public ResponseData<Object> exceptionHandler(Exception e, HttpServletResponse response) {
        System.out.println(e);
        ResponseData<Object> responseData = new ResponseData<Object>();
        //参数校验未通过异常 @RequestBody参数校验失败
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
            List<ObjectError> errors = exception.getBindingResult().getAllErrors();
            StringBuffer sb = new StringBuffer();
            for (ObjectError error : errors) {
                String message = error.getDefaultMessage();
                sb.append(message).append(";");
            }
            responseData.setMsg(sb.toString());
        } else if (e instanceof ConstraintViolationException) {
            //@RequestParam 参数校验失败
            ConstraintViolationException exception = (ConstraintViolationException) e;
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation constraint : exception.getConstraintViolations()) {
                sb.append(constraint.getInvalidValue()).append("值不正确，").append(constraint.getMessage()).append("；");
            }
            responseData.setMsg(sb.toString());
        } else {
            //其他异常
            responseData.setMsg(e.getMessage());
        }
        responseData.setCode(9999);

        return responseData;
    }
}
