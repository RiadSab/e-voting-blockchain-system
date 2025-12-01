package com.evote.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;


@Configuration
public class ContractConfig {

    private static final Logger log = LoggerFactory.getLogger(ContractConfig.class);

    @Value("${contract.deployment-info-path}")
    private String deploymentInfoPath;

    @Bean(name = "deployedContractAddress")
    public String deployedContractAddress()   throws RuntimeException {
        File jsonFile = new File(deploymentInfoPath);
        log.info("Reading foundry deployment info from: {}", deploymentInfoPath);
        if(!jsonFile.exists()){
            throw new RuntimeException("deployment-info.json not found");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode transactions = rootNode.get("transactions");

            if(transactions == null || !transactions.isArray()){
                throw new RuntimeException("Invalid deployment-info.json format: 'transactions' field is missing or not an array");
            }

            for(JsonNode transaction : transactions){
                if("CREATE".equals(transaction.get("transactionType").asString())){
                    String address = transaction.get("contractAddress").asString();
                    log.info("Deploying Contract Address {}", address);
                    return address;
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to parse foundry JSON file", e);
        }
        throw new RuntimeException("No CREATE transaction found in artifact");

    }
}