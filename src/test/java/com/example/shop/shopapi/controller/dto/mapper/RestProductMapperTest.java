package com.example.shop.shopapi.controller.dto.mapper;

import com.example.shop.shopapi.config.MockAwsConfig;
import com.example.shop.shopapi.repository.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(MockAwsConfig.class)
@ActiveProfiles("unit-test")
class RestProductMapperTest {
    @Autowired
    private RestProductMapper restProductMapper;

    @Test
    void givenProduct_whenCallingToProductDto_shouldMapAllFields() {
        var product = Product.builder()
                .sku("123STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build();

        assertThat(restProductMapper.toProductDto(product))
                .matches(productDto -> "123STK".equals(productDto.getSku()))
                .matches(productDto -> "Lorem".equals(productDto.getName()))
                .matches(productDto -> "Lorem ipsum dolor sit amet.".equals(productDto.getDescription()))
                .matches(productDto -> 100 == productDto.getPriceInCents());
    }
}