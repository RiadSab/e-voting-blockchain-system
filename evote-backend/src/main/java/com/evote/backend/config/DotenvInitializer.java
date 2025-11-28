package com.evote.backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final Logger log = LoggerFactory.getLogger(DotenvInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        try {
            // 1. Load the .env file
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            // 2. Convert to a Map
            Map<String, Object> envMap = new HashMap<>();
            dotenv.entries().forEach(entry -> envMap.put(entry.getKey(), entry.getValue()));

            // 3. Add to Spring's Environment (High Priority)
            environment.getPropertySources()
                    .addFirst(new MapPropertySource("dotenvProperties", envMap));

            log.info("DotenvInitializer: Loaded {} variables from .env", envMap.size());

        } catch (Exception e) {
            log.info("DotenvInitializer: No .env file found (using system env vars)");
        }
    }
}