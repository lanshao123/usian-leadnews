package com.usian.admin.service;

import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;

public interface AdChannelService {
    /**
     * 根据名称分页查询频道列表
     * @return
     */
    public ResponseResult findByNameAndPage(ChannelDto channelDto);
    /**
     * 根据id 删除频道
     * @param id
     * @return
     */
    public ResponseResult deleteById(Integer id);
    /**
     * 修改频道信息 根据频道id
     * @param adChannel 传入频道参数
     * @return
     */
    public ResponseResult update(AdChannel adChannel);
    /**
     * 添加新的频道
     * @param adChannel
     * @return
     */
    public ResponseResult save(AdChannel adChannel);
}

