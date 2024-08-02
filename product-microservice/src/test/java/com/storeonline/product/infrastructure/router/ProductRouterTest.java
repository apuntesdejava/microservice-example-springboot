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
package com.storeonline.product.infrastructure.router;

import com.storeonline.product.application.repositories.ProductCrudRepository;
import com.storeonline.product.application.services.ProductReactiveCrudService;
import com.storeonline.product.domain.models.Product;
import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.handler.ProductHandler;
import com.storeonline.product.infrastructure.handler.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@ExtendWith(SpringExtension.class)
class ProductRouterTest {

    @Mock
    ProductCrudRepository repository;

    @InjectMocks
    ProductReactiveCrudService service;

    WebTestClient webClient;

    @BeforeEach
    public void setUp() {
        var productHandler = new ProductHandler(service);
        RouterFunction<?> routes = new RouterConfiguration()
                .productRoutes(productHandler);
        webClient = WebTestClient.bindToRouterFunction(routes)
                .build();
    }

    @Test
    void testCreateProduct() {
        var requestToCreate = Product.builder()
                .name("Product 1")
                .category(
                        ProductCategory.builder()
                                .categoryId(1)
                                .build()
                )
                .salePrice(100)
                .build();
        var responseToCreate = Product.builder()
                .name("Product 1")
                .productId(1)
                .category(
                        ProductCategory.builder()
                                .categoryId(1)
                                .enabled(true)
                                .name("Category 1")
                                .build()
                )
                .salePrice(100)
                .build();
        var request = new ProductRequest("Product 1", 1, 100);

        when(repository.create(requestToCreate))
                .thenReturn(Mono.just(responseToCreate));

        webClient.post()
                .uri("/product")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Product 1")
                .jsonPath("$.salePrice").isEqualTo(100)
                .jsonPath("$.category.categoryId").isEqualTo(1)
                .jsonPath("$.category.name").isEqualTo("Category 1")
                .jsonPath("$.category.enabled").isEqualTo(true);

    }
}
