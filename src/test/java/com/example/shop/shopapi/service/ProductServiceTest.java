package com.example.shop.shopapi.service;

import com.example.shop.shopapi.repository.ProductRepository;
import com.example.shop.shopapi.repository.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void givenProductUpdateDeleteWithoutSku_whenMutateProduct_thenThrowException() {
        assertThrows(NullPointerException.class,
                () -> productService.mutateProduct(Product.builder().build(), true));
        assertThrows(NullPointerException.class,
                () -> productService.mutateProduct(Product.builder().build(), false));

        verifyNoInteractions(productRepository);
    }

    @Test
    void givenProductUpdateDelete_whenMutateProduct_thenDeleteProduct() {
        productService.mutateProduct(Product.builder().sku("123STK").build(), true);

        verify(productRepository).deleteProductBySku("123STK");
    }

    @Test
    void givenProductUpdate_whenMutateProduct_thenUpdateProduct() {
        productService.mutateProduct(Product.builder()
                .sku("123STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build(), false);

        verify(productRepository).save(Product.builder()
                .sku("123STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build());
    }
}