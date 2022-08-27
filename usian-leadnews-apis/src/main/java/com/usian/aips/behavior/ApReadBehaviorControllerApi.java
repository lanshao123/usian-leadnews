package com.usian.aips.behavior;

import com.usian.model.behavior.dtos.ReadBehaviorDto;
import com.usian.model.common.dtos.ResponseResult;

public interface ApReadBehaviorControllerApi {
    /**
     * 保存或更新阅读行为
     * @return
     */
    public ResponseResult readBehavior(ReadBehaviorDto dto);
}
