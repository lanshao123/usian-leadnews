package com.usian.comment.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.usian.common.web.app")
public class SecurityConfig {
}
