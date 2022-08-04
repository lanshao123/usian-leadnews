package com.usian.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @program: usian-leadnews
 * @description: AdSensitiveService
 * @author: wangheng
 * @create: 2022-08-04 16:56
 **/
public interface AdSensitiveService extends IService<AdSensitive> {
    /**
     * 敏感词分页条件查询
     * @param sensitiveDto
     * @return
     */

    public ResponseResult findByNameAndPage(SensitiveDto sensitiveDto);

    /**
     * 根据id删除敏感词
     * @param id
     * @return
     */

    public ResponseResult deleteById(Integer id);

    /**
     * 修改敏感词信息
     * @param adSensitive
     * @return
     */
    public ResponseResult update(AdSensitive adSensitive);

    /**
     * 添加敏感词信息
     * @param adSensitive
     * @return
     */
    public ResponseResult insert(AdSensitive adSensitive);

}
