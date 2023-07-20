package com.example.shop.shopapi.repository;

import com.example.shop.shopapi.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = """
        select * from products p
         where name like %:search%
         or description like %:search%
         or sku like %:search%
         or cast(price_in_cents as varchar) like %:search%
    """,
    nativeQuery = true)
    List<Product> findAllBySearchCriteria(String search);

    void deleteProductBySku(String sku);
}
