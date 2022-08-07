package com.usian.aips.admin;

import com.usian.model.admin.dtos.AdUserDto;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "登陆管理",tags = "login",description = "登陆管理API")
public interface LoginControllerApi {
    /**
     * admin登录功能
     * @param dto
     * @return
     */
    @ApiOperation(value = "用户登陆",notes = "用户登陆")
    public ResponseResult login(AdUserDto dto);

}
