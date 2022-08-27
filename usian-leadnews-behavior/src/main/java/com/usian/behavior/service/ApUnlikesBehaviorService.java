package com.usian.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.behavior.dtos.LikesBehaviorDto;
import com.usian.model.behavior.dtos.UnLikesBehaviorDto;
import com.usian.model.behavior.pojos.ApUnlikesBehavior;
import com.usian.model.common.dtos.ResponseResult;

public interface ApUnlikesBehaviorService extends IService<ApUnlikesBehavior> {
    ResponseResult unlike(UnLikesBehaviorDto dto);
}
