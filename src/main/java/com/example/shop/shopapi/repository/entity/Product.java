package com.example.shop.shopapi.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Product {
    @Id
    @Column(length = 10)
    private String sku;
    @Column(length = 25)
    private String name;
    private String description;
    @Column(name = "price_in_cents")
    private Integer priceInCents;
}
