package com.usian.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usian.admin.mapper.AdChannelMapper;
import com.usian.admin.service.AdChannelService;
import com.usian.common.exception.CustomException;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.common.dtos.PageResponseResult;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: usian-leadnews
 * @description: AdChannelServiceImpl
 * @author: wangheng
 * @create: 2022-08-03 15:50
 **/
@Service
@SuppressWarnings("ALL")
public class AdChannelServiceImpl implements AdChannelService {

    @Autowired
    private AdChannelMapper adChannelMapper;

    @Override
    public ResponseResult findByNameAndPage(ChannelDto dto) {
        //判断参数
        if (dto == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        //对分页参数进行检测
        dto.checkParam();
        //开启分页
        Page page = new Page(dto.getPage(), dto.getSize());
        //使用mybatis-plus 进行查询
        LambdaQueryWrapper<AdChannel> lambdaQueryWrapper = new LambdaQueryWrapper();
        //构建查询条件
        lambdaQueryWrapper.like(dto.getName() != null && !"".equals(dto.getName()), AdChannel::getName, dto.getName());
        //构建状态查询
        lambdaQueryWrapper.eq(dto.getStatus() != null && !"".equals(dto.getStatus()), AdChannel::getStatus, dto.getStatus());
        //获取分页结果
        IPage iPage = adChannelMapper.selectPage(page, lambdaQueryWrapper);
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) iPage.getTotal());
        responseResult.setData(iPage.getRecords());
        return responseResult;
    }

    @Override
    public ResponseResult deleteById(Integer id) {
        //判断参数
        if (id == 0) {
            //抛出可知的运行时异常
            ExceptionCast.cast(1, "id不能为空");
            //return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        //id校验成功 进行删除操作
        adChannelMapper.deleteById(id);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult update(AdChannel adChannel) {
        //判断参数
        if (adChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        //直接进行修改操作 根据主键进行修改
        try {

            adChannelMapper.updateById(adChannel);
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }

    }

    @Override
    public ResponseResult save(AdChannel adChannel) {
        //判断参数
        if (adChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        //对参数进行初始化
        adChannel.setCreatedTime(new Date());
        adChannel.setIsDefault(true);
        //进行添加操作
        try {
            adChannelMapper.insert(adChannel);
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    @Override
    public ResponseResult findByIdChannel(Integer id) {
        //判断参数
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        //根据id查询
        AdChannel adChannel = adChannelMapper.selectById(id);
        if (adChannel == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.okResult(adChannel);
    }
}
