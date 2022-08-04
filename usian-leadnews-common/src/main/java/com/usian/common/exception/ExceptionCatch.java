package com.usian.common.exception;

import com.google.common.collect.ImmutableMap;
import com.usian.model.common.dtos.ResponseResult;
import lombok.extern.log4j.Log4j2;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

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
@Log4j2
public class ExceptionCatch {

    //使用EXCEPTIONS存放异常类型和错误代码的映射，ImmutableMap的特点的一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>, ResponseResult> EXCEPTIONS;
    //使用builder来构建一个异常类型和错误代码的异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResponseResult> builder = ImmutableMap.builder();

    //静态代码块初始化异常信息
    static {
        //在这里加入一些基础的异常类型判断
        builder.put(HttpMessageNotReadableException.class,ResponseResult.errorResult( 1,"非法参数"));
        builder.put(NullPointerException.class,ResponseResult.errorResult(1,"出现空对象异常"));
        builder.put(IndexOutOfBoundsException.class,ResponseResult.errorResult(1,"下角标越界异常"));
        //自己填充的
        builder.put(ArithmeticException.class,ResponseResult.errorResult(1,"除数不能为0"));
    }

    //抛出的异常类型 如果 是  CustomException 就执行下面的代码
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult exception(CustomException exception){
        //执行的具体代码
        exception.printStackTrace();
        return exception.getResponseResult();
    }
    //捕获未知异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exceptions(Exception e){
        log.error(e.getMessage()); //日志
        e.printStackTrace(); //打印输出
        if(EXCEPTIONS==null){ //如果为空 就 初始化
            EXCEPTIONS = builder.build();
        }
        //获取异常返回的结果
        ResponseResult responseResult =EXCEPTIONS.get(e.getClass());
        if(responseResult!=null){
            return responseResult;
        }
        return ResponseResult.errorResult(1,"出现未知异常");

    }
}
