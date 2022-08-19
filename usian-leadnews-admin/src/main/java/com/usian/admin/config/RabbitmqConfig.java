package com.usian.admin.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: usian-leadnews
 * @description: RabbitmqConfig
 * @author: wangheng
 * @create: 2022-08-12 19:05
 **/
@Configuration
public class RabbitmqConfig {
    //交换机名称
    public static final String AUTHNEWSExchange = "authNewsExchange";
    public static final String AUTHNEWSQUEUE = "authNewsQueue";

    //配置交换机
    @Bean(AUTHNEWSExchange)
    public Exchange authExchange() {
        //交换机是否持久化durable(true)
        return ExchangeBuilder.directExchange(AUTHNEWSExchange).durable(true).build();
    }

    //配置队列
    @Bean(AUTHNEWSQUEUE)
    public Queue authQueue() {
        return new Queue(AUTHNEWSQUEUE);
    }

    //交换机和队列绑定
    @Bean
    public Binding bingDingAuthExchange_AuthQueue(@Qualifier(AUTHNEWSExchange) Exchange exchange, @Qualifier(AUTHNEWSQUEUE)Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("authNews").noargs();
    }
    // /** 短信发送队列 */
    // public static final String QUEUE_MESSAGE = "queue.message";
    // /** 交换机 */
    // public static final String DLX_EXCHANGE = "dlx.exchange";
    // /** 短信发送队列 延迟缓冲（按消息） */
    // public static final String QUEUE_MESSAGE_DELAY = "queue.message.delay";
    // /**
    //  * 短信发送队列
    //  * @return
    //  */
    // @Bean(QUEUE_MESSAGE)
    // public Queue messageQueue() {
    //     return new Queue(QUEUE_MESSAGE, true);
    // }
    // /**
    //  * 短信发送队列
    //  * @return
    //  */
    // @Bean(QUEUE_MESSAGE_DELAY)
    // public Queue delayMessageQueue() {
    //     return QueueBuilder.durable(QUEUE_MESSAGE_DELAY)
    //             .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)        // 消息超时进入死信队列，绑定死信队列交换机
    //             .withArgument("x-dead-letter-routing-key", QUEUE_MESSAGE)   // 绑定指定的routing-key
    //             .build();
    // }
    // /***
    //  * 创建交换机
    //  * @return
    //  */
    // @Bean(DLX_EXCHANGE)
    // public DirectExchange directExchange(){
    //     return new DirectExchange(DLX_EXCHANGE);
    // }
    // /***
    //  * 交换机与队列绑定
    //  * @param messageQueue
    //  * @param directExchange
    //  * @return
    //  */
    // @Bean
    // public Binding basicBinding(@Qualifier(QUEUE_MESSAGE_DELAY) Queue messageQueue, @Qualifier(DLX_EXCHANGE) DirectExchange directExchange) {
    //     return BindingBuilder.bind(messageQueue)
    //             .to(directExchange)
    //             .with(QUEUE_MESSAGE);
    // }
}
