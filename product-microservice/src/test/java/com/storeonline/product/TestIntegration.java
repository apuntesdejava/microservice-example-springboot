/*
 * Copyright 2024 Diego Silva (diego.silva at apuntesdejava.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.storeonline.product;

import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.handler.request.ProductCategoryRequest;
import com.storeonline.product.infrastructure.handler.request.ProductRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Testcontainers
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.ALWAYS
)
@ActiveProfiles("mysql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestIntegration {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest");

    @Autowired
    WebTestClient webTestClient;

    @BeforeAll
    static void init() {
        mysql.start();
    }

    @Test
    @Order(1)
    void createCategoryTest() {
        var request = new ProductCategoryRequest("Category 1", true);
        webTestClient.post()
                .uri("/category")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Category 1")
                .jsonPath("$.enabled").isEqualTo(true)
                .jsonPath("$.categoryId").isEqualTo(1);
    }

    @Test
    @Order(2)
    void testFindById() {
        webTestClient.get()
                .uri("/category/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Category 1")
                .jsonPath("$.enabled").isEqualTo(true)
                .jsonPath("$.categoryId").isEqualTo(1);
    }

    @Test
    @Order(3)
    void listCategoriesTest() {
        webTestClient.get()
                .uri("/category")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductCategory.class)
                .value(result -> {
                    assertFalse(result.isEmpty());
                });
    }

    @Test
    @Order(4)
    void createProductTest() {
        var request = new ProductRequest("Product 1", 1, 100.0);
        webTestClient.post()
                .uri("/product")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(1);
    }

    @Test
    @Order(5)
    void testFindProductById() {
        webTestClient.get()
                .uri("/product/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Product 1");
    }

    @Test
    @Order(6)
    void testDeleteProduct() {
        webTestClient.delete()
                .uri("/product/1")
                .exchange()
                .expectStatus().isOk();
    }

}
