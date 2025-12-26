package com.evote.backend;

import com.evote.backend.config.springConfig.DotenvInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EvoteBackendApplication {

	public static void main(String[] args) {
        new SpringApplicationBuilder(EvoteBackendApplication.class)
                .initializers(new DotenvInitializer())
                .run(args);
    }

}
