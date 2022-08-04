package com.usian.common.exception;

import com.usian.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: usian-leadnews
 * @description: ExceptionCatch
 * @author: wangheng
 * @create: 2022-08-04 10:53
 **/

/**
 * 异常捕获类 抛出的异常由它来捕获
 */
@ControllerAdvice
public class ExceptionCatch {
    //抛出的异常类型 如果 是  CustomException 就执行下面的代码
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult exception(CustomException exception){
        //执行的具体代码
        exception.printStackTrace();
        return exception.getResponseResult();
    }
}
