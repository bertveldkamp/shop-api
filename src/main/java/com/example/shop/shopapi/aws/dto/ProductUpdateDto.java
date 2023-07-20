package com.example.shop.shopapi.aws.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ProductUpdateDto {
    @JsonProperty("product")
    private ProductDto productDto;
    private boolean isDeleted;
}
