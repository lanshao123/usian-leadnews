package com.usian.behavior.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ServletComponentScan("com.usian.common.web.behavior")
public class SecurityConfig {
}
