package com.usian.sms;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: usian-leadnews
 * @description: AdminApp
 * @author: wangheng
 * @create: 2022-08-03 15:30
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class SmsApp {
    public static void main(String[] args) {
        SpringApplication.run(SmsApp.class,args);
    }
    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
