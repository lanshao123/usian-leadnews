package com.usian.sms.listener;

import com.alibaba.fastjson.JSONObject;
import com.usian.model.user.dtos.SmsModel;
import com.usian.model.user.dtos.UserSmsDto;
import com.usian.sms.utils.sendsmsutf8;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * @program: usian-leadnews
 * @description: RabbitMQlistener
 * @author: wangheng
 * @create: 2022-08-12 19:19
 **/
@Configuration
@Log4j2
public class RabbitMQListener {

    @RabbitListener(queues = {"SMSQueue"})
    public void authSms(String msg) throws IOException {
        UserSmsDto userSmsDto = JSONObject.parseObject(msg, UserSmsDto.class);
        if (userSmsDto.getTeme_code().equals(SmsModel.LOGIN)) {
            System.out.println("登陆验证码:"+userSmsDto.getCode());
        }else if (userSmsDto.getTeme_code().equals(SmsModel.REGISTER)){
            System.out.println("注册验证码:"+userSmsDto.getCode());
        }
        //sendsmsutf8.smscode(userSmsDto.getPhone(),userSmsDto.getCode());

    }


}
