package com.usian.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.behavior.dtos.FollowBehaviorDto;
import com.usian.model.behavior.pojos.ApFollowBehavior;
import com.usian.model.common.dtos.ResponseResult;

public interface ApFollowBehaviorService extends IService<ApFollowBehavior> {
    /**
     * 存储关注数据
     * @param dto
     * @return
     */
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto);
}
