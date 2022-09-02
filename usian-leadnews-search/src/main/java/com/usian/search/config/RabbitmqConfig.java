package com.usian.search.config;

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
    public static final String ESExchange = "ESExchange";
    //自动审核队列名称
    public static final String ESQueue = "ESQueue";


    //配置交换机
    @Bean(ESExchange)
    public Exchange authExchange() {
        //交换机是否持久化durable(true)
        return ExchangeBuilder.directExchange(ESExchange).durable(true).build();
    }
    //配置队列
    @Bean(ESQueue)
    public Queue authQueue() {
        return new Queue(ESQueue);
    }
    //交换机和队列绑定
    @Bean
    public Binding bingDingAuthExchange_AuthQueue(@Qualifier(ESExchange) Exchange exchange, @Qualifier(ESQueue)Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("es").noargs();
    }




}
