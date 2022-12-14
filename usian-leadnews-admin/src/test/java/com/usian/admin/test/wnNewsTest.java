package com.usian.admin.test;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.usian.admin.mapper.AdUserMapper;
import com.usian.admin.mapper.FaSongMapper;
import com.usian.admin.service.FaSongService;
import com.usian.admin.service.WemediaNewsAutoScanService;
import com.usian.model.admin.pojos.AdUser;
import com.usian.model.admin.pojos.FaResult;
import com.usian.model.admin.pojos.FaSong;
import com.usian.utils.common.AppJwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: usian-leadnews
 * @description: wnNewsTest
 * @author: wangheng
 * @create: 2022-08-19 15:59
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class wnNewsTest {
    @Autowired
    private AdUserMapper adUserMapper;
    @Autowired
    private FaSongMapper faSongMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FaSongService faSongService;
    @Test
    public void newTest() throws Exception {
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
    @Test
    public void getRedis(){
        //faSongService.getRedis3();
    }


}
