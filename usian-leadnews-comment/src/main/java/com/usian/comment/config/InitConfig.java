package com.usian.comment.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.usian.common.jackson","com.usian.common.aliyun","com.usian.common.exception"})
public class InitConfig {
}