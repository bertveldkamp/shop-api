package com.example.shop.shopapi.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Slf4j
@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalAwsConfig {
    private static final String SERVICE_ENDPOINT = "http://localhost:4566";
    private static final String ACCESS_KEY = "dummy";
    private static final String SECRET_KEY = "dummy";

    @Value("${spring.cloud.aws.region.static}")
    private String region;
    @Value("${app.aws.sqs-name}")
    private String queueName;

    @Bean(destroyMethod = "close")
    public SqsAsyncClient sqsAsyncClient(){

        SqsAsyncClient asyncClient = SqsAsyncClient.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY))
                .endpointOverride(URI.create(SERVICE_ENDPOINT))
                .region(Region.of(region))
                .build();

        asyncClient.createQueue(builder -> builder.queueName(queueName).build());

        return asyncClient;
    }
}
