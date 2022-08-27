package com.usian.common.web.behavior;


import com.alibaba.fastjson.JSON;
import com.usian.common.contants.Contants;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.pojos.ApUser;
import com.usian.utils.common.AppJwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(filterName = "behaviorTokenFilter" ,urlPatterns = "/*")
public class BehaviorTokenFilter extends GenericFilterBean {

    Logger logger = LoggerFactory.getLogger(BehaviorTokenFilter.class);

    @Override
    public  void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        String uri = request.getRequestURI();
        ResponseResult<?> result = checkToken(request,response);
        // 测试和开发环境不过滤
        if(result==null||uri.contains("/login")||uri.contains("/register")){
            chain.doFilter(req,res);
        }else{
            res.setCharacterEncoding(Contants.CHARTER_NAME);
            res.setContentType("application/json");
            res.getOutputStream().write(JSON.toJSONString(result).getBytes());
        }
    }

    /**
     * 判断TOKEN信息，并设置为上下文
     * @param request
     * @return
     * 如果验证不通过则返回对应的错误，否则返回null继续执行
     */
    public ResponseResult checkToken(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("token");
        ResponseResult<?> rr = null;
        if(StringUtils.isNotEmpty(token)) {
            Claims claims = AppJwtUtil.getClaimsBody(token);
            int result = AppJwtUtil.verifyToken(claims);
            // 有效的token
            if (result == 0||result==-1) {
                ApUser apUser = findUser((Integer)claims.get("id"));
                logger.info("find userid:[{}] from uri:{}",apUser.getId(),request.getRequestURI());
                if(apUser.getId()!=null) {
                    // 重新设置TOKEN
                    if(result==-1) {
                        response.setHeader("REFRESH_TOKEN", AppJwtUtil.getToken(Long.parseLong(apUser.getId()+"")));
                        // 在存到redis中
                    }
                    com.usian.utils.threadlocal.AppThreadLocalUtils.setUser(apUser);
                }else{
                    rr = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_INVALID);
                }
            }else if(result==1){
                // 过期
                rr = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_EXPIRE);
            }else if(result==2){
                // TOKEN错误
                rr = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_INVALID);
            }
        }else{
            rr = ResponseResult.setAppHttpCodeEnum(AppHttpCodeEnum.TOKEN_REQUIRE);
        }
        return rr;
    }

    public ApUser findUser(Integer userId){
        ApUser apUser = new ApUser();
        apUser.setId(userId);
        return apUser;
    }

}
