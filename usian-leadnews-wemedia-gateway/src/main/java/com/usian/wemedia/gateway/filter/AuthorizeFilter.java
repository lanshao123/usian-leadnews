package com.usian.wemedia.gateway.filter;

import com.usian.wemedia.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: usian-leadnews
 * @description: AuthorizeFilter
 * @author: wangheng
 * @create: 2022-08-05 16:56
 **/
@Component
@Log4j2
public class AuthorizeFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取请求对象和响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //2.判断当前的请求是否为登录，如果是，直接放行
        if(request.getURI().getPath().contains("/login/in")){
            //放行
            return chain.filter(exchange);
        }

        String jwtToken = request.getHeaders().getFirst("token");
        //4.判断当前令牌是否存在
        if(StringUtils.isEmpty(jwtToken)){
            //如果不存在，向客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            //获取载荷
            Claims claimsBody = AppJwtUtil.getClaimsBody(jwtToken);
          /*  //判断是否过期
            int result = AppJwtUtil.verifyToken(claimsBody);
            log.info("token过期状态:",result);
            //先获取到载荷里面的id 用来做刷新token
            Integer id =  (Integer)claimsBody.get("id");
            if(result==-1){
                //这个情况是token小于5分钟需要刷新token
                String token = AppJwtUtil.getToken(id.longValue());
                log.info("刷新token:{}",token);
                //刷新玩把token存到响应头里面 返回给前端处理
                response.getHeaders().set("refresh_token", token);
                return chain.filter(exchange);
            }
            log.info("通过请求头获取到token中的id:{}",id);*/
            //直接携带token放行
            request.mutate().header("token",jwtToken);
            //request.mutate().header("id",id+"");
            return chain.filter(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            //如果不存在，向客户端返回错误提示信息
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
