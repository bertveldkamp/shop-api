package com.example.shop.shopapi.controller.dto.mapper;

import com.example.shop.shopapi.repository.entity.Product;
import com.example.shop.shopapi.controller.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(
        componentModel = "spring",
        injectionStrategy = CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface RestProductMapper {
    ProductDto toProductDto(Product product);
}
