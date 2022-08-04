package com.usian.common.exception;

/**
 * @program: usian-leadnews
 * @description: ExceptionCast
 * @author: wangheng
 * @create: 2022-08-04 11:01
 **/

/**
 * 抛出异常的类  对抛出做了封装
 */
public class ExceptionCast {
    public static void cast(Integer code,String msg){
        throw new CustomException(code,msg);
    }

}
