package com.usian.admin.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
        requestTemplate.header("token", "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAC2LWwrDMAzA7uLvBvLYUru38RIXMtgIOIGN0bvXhf1JCP3gORpsQGXFtdyjk-Sru-3FOy4oLlEtuZIw5QALNB6whZw9RopIC-h82K1fHfK6uqrp1MZvM57VjHs3lk__nynidTZrIUR_nK2W0UmDAAAA.l8gQwmGoJr7G1ekv46nrQ8FCxiUQCz3j2Vc7gwKas87w1MU7bPG5p-nNkNAy6NAA1Y4MBJ9rPxYP-SSHuXfdUA");
    }
}
