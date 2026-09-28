package com.taskflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy // <-- КРИТИЧЕСКИ ВАЖНО: включает магию AOP в Spring
public class AppConfig {
}
