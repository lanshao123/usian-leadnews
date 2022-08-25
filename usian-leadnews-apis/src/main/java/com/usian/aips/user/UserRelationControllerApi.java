package com.usian.aips.user;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.UserRelationDto;

public interface UserRelationControllerApi {
    /**
     * 关注或取消关注
     * @param dto
     * @return
     */
    ResponseResult follow(UserRelationDto dto);
}
