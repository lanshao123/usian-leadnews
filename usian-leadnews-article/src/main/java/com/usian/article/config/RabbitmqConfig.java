package com.usian.article.config;

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
    //修改文章配置交换机
    public static final String APARTICLECONFIGEXCHANGE = "APARTICLECONFIGEXCHANGE";
    //修改文章配置队列
    public static final String APARTICLECONFIGQUEUE = "APARTICLECONFIGQUEUE";

    //配置交换机
    @Bean(APARTICLECONFIGEXCHANGE)
    public Exchange authExchange() {
        //交换机是否持久化durable(true)
        return ExchangeBuilder.directExchange(APARTICLECONFIGEXCHANGE).durable(true).build();
    }
    //配置队列
    @Bean(APARTICLECONFIGQUEUE)
    public Queue authQueue() {
        return new Queue(APARTICLECONFIGQUEUE);
    }
    //交换机和队列绑定
    @Bean
    public Binding bingDingAuthExchange_AuthQueue(@Qualifier(APARTICLECONFIGEXCHANGE) Exchange exchange, @Qualifier(APARTICLECONFIGQUEUE)Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("aparticleconfig").noargs();
    }


}
