package com.usian.admin.listener;

import com.rabbitmq.client.Channel;
import com.usian.admin.config.RabbitmqConfig;
import com.usian.admin.service.WemediaNewsAutoScanService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

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

    /**
     * 通过延迟队列来进行自动发布
     * @param msg
     */
    @RabbitListener(queues = {RabbitmqConfig.DELAYEDAUTHQueue})
    public void auth(@Payload Object msg) throws Exception {
        Message message = (Message) msg;
        String str =  new String(message.getBody());
        //收到消息就进行发布文章即可
        log.info("收到延迟队列的消息：{}-开始发布文章",str);
        wemediaNewsAutoScanService.autoScanByMediaNewsId(Integer.valueOf(str));
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
