package com.evote.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;


@Configuration
public class ContractConfig {

    private static final Logger log = LoggerFactory.getLogger(ContractConfig.class);

    @Bean(name = "deployedContractAddress")
    public String deployedContractAddress()   throws RuntimeException {
        File jsonFile = new File("../deployment-info.json");
        if(!jsonFile.exists()){
            throw new RuntimeException("deployment-info.json not found");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonFile);
        String address = jsonNode.path("address").asText();

        log.info("deployedContractAddress: {}", address);
        return address;
    }
}