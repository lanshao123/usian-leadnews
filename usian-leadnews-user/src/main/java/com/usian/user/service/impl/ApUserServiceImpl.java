package com.usian.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usian.model.user.pojos.ApUser;
import com.usian.user.mapper.ApUserMapper;
import com.usian.user.service.ApUserService;
import org.springframework.stereotype.Service;

/**
 * @program: usian-leadnews
 * @description: ApUserServiceImpl
 * @author: wangheng
 * @create: 2022-08-29 21:33
 **/
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {
}
