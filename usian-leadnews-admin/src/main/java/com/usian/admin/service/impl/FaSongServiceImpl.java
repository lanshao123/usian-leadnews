package com.usian.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.admin.mapper.AdUserMapper;
import com.usian.admin.mapper.FaSongMapper;
import com.usian.admin.service.FaSongService;
import com.usian.model.admin.pojos.AdUser;
import com.usian.model.admin.pojos.FaResult;
import com.usian.model.admin.pojos.FaSong;
import com.usian.model.common.dtos.ResponseResult;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: usian-leadnews
 * @description: FaSongServiceImpl
 * @author: wangheng
 * @create: 2022-08-28 20:37
 **/
@Service
@SuppressWarnings("ALL")
public class FaSongServiceImpl implements FaSongService {
    @Autowired
    private FaSongMapper faSongMapper;
    @Autowired
    private AdUserMapper adUserMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void redis() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        String date3 = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -12);
        String date15 = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, -15);
        String date30 = sdf.format(calendar.getTime());
        List<AdUser> adUsers = adUserMapper.selectList(Wrappers.<AdUser>lambdaQuery());

        for (AdUser adUser : adUsers) {
            FaResult faResult = faSongMapper.findCount(adUser.getId(),date3,sdf.format(new Date()));
            if(faResult==null){
                faResult=new FaResult();
                faResult.setId(adUser.getId());
                faResult.setName(adUser.getName());
                faResult.setCount(0);
            }
            redisTemplate.boundHashOps("tian3").put(faResult.getId(), JSONObject.toJSONString(faResult));
            faResult = faSongMapper.findCount(adUser.getId(),date15,sdf.format(new Date()));
            if(faResult==null){
                faResult=new FaResult();
                faResult.setId(adUser.getId());
                faResult.setName(adUser.getName());
                faResult.setCount(0);
            }
            redisTemplate.boundHashOps("tian15").put(faResult.getId(), JSONObject.toJSONString(faResult));

            faResult = faSongMapper.findCount(adUser.getId(),date30,sdf.format(new Date()));
            if(faResult==null){
                faResult=new FaResult();
                faResult.setId(adUser.getId());
                faResult.setName(adUser.getName());
                faResult.setCount(0);
            }
            redisTemplate.boundHashOps("tian30").put(faResult.getId(), JSONObject.toJSONString(faResult));

        }
    }

    @Override
    public ResponseResult getRedis3(String day) {
        List<FaResult> faResults=new ArrayList<>();
        redisTemplate.boundHashOps("tian"+day).values().forEach(t->{
            FaResult faResult = JSONObject.parseObject(t.toString(), FaResult.class);
            faResults.add(faResult);
        });
        return ResponseResult.okResult(faResults);
    }
}
