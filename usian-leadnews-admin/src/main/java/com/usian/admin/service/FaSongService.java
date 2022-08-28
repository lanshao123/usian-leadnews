package com.usian.admin.service;

import com.usian.model.common.dtos.ResponseResult;

public interface FaSongService {
    public void redis();
    public ResponseResult getRedis3(String day);
}
