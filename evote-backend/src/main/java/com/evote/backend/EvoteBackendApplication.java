package com.evote.backend;

import com.evote.backend.config.bootstrap.DotenvInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class EvoteBackendApplication {

	public static void main(String[] args) {
        new SpringApplicationBuilder(EvoteBackendApplication.class)
                .initializers(new DotenvInitializer())
                .run(args);
    }

}
