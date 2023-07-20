package it.com.example.shop.shopapi.aws.sqs;

import com.example.shop.shopapi.ShopApiApplication;
import com.example.shop.shopapi.repository.ProductRepository;
import com.example.shop.shopapi.repository.entity.Product;
import it.com.example.shop.shopapi.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.PurgeQueueRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@ActiveProfiles("integration-test")
@ContextConfiguration(classes = IntegrationTestConfig.class)
@SpringBootTest(classes = ShopApiApplication.class)
public class ProductUpdateListenerIT {
    @Autowired
    private SqsAsyncClient sqsClient;

    @Autowired
    private ProductRepository productRepository;

    @Value("${app.aws.sqs-name}")
    private String queueName;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();
        sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build())
                .thenAccept(response -> sqsClient.purgeQueue(PurgeQueueRequest.builder().queueUrl(response.queueUrl()).build()));
    }

    @Test
    void givenInvalidMessageContent_whenListeningOnSqs_ShouldDiscard() {
        var message = "{INVALID}";

        sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build())
                .thenAccept(response -> sqsClient.sendMessage(SendMessageRequest.builder()
                        .queueUrl(response.queueUrl()).messageBody(message).build()));

    }

    @Test
    void givenValidUpdateMessage_whenListeningOnSqs_ShouldUpdateArticle() {
        var message = """
                {
                    "isDeleted": "false",
                    "product": {
                        "sku": "123STK",
                        "name": "Lorem",
                        "description": "Lorem ipsum dolor sit amet.",
                        "priceInCents": 100
                    }
                }
                """;

        sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build())
                .thenAccept(response -> sqsClient.sendMessage(SendMessageRequest.builder()
                        .queueUrl(response.queueUrl()).messageBody(message).build()));

        await().atMost(3, SECONDS)
                .untilAsserted(() -> assertThat(productRepository.findAll())
                        .hasSize(1)
                        .contains(Product.builder()
                                .sku("123STK")
                                .name("Lorem")
                                .description("Lorem ipsum dolor sit amet.")
                                .priceInCents(100)
                                .build()));
    }

    @Test
    void givenValidDeleteMessage_whenListeningOnSqs_ShouldUpdateArticle() {
        var message = """
                {
                    "isDeleted": "true",
                    "product": {
                        "sku": "123STK"
                    }
                }
                """;

        productRepository.saveAll(List.of(Product.builder()
                .sku("123STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build(), Product.builder()
                .sku("321STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build()));

        sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build())
                .thenAccept(response -> sqsClient.sendMessage(SendMessageRequest.builder()
                        .queueUrl(response.queueUrl()).messageBody(message).build()));

        await().atMost(3, SECONDS)
                .untilAsserted(() -> assertThat(productRepository.findAll())
                        .hasSize(1)
                        .element(0)
                        .matches(product -> "321STK".equals(product.getSku())));
    }
}
