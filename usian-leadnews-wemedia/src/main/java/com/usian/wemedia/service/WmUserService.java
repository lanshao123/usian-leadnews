package com.usian.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmUserDto;
import com.usian.model.media.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {
    public ResponseResult login(WmUserDto dto);
}
