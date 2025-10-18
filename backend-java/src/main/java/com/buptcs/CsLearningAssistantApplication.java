package com.buptcs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * CS学习助手应用主类
 * 
 * @author BUPTcs Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableRedisRepositories
public class CsLearningAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsLearningAssistantApplication.class, args);
    }
}