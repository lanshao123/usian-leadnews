package com.usian.user;

import com.usian.user.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMQTest {
    // @Autowired
    // private RabbitTemplate rabbitTemplate;
    // /***
    //  * 发送消息
    //  */
    // @Test
    // public void sendMessage() throws  Exception {
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //     System.out.println("发送当前时间:"+dateFormat.format(new Date()));
    //     Map<String,String> message = new HashMap<>();
    //     message.put("name","changgou");
    //     rabbitTemplate.convertAndSend(RabbitmqConfig.QUEUE_MESSAGE_DELAY, message, new MessagePostProcessor() {
    //         @Override
    //         public Message postProcessMessage(Message message) throws AmqpException {
    //             message.getMessageProperties().setExpiration("10000");
    //             return message;
    //         }
    //     });
    //     System.in.read();
    // }
}
