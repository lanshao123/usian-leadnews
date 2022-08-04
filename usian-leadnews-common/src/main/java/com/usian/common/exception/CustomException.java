package com.usian.common.exception;

import com.usian.model.common.dtos.ResponseResult;

/**
 * @program: usian-leadnews
 * @description: CustomException
 * @author: wangheng
 * @create: 2022-08-04 10:57
 **/

/**
 * 自定义异常类 继承运行时异常
 */
public class CustomException  extends RuntimeException{
    private ResponseResult responseResult=new ResponseResult();
    public CustomException(Integer code,String msg){
        this.responseResult.error(code,msg);
    }

    public ResponseResult getResponseResult() {
        return responseResult;
    }
}
