package com.usian.admin.feign;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.pojos.WmNews;
import com.usian.model.media.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @program: usian-leadnews
 * @description: WemediaFeign
 * @author: wangheng
 * @create: 2022-08-18 20:02
 **/
@FeignClient("leadnews-wemedia")
public interface WemediaFeign {
    @GetMapping("/api/v1/news/one/{id}")
    ResponseResult findById(@PathVariable("id") Integer id);

    @PostMapping("/api/v1/news/update")
    ResponseResult updateWmNews(WmNews wmNews);

    @GetMapping("/api/v1/user/findOne/{id}")
    WmUser findWmUserById(@PathVariable("id") Long id);
}
