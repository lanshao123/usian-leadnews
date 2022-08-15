package com.usian.aips.user;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: usian-leadnews
 * @description: ApUserIdentityControllerApi
 * @author: wangheng
 * @create: 2022-08-09 20:37
 **/
@Api(value = "身份认证",tags = "userIdentity",description = "身份认证管理API")
public interface ApUserIdentityControllerApi {
    /**
     * 管理员审核身份接口
     * @param dto
     * @return
     */
    public ResponseResult idEntityPass(@RequestBody AuthDto dto);
}
