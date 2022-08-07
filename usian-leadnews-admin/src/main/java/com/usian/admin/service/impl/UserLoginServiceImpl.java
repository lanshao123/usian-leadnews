package com.usian.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.usian.admin.mapper.AdUserMapper;
import com.usian.admin.service.UserLoginService;
import com.usian.common.exception.ExceptionCast;
import com.usian.model.admin.dtos.AdUserDto;
import com.usian.model.admin.pojos.AdUser;
import com.usian.model.admin.vos.AdUserVo;
import com.usian.model.common.dtos.ResponseResult;
import com.usian.utils.common.AppJwtUtil;
import com.usian.utils.common.BCrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: UserLoginServiceImpl
 * @author: wangheng
 * @create: 2022-08-04 20:48
 **/
@Service
@Transactional
public class UserLoginServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements UserLoginService {
    @Override
    public ResponseResult login(AdUserDto dto) {
        //对参数进行校验
        if(StringUtils.isEmpty(dto.getName())||StringUtils.isEmpty(dto.getPassword())){
            ExceptionCast.cast(1,"参数错误");
        }
        //构建查询条件
        LambdaQueryWrapper<AdUser> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AdUser::getName,dto.getName());
        List<AdUser> list = this.list(queryWrapper);
        if(list!=null&list.size()==1){
            //这是查询到了数据并且 数据只有一条
            AdUser adUser = list.get(0);

            //对账号进行判断
            if(adUser.getStatus()!=9){
                ExceptionCast.cast(1,"账号信息异常");
            }
            //加密后的密码 这里是对用户输入的密码 然后 数据库获取的用户盐 进行加密
            //String pwd= BCrypt.hashpw(dto.getPassword(),BCrypt.gensalt());
            //进行密码校验
            if(BCrypt.checkpw(dto.getPassword(),adUser.getPassword())){
                //通过反序列化 把json字符 转换为 对象
                AdUserVo adUserVo=JSON.parseObject(JSON.toJSONString(adUser),AdUserVo.class);
                //设置token串
                adUserVo.setToken(AppJwtUtil.getToken(adUser.getId().longValue()));
                adUserVo.setPassword("");
                //登陆成功后 对登陆状态改变
                adUser.setLoginTime(new Date());
                this.updateById(adUser);
                ResponseResult responseResult=new ResponseResult();
                responseResult.setData(adUserVo);
                return  responseResult;
            }else{
                ExceptionCast.cast(1,"密码错误");
            }
        }else{
            //这里是 条件不成立 就是说没有查询到用户 或者说 查询到了多个用户
            ExceptionCast.cast(1,"用户不存在");
        }

        return null;
    }
}
