package com.usian.aips.admin;

import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @program: usian-leadnews
 * @description: AdSensitiveControllerApi
 * @author: wangheng
 * @create: 2022-08-04 16:47
 **/
@Api(value = "敏感词管理",tags = "sensitive",description = "敏感词管理API")
public interface AdSensitiveControllerApi {
    /**
     * 敏感词分页条件查询
     * @param sensitiveDto
     * @return
     */
    @ApiOperation(value = "查询敏感词列表",notes = "根据敏感词分页条件查询")
    public ResponseResult findByNameAndPage(SensitiveDto sensitiveDto);

    /**
     * 根据id删除敏感词
     * @param id
     * @return
     */
    @ApiOperation(value = "删除敏感词",notes = "根据id删除敏感词")
    public ResponseResult deleteById(Integer id);

    /**
     * 修改敏感词信息
     * @param adSensitive
     * @return
     */
    @ApiOperation(value = "修改敏感词",notes = "根据id修改敏感词信息")
    public ResponseResult update(AdSensitive adSensitive);

    /**
     * 添加敏感词信息
     * @param adSensitive
     * @return
     */
    @ApiOperation(value = "添加敏感词",notes = "根据id修改敏感词信息")
    public ResponseResult save(AdSensitive adSensitive);

}
