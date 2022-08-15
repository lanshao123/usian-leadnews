package com.usian.user.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.media.pojos.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @program: usian-leadnews
 * @description: WemediaFeign
 * @author: wangheng
 * @create: 2022-08-09 18:41
 **/
@FeignClient("leadnews-wemedia")
public interface WemediaFeign {
    @PostMapping("/api/v1/user/save")
    public ResponseResult save(@RequestBody WmUser wmUser);


    @GetMapping("/api/v1/user/findByName/{name}")
    public WmUser findByName(@PathVariable("name") String name);
}
