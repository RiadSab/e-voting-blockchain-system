package com.evote.backend.config;

import com.evote.backend.entitiy.records.RetryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BlockchainConfig {
    @Bean
    public RetryPolicy retryPolicy() {
        return new RetryPolicy(
                3,
                Duration.ofMillis(250),
                Duration.ofSeconds(3)
        );
    }
}
