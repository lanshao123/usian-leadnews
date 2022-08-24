package com.usian.article.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rabbitmq.client.Channel;
import com.usian.article.config.RabbitmqConfig;
import com.usian.article.service.ApArticleConfigService;
import com.usian.model.article.pojos.ApArticleConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;
import java.util.Map;


/**
 * @program: usian-leadnews
 * @description: RabbitMQlistener
 * @author: wangheng
 * @create: 2022-08-12 19:19
 **/
@Configuration
@Log4j2
@SuppressWarnings("ALL")
public class RabbitMQListener {
    @Autowired
    private ApArticleConfigService apArticleConfigService;
    @RabbitListener(queues = {RabbitmqConfig.APARTICLECONFIGQUEUE})
    public void authQueueListener(String msg, Channel channel, Message mqMsg) throws IOException {
        System.out.println("文章配置收到消息");
        Map map = JSONObject.parseObject(msg,Map.class);
        apArticleConfigService.update(Wrappers.<ApArticleConfig>lambdaUpdate()
                .eq(ApArticleConfig::getArticleId,map.get("articleId")).set(ApArticleConfig::getIsDown,map.get("enable")));
        }

}
