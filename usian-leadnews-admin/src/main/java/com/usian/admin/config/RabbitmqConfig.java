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
    //自动审核交换机名称
    public static final String AUTHNEWSExchange = "authNewsExchange";
    //自动审核队列名称
    public static final String AUTHNEWSQUEUE = "authNewsQueue";
    //延迟队列名称
    public static final String DELAYEDAUTHExchange = "delayedAuthExchange";
    public static final String DELAYEDAUTHQueue = "delayedAuthQueue";

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


    @Bean(DELAYEDAUTHExchange)
    public Exchange delayedExchange(){
        return ExchangeBuilder.directExchange(DELAYEDAUTHExchange).delayed().durable(true).build();
    }
    @Bean(DELAYEDAUTHQueue)
    public Queue delayedQueue(){
        return new Queue(DELAYEDAUTHQueue);
    }
    @Bean
    public Binding Bindings1(@Qualifier(DELAYEDAUTHExchange) Exchange exchange,@Qualifier(DELAYEDAUTHQueue) Queue queue){
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with("auth").noargs();
    }

}
