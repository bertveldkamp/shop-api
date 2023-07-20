package com.example.shop.shopapi.aws.sqs;

import com.example.shop.shopapi.aws.dto.ProductUpdateDto;
import com.example.shop.shopapi.aws.dto.mapper.SqsProductMapper;
import com.example.shop.shopapi.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductUpdateListener {
    private final ObjectMapper objectMapper;
    private final SqsProductMapper sqsProductMapper;
    private final ProductService productService;

    @SqsListener(value = "${app.aws.sqs-name}")
    public void listen(final String message) {
        try {
            var productUpdate = objectMapper.readValue(message, ProductUpdateDto.class);
            productService.mutateProduct(sqsProductMapper.toProduct(productUpdate.getProductDto()),
                    productUpdate.isDeleted());

        } catch (JsonProcessingException e) {
            log.error("Parsing error for message {}", message, e);
        }
    }
}
