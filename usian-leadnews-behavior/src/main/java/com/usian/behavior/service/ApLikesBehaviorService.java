package com.usian.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.pojos.ApLikesBehavior;
import com.usian.model.common.dtos.ResponseResult;

public interface ApLikesBehaviorService extends IService<ApLikesBehavior> {
    /**
     * 存储喜欢数据
     * @param dto
     * @return
     */
    public ResponseResult like(LikesBehaviorDto dto);
}
