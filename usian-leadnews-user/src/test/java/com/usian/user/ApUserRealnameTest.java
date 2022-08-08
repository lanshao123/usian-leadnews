package com.usian.user;

import com.usian.model.common.dtos.ResponseResult;
import com.usian.model.user.dtos.AuthDto;
import com.usian.user.service.ApUserRealnameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: usian-leadnews
 * @description: ApUserRealnameTest
 * @author: wangheng
 * @create: 2022-08-08 20:29
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("ALL")
public class ApUserRealnameTest {
    @Autowired
    private ApUserRealnameService apUserRealnameService;
    @Test
    public void dto(){
        AuthDto authDto=new AuthDto();
        authDto.setId(2);
        ResponseResult responseResult = apUserRealnameService.AutoUpdateStatus(authDto);
        System.out.println("responseResult"+responseResult);
    }
}
