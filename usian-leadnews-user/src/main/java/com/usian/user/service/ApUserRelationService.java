package com.usian.user.service;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.UserRelationDto;

public interface ApUserRelationService {
    /**
     * 用户关注/取消关注
     * @param dto
     * @return
     */
    public ResponseResult follow(UserRelationDto dto);
}
