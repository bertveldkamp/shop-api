package com.example.shop.shopapi.service;

import com.example.shop.shopapi.repository.ProductRepository;
import com.example.shop.shopapi.repository.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> searchProducts(final String searchCriteria) {
        return productRepository.findAllBySearchCriteria(searchCriteria);
    }

    @Transactional
    public void mutateProduct(final Product product, boolean isDeleted) {
        Objects.requireNonNull(product.getSku());
        if(isDeleted) {
            productRepository.deleteProductBySku(product.getSku());
            return;
        }
        productRepository.save(product);
    }
}
