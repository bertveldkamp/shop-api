package com.example.shop.shopapi.controller;

import com.example.shop.shopapi.controller.dto.ProductDto;
import com.example.shop.shopapi.controller.dto.mapper.RestProductMapper;
import com.example.shop.shopapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;
    private final RestProductMapper restProductMapper;

    @GetMapping("/product")
    public ResponseEntity<List<ProductDto>> searchProduct(@RequestParam("searchCriteria") String searchCriteria) {
        return ResponseEntity.ok(productService.searchProducts(searchCriteria)
                .stream()
                .map(restProductMapper::toProductDto)
                .toList());
    }

}
