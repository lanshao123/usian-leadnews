package com.usian.wemedia.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.media.dtos.WmUserDto;
import com.usian.model.media.pojos.WmUser;
import com.usian.utils.common.AppJwtUtil;
import com.usian.utils.common.BCrypt;
import com.usian.wemedia.mapper.WmUserMapper;
import com.usian.wemedia.service.WmUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: WmUserServiceImpl
 * @author: wangheng
 * @create: 2022-08-09 16:49
 **/
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements WmUserService {

    @Override
    public ResponseResult login(WmUserDto dto) {
        //1.检查参数
        if (StringUtils.isEmpty(dto.getName()) || StringUtils.isEmpty(dto.getPassword())) {
            ExceptionCast.cast(1, "参数不能为空");
        }
        //2.查询数据库中的用户信息
        WmUser user = this.getOne(Wrappers.<WmUser>lambdaQuery().eq(WmUser::getName, dto.getName()));
        if(user==null){
            ExceptionCast.cast(1,"用户不存在");
        }
        if(!BCrypt.checkpw(dto.getPassword(),user.getPassword())) {
            ExceptionCast.cast(1, "密码错误");
        }
        //验证成功签发token
        String token = AppJwtUtil.getToken(user.getId().longValue());
        //4.返回数据jwt
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        user.setPassword("");
        map.put("user",user);
        return ResponseResult.okResult(map);

    }
}
