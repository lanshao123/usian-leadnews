package com.usian.article;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: usian-leadnews
 * @description: WemediaApp
 * @author: wangheng
 * @create: 2022-08-03 15:30
 **/
@SpringBootApplication
@MapperScan("com.usian.article.mapper")
@EnableDiscoveryClient
public class ArticleApp {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApp.class,args);
    }
    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
