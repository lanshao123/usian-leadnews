package com.usian.admin.controller.v1;

import com.usian.admin.service.AdChannelService;
import com.usian.admin.service.AdSensitiveService;
import com.usian.aips.admin.AdChannelControllerApi;
import com.usian.aips.admin.AdSensitiveControllerApi;
import com.usian.model.admin.dtos.ChannelDto;
import com.usian.model.admin.dtos.SensitiveDto;
import com.usian.model.admin.pojos.AdChannel;
import com.usian.model.admin.pojos.AdSensitive;
import com.usian.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: AdChannelServiceController
 * @author: wangheng
 * @create: 2022-08-04 18:17
 **/
@RestController
@RequestMapping("/api/v1/sensitive")
public class AdSensitiveController implements AdSensitiveControllerApi {
    @Autowired
    private AdSensitiveService adSensitiveService;
    @Override
    @PostMapping("/list")
    public ResponseResult findByNameAndPage(@RequestBody SensitiveDto sensitiveDto) {

        return adSensitiveService.findByNameAndPage(sensitiveDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable Integer id) {
        return adSensitiveService.deleteById(id);
    }

    @Override
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.update(adSensitive);
    }

    @Override
    @PostMapping("/save")
    public ResponseResult save(@RequestBody AdSensitive adSensitive) {
        return adSensitiveService.insert(adSensitive);
    }

}
