package com.usian.aips.admin;

import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "频道管理",tags = "channel",description = "频道管理API")
public interface AdChannelControllerApi {
    /**
     * 根据名称分页查询频道列表
     * @return
     */
    @ApiOperation(value = "查询频道信息",notes = "根据名称分页查询频道列表")
    @ApiImplicitParam(value = "查询名称") //name = "channelDto",type = "ChannelDto",
    public ResponseResult findByNameAndPage(ChannelDto channelDto);

    /**
     * 根据id 删除频道
     * @param id
     * @return
     */
    @ApiOperation(value = "删除频道",notes = "根据id删除频道信息")
    public ResponseResult deleteById(Integer id);

    /**
     * 修改频道信息 根据频道id
     * @param adChannel 传入频道参数
     * @return
     */
    @ApiOperation(value = "修改频道",notes = "根据传入参数修改频道信息")
    public ResponseResult update(AdChannel adChannel);

    /**
     * 添加新的频道
     * @param adChannel
     * @return
     */
    @ApiOperation(value = "添加频道",notes = "根据传入参数添加频道信息")
    public ResponseResult save(AdChannel adChannel);

    /**
     * 根据id查询频道信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询频道",notes = "根据id查询频道信息")
    public ResponseResult findByIdChannel(Integer id);
}
