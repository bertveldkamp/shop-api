package it.com.example.shop.shopapi.controller;

import com.example.shop.shopapi.ShopApiApplication;
import com.example.shop.shopapi.repository.ProductRepository;
import com.example.shop.shopapi.repository.entity.Product;
import it.com.example.shop.shopapi.config.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles({"integration-test"})
@ContextConfiguration(classes = IntegrationTestConfig.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = ShopApiApplication.class)
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.saveAll(List.of(Product.builder()
                .sku("123STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build(), Product.builder()
                .sku("321STK")
                .name("Lorem")
                .description("Lorem ipsum dolor sit amet.")
                .priceInCents(100)
                .build()));
    }

    @Test
    void givenNoMatchingProducts_whenSearchProduct_shouldReturnEmptyList() throws Exception {
        mockMvc
                .perform(get("/product").queryParam("searchCriteria", "test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void givenMatchingProduct_whenSearchProduct_shouldReturnProduct() throws Exception {
        mockMvc
                .perform(get("/product").queryParam("searchCriteria", "321S"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].sku").value("321STK"))
                .andExpect(jsonPath("$[0].name").value("Lorem"))
                .andExpect(jsonPath("$[0].description").value("Lorem ipsum dolor sit amet."))
                .andExpect(jsonPath("$[0].priceInCents").value(100));
    }

    @Test
    void givenMultipleMatchingProducts_whenSearchProduct_shouldReturnAllMatchingProducts() throws Exception {
        mockMvc
                .perform(get("/product").queryParam("searchCriteria", "Lorem"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Lorem"))
                .andExpect(jsonPath("$[1].name").value("Lorem"));
    }
}
