package com.usian.aips.user;

import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import io.swagger.annotations.Api;

@Api(value = "用户认证列表",tags = "userRealname",description = "用户认证管理API")
public interface ApUserRealnameControllerApi {
    /**
     *按照状态查询用户认证列表
     * @param dto
     * @return
     */
    public ResponseResult loadListByStatus(AuthDto dto);

    /**
     * 自动认证接口
     * @param dto
     * @return
     */
    public ResponseResult AutoUpdateStatus(AuthDto dto);
}
