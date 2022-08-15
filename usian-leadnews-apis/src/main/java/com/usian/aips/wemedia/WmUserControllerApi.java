package com.usian.aips.wemedia;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.pojos.WmUser;
import io.swagger.annotations.Api;

@Api(value = "自媒体账号管理",tags = "wmUser",description = "自媒体账号管理API")
public interface WmUserControllerApi {
    /**
     * 保存自媒体用户
     * @param wmUser
     * @return
     */
    public ResponseResult save(WmUser wmUser);

    /**
     * 按照名称查询用户
     * @param name
     * @return
     */
    public WmUser findByName(String name);
}
