package com.usian.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import com.usian.model.user.pojos.ApUserIdentity;

public interface ApUserIdentityService extends IService<ApUserIdentity> {
    /**
     * 管理员进行身份认证 接口
     * @param dto
     * @param status
     * @return
     */
    public ResponseResult updateStatus(AuthDto dto, Short status);
}
