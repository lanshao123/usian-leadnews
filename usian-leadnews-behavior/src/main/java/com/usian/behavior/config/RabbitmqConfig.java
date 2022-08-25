package com.usian.behavior.config;

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
    public static final String BEHAVIORExchange = "behaviorExchange";
    public static final String BEHAVIORQUEUE = "behaviorQueue";

    //配置交换机
    @Bean(BEHAVIORExchange)
    public Exchange authExchange() {
        //交换机是否持久化durable(true)
        return ExchangeBuilder.directExchange(BEHAVIORExchange).durable(true).build();
    }

    //配置队列
    @Bean(BEHAVIORQUEUE)
    public Queue authQueue() {
        return new Queue(BEHAVIORQUEUE);
    }

    //交换机和队列绑定
    @Bean
    public Binding bingDingAuthExchange_AuthQueue(@Qualifier(BEHAVIORExchange) Exchange exchange, @Qualifier(BEHAVIORQUEUE)Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("behavior").noargs();
    }
}
