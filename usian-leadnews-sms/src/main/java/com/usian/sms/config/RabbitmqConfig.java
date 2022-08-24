package com.usian.sms.config;

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
    //验证码交换机
    public static final String SMSEXCHANGE = "SMSExchange";
    //验证码队列
    public static final String SMSQUEUE = "SMSQueue";


    //配置交换机
    @Bean(SMSEXCHANGE)
    public Exchange authExchange() {
        //交换机是否持久化durable(true)
        return ExchangeBuilder.directExchange(SMSEXCHANGE).durable(true).build();
    }
    //配置队列
    @Bean(SMSQUEUE)
    public Queue authQueue() {
        return new Queue(SMSQUEUE);
    }
    //交换机和队列绑定
    @Bean
    public Binding bingDingAuthExchange_AuthQueue(@Qualifier(SMSEXCHANGE) Exchange exchange, @Qualifier(SMSQUEUE)Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("sms").noargs();
    }


}
