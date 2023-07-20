package it.com.example.shop.shopapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

@TestConfiguration
public class IntegrationTestConfig {
    @Value("${app.aws.sqs-name}")
    private String queueName;

    @Bean(destroyMethod = "stop")
    public LocalStackContainer localStackContainer(){
        final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:2.1.0")).withServices(SQS);
        localStackContainer.start();

        return localStackContainer;
    }

    @Bean(destroyMethod = "close")
    public SqsAsyncClient sqsAsyncClient(LocalStackContainer localStackContainer){

        SqsAsyncClient asyncClient = SqsAsyncClient.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(localStackContainer.getAccessKey(), localStackContainer.getSecretKey()))
                .endpointOverride(localStackContainer.getEndpoint())
                .region(Region.of(localStackContainer.getRegion()))
                .build();

        asyncClient.createQueue(builder -> builder.queueName(queueName).build());

        return asyncClient;
    }
}