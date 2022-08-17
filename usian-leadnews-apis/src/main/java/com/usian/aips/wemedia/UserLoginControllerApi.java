package com.usian.aips.wemedia;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmUserDto;
import com.usian.model.user.dtos.AuthDto;
import io.swagger.annotations.Api;

/**
 * @program: usian-leadnews
 * @description: UserLoginControllerApi
 * @author: wangheng
 * @create: 2022-08-15 16:32
 **/
@Api(value = "自媒体登陆",tags = "userLogin",description = "自媒体登陆API")
public interface UserLoginControllerApi {
    /**
     * 自媒体登陆
     * @param dto
     * @return
     */
    public ResponseResult login(WmUserDto dto);
}
