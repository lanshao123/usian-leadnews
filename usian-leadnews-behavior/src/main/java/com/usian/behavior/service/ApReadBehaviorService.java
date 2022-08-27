package com.usian.behavior.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.behavior.dtos.ReadBehaviorDto;
import com.usian.model.behavior.pojos.ApReadBehavior;
import com.usian.model.common.dtos.ResponseResult;

public interface ApReadBehaviorService extends IService<ApReadBehavior> {
    /**
     * 保存阅读行为
     * @param dto
     * @return
     */
    ResponseResult readBehavior(ReadBehaviorDto dto);
}
