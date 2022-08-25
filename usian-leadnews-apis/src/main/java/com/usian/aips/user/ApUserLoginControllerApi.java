package com.usian.aips.user;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.LoginDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ApUserLoginControllerApi {
    /**
     * app端密码登录
     * @param dto
     * @return
     */
    public ResponseResult login(LoginDto dto);
    /**
     * app端注册
     * @param dto
     * @return
     */
    public ResponseResult register(LoginDto dto);

    /**
     * 登陆发送验证码
     */
    public ResponseResult loginSms(LoginDto dto);
    /**
     * 注册发送验证码
     */
    public ResponseResult registerSms( LoginDto dto);

    /**
     * app端验证码登录
     * @param dto
     * @return
     */
    public ResponseResult loginCode(LoginDto dto);
}
