package com.example.shop.shopapi.controller.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String sku;
    private String name;
    private String description;
    private Integer priceInCents;
}
