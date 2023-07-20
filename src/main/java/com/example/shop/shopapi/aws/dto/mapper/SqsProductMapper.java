package com.example.shop.shopapi.aws.dto.mapper;


import com.example.shop.shopapi.aws.dto.ProductDto;
import com.example.shop.shopapi.repository.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(
        componentModel = "spring",
        injectionStrategy = CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface SqsProductMapper {
    Product toProduct(ProductDto productDto);
}
