package com.usian.wemedia.controller;

import com.usian.aips.wemedia.MaterialManageControllerApi;
import com.usian.common.contants.wemedia.WemediaContans;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmMaterialDto;
import com.usian.wemedia.service.WmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: usian-leadnews
 * @description: WmMaterialController
 * @author: wangheng
 * @create: 2022-08-15 17:56
 **/
@RestController
@RequestMapping("/api/v1/material")
public class WmMaterialController implements MaterialManageControllerApi {
    @Autowired
    private WmMaterialService wmMaterialService;
    @Override
    @RequestMapping("/upload_picture")
    public ResponseResult uploadPicture(@RequestPart MultipartFile multipartFile,@RequestParam("type")Integer type) {
        System.out.println(type);
        return  wmMaterialService.uploadPicture(multipartFile,type);
    }

    @Override
    @RequestMapping("/list")
    public ResponseResult findList(@RequestBody WmMaterialDto dto) {
        return wmMaterialService.findList(dto);
    }

    @Override
    @GetMapping("/del_picture/{id}")
    public ResponseResult delPicture(@PathVariable Integer id) {
        return wmMaterialService.delPicture(id);
    }

    @Override
    @GetMapping("/collect/{id}")
    public ResponseResult collect(@PathVariable Integer id) {
        return wmMaterialService.isCollection(id, WemediaContans.COLLECT_MATERIAL);
    }

    @Override
    @GetMapping("/cancel_collect/{id}")
    public ResponseResult cancelCollect(@PathVariable Integer id) {
        return wmMaterialService.isCollection(id, WemediaContans.CANCEL_COLLECT_MATERIAL);
    }
}
