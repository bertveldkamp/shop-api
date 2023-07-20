package com.example.shop.shopapi;

import com.example.shop.shopapi.config.MockAwsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Import(MockAwsConfig.class)
@ActiveProfiles("unit-test")
class ShopApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
