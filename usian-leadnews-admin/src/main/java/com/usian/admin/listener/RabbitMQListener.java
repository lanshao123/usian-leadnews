package com.usian.admin.listener;

import com.rabbitmq.client.Channel;
import com.usian.admin.config.RabbitmqConfig;
import com.usian.admin.service.WemediaNewsAutoScanService;
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
    private WemediaNewsAutoScanService wemediaNewsAutoScanService;
    @RabbitListener(queues = {RabbitmqConfig.AUTHNEWSQUEUE})
    public void authQueueListener(String msg, Channel channel, Message mqMsg) throws IOException {
        System.out.println("收到消息:"+msg);
        try {
            wemediaNewsAutoScanService.autoScanByMediaNewsId(Integer.valueOf(msg));
            System.out.println("success，手动签收");
            channel.basicAck(mqMsg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("出现异常，手动签收");
            channel.basicReject(mqMsg.getMessageProperties().getDeliveryTag(), false);
            e.printStackTrace();
        }

    }
    // /***
    //  * 监听消息
    //  * @param msg
    //  */
    // @RabbitHandler
    // public void msg(@Payload Object msg){
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //     System.out.println("当前时间:"+dateFormat.format(new Date()));
    //     System.out.println("收到信息:"+msg);
    // }

}
