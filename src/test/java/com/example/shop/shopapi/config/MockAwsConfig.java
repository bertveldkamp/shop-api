package com.example.shop.shopapi.config;

import com.example.shop.shopapi.aws.sqs.ProductUpdateListener;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@TestConfiguration
public class MockAwsConfig {
    @MockBean
    private SqsAsyncClient sqsAsyncClient;

    @MockBean
    private ProductUpdateListener productUpdateListener;
}
