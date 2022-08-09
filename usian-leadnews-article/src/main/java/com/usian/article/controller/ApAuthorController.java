package com.usian.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.usian.aips.article.AuthorControllerApi;
import com.usian.article.service.ApAuthorService;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: ApAuthorController
 * @author: wangheng
 * @create: 2022-08-09 18:52
 **/
@RestController
@RequestMapping("/api/v1/author")
public class ApAuthorController implements AuthorControllerApi {
    @Autowired
    private ApAuthorService apAuthorService;
    @GetMapping("/findByUserId/{id}")
    @Override
    public ApAuthor findByUserId(@PathVariable("id") Integer id){
        List<ApAuthor> list = apAuthorService.list(Wrappers.<ApAuthor>lambdaQuery().eq(ApAuthor::getUserId,id));
        for (ApAuthor apAuthor : list) {
            return apAuthor;
        }
        return null;
    }

    @PostMapping("/save")
    @Override
    public ResponseResult save(@RequestBody ApAuthor apAuthor){
        apAuthor.setCreatedTime(new Date());
        apAuthorService.save(apAuthor);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
