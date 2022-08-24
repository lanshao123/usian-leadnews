package com.usian.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.common.enums.AppHttpCodeEnum;
import com.usian.model.user.dtos.LoginDto;
import com.usian.model.user.dtos.UserSmsDto;
import com.usian.model.user.pojos.ApUser;
import com.usian.user.mapper.ApUserMapper;
import com.usian.user.service.ApUserLoginService;
import com.usian.utils.common.AppJwtUtil;
import com.usian.utils.common.BCrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: usian-leadnews
 * @description: ApUserLoginServiceImpl
 * @author: wangheng
 * @create: 2022-08-24 19:47
 **/
@Service
@SuppressWarnings("ALL")
public class ApUserLoginServiceImpl  implements ApUserLoginService {
    @Autowired
    private ApUserMapper apUserMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public ResponseResult login(LoginDto dto) {
        //1.校验参数
        if (dto.getEquipmentId() == null && (StringUtils.isEmpty(dto.getPassword()) && StringUtils.isEmpty(dto.getPassword()))) {
            ExceptionCast.cast(1, "账号密码不能为空");
        }
        //2.手机号+密码登录
        if (!StringUtils.isEmpty(dto.getPhone()) && !StringUtils.isEmpty(dto.getPassword())) {
            //用户登录
            ApUser dbUser = apUserMapper.selectOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, dto.getPhone()));
            if (dbUser != null) {
                if (BCrypt.checkpw(dto.getPassword(), dbUser.getPassword())) {
                    Map<String, Object> map = new HashMap<>();
                    dbUser.setPassword("");
                    dbUser.setSalt("");
                    map.put("token", AppJwtUtil.getToken(dbUser.getId().longValue()));
                    map.put("user", dbUser);
                    return ResponseResult.okResult(map);
                } else {
                    ExceptionCast.cast(1, "密码错误");
                }
            } else {
                ExceptionCast.cast(1, "用户不存在");
            }
        } else {
            //3.设备登录
            if (dto.getEquipmentId() == null) {
                ExceptionCast.cast(1, "无效参数");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(0l));
            return ResponseResult.okResult(map);

        }
        return null;
    }

    @Override
    public ResponseResult register(LoginDto dto) {
        //1.校验参数
        if (dto.getEquipmentId() == null && (StringUtils.isEmpty(dto.getPassword()) && StringUtils.isEmpty(dto.getPassword()))) {
            ExceptionCast.cast(1, "账号密码不能为空");
        }
        //最简单的注册
        ApUser apUser=new ApUser();
        apUser.setName(dto.getPhone());
        apUser.setPhone(dto.getPhone());
        apUser.setSex(true);
        apUser.setStatus(true);
        apUser.setFlag((short)1);
        apUser.setCreatedTime(new Date());
        apUser.setPassword(BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt()));
        apUserMapper.insert(apUser);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    /**
     * 登陆发送验证码
     */
    public ResponseResult sms(LoginDto dto) {
        if(dto==null ||dto.getPhone()==null){
            ExceptionCast.cast(1,"参数错误");
        }
        //先从redis检测是否 短信验证码的时效：60秒 不能重复发送 login15225839128

        Object o = redisTemplate.boundValueOps(dto.getPhone()).get();
        if(o!=null){
            System.out.println(o);
            ExceptionCast.cast(1,"60秒内只能发送一次验证码");
        }
        //如果有就不能发送 没有就可以发送
        //随机验证码
        UserSmsDto userSmsDto=new UserSmsDto();
        int code = (int) (Math.random() * (9999 - 1000 + 1) + 1000);
        userSmsDto.setCode(String.valueOf(code));
        userSmsDto.setPhone(dto.getPhone());
        userSmsDto.setTeme_code(dto.getTeme_code());
        rabbitTemplate.convertAndSend("SMSExchange","sms", JSONObject.toJSONString(userSmsDto));
        //发送消息到mq 进行消息处理
        //验证码过期时间
        redisTemplate.boundValueOps(dto.getTeme_code()+dto.getPhone()).set(code);
        redisTemplate.boundValueOps(dto.getTeme_code()+dto.getPhone()).expire(300, TimeUnit.SECONDS );
        //60秒只能发送一次
        redisTemplate.boundValueOps(dto.getPhone()).set(code);
        redisTemplate.boundValueOps(dto.getPhone()).expire(60, TimeUnit.SECONDS );
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
