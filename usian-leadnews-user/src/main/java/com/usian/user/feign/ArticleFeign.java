package com.usian.user.feign;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.model.article.pojos.ApAuthor;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: ArticleFeign
 * @author: wangheng
 * @create: 2022-08-09 18:44
 **/
@FeignClient("leadnews-article")
public interface ArticleFeign {
     @GetMapping("/api/v1/author/findByUserId/{id}")
     public ApAuthor findByUserId(@PathVariable("id") Integer id);
     @GetMapping("/api/v1/author/one/{id}")
     ApAuthor selectById(@PathVariable("id") Integer id);
     @PostMapping("/api/v1/author/save")
     public ResponseResult save(@RequestBody ApAuthor apAuthor);
}
