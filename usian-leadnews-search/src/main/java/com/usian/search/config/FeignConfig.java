package com.usian.search.config;

import com.usian.utils.common.AppJwtUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @program: usian-leadnews
 * @description: FeignConfig
 * @author: wangheng
 * @create: 2022-08-15 16:16
 **/
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //添加token
        requestTemplate.header("token", AppJwtUtil.getToken(1L));
    }
}
