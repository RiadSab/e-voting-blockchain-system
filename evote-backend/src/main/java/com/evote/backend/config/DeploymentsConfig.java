package com.evote.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.math.BigInteger;


@Configuration
public class DeploymentsConfig {

    private static final Logger log = LoggerFactory.getLogger(DeploymentsConfig.class);

    @Value("${contract.deployment-info-path}")
    private String deploymentInfoPath;

    @Bean
    public DeploymentsInfo deploymentsInfo(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(deploymentInfoPath);
            JsonNode rootNode = objectMapper.readTree(file);

            BigInteger chainId = new BigInteger(getFieldAsText(rootNode, "chainId"));
            String network = getFieldAsText(rootNode, "network");
            String factory = getFieldAsText(rootNode, "factory");
            String tallyVerifier = getOptionalFieldAsText(rootNode, "tallyVerifier");

            log.info("Loaded deployments info for network '{}', chainId {}", network, chainId);

            return new DeploymentsInfo(chainId, network, factory, tallyVerifier);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load deployments info from path: " + deploymentInfoPath, e);
        }
    }

    private static String getFieldAsText(JsonNode node, String fieldName) {
        if(!node.hasNonNull(fieldName)) {
            throw new IllegalStateException("Missing field '" + fieldName + "' in deployments info");
        }
        return node.get(fieldName).asString();
    }

    private static String getOptionalFieldAsText(JsonNode node, String fieldName) {
        if(!node.has(fieldName) || node.get(fieldName).isNull()) {
            return null;
        }
        return node.get(fieldName).asString();
    }
}