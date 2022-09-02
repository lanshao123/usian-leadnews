package com.usian.search.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.usian.common.threadpool","com.usian.common.exception"})
public class InitConfig {
}
