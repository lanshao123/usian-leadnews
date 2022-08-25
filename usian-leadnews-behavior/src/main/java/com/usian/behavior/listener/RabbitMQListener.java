package com.usian.behavior.listener;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.usian.behavior.config.RabbitmqConfig;
import com.usian.behavior.service.ApFollowBehaviorService;
import com.usian.model.behavior.dtos.FollowBehaviorDto;
import com.usian.model.user.dtos.AuthDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


/**
 * @program: usian-leadnews
 * @description: RabbitMQlistener
 * @author: wangheng
 * @create: 2022-08-12 19:19
 **/
@Configuration
public class RabbitMQListener {
    @Autowired
    private ApFollowBehaviorService apFollowBehaviorService;
    @RabbitListener(queues = {RabbitmqConfig.BEHAVIORQUEUE})
    public void authQueueListener(String msg,Message message,Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        FollowBehaviorDto dto = JSONObject.parseObject(msg, FollowBehaviorDto.class);
        apFollowBehaviorService.saveFollowBehavior(dto);
        System.out.println(msg);
    }

}
