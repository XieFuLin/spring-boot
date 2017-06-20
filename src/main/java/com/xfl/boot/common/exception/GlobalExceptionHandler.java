package com.xfl.boot.common.exception;

import com.xfl.boot.entity.ResponseData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by XFL
 * time on 2017/6/20 21:53
 * description:统一异常处理
 */
@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    //在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    @ResponseBody
    public ResponseData<Object> exceptionHandler(RuntimeException e, HttpServletResponse response) {
        ResponseData<Object> responseData = new ResponseData<Object>();
        return responseData;
    }
}
