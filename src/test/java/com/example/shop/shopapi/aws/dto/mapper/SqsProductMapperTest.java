package com.example.shop.shopapi.aws.dto.mapper;

import com.example.shop.shopapi.aws.dto.ProductDto;
import com.example.shop.shopapi.config.MockAwsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(MockAwsConfig.class)
@ActiveProfiles("unit-test")
class SqsProductMapperTest {
    @Autowired
    private SqsProductMapper sqsProductMapper;

    @Test
    void givenProduct_whenCallingToProductDto_shouldMapAllFields() {
        var productDto = ProductDto.builder()
                .sku("123STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build();

        assertThat(sqsProductMapper.toProduct(productDto))
                .matches(product -> "123STK".equals(product.getSku()))
                .matches(product -> "Lorem".equals(product.getName()))
                .matches(product -> "Lorem ipsum dolor sit amet.".equals(product.getDescription()))
                .matches(product -> 100 == product.getPriceInCents());
    }
}