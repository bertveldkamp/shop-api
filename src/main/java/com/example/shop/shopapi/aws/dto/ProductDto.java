package com.example.shop.shopapi.aws.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private String sku;
    private String name;
    private String description;
    private Integer priceInCents;
}
