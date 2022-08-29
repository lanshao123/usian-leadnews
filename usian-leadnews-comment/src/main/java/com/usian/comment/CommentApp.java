package com.usian.comment;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @program: usian-leadnews
 * @description: WemediaApp
 * @author: wangheng
 * @create: 2022-08-03 15:30
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CommentApp {
    public static void main(String[] args) {
        SpringApplication.run(CommentApp.class,args);
    }

}
