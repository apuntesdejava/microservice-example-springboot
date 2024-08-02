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

import com.storeonline.product.application.repositories.ProductCategoryCrudRepository;
import com.storeonline.product.application.services.ProductCategoryReactiveCrudService;
import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.handler.ProductCategoryHandler;
import com.storeonline.product.infrastructure.handler.request.ProductCategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProductCategoryRouterTest {

    @Mock
    ProductCategoryCrudRepository repository;

    @InjectMocks
    ProductCategoryReactiveCrudService service;

    private WebTestClient webClient;

    @BeforeEach
    public void setUp() {
        var productCategoryHandler = new ProductCategoryHandler(service);
        RouterFunction<?> routes = new RouterConfiguration()
                .productCategoryRoutes(productCategoryHandler);
        webClient = WebTestClient.bindToRouterFunction(routes)
                .build();
    }

    @Test
    void testCreateCategory() {
        var modelCreate = ProductCategory.builder()
                .name("Category 1")
                .enabled(true)
                .build();
        var response = ProductCategory.builder()
                .name("Category 1")
                .enabled(true)
                .categoryId(1)
                .build();
        var request = new ProductCategoryRequest("Category 1", true);

        when(repository.create(modelCreate))
                .thenReturn(Mono.just(response));

        webClient.post()
                .uri("/category")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.categoryId").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Category 1")
                .jsonPath("$.enabled").isEqualTo(true);

    }

    @Test
    void testFindById() {
        var model = ProductCategory.builder()
                .name("Category 1")
                .enabled(true)
                .categoryId(1)
                .build();
        when(repository.findById(1))
                .thenReturn(Mono.just(model));

        webClient.get()
                .uri("/category/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.categoryId").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Category 1")
                .jsonPath("$.enabled").isEqualTo(true);

    }

    @Test
    void testListCategories() {
        var models = List.of(ProductCategory.builder()
                .name("Category 1")
                .enabled(true)
                .build(),
                ProductCategory.builder()
                        .name("Category 2")
                        .enabled(false)
                        .build()
        );
        var modelsFlux = Flux.fromIterable(models);
        when(repository.listAll())
                .thenReturn(modelsFlux);
        webClient.get()
                .uri("/category")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductCategory.class)
                .isEqualTo(models);

    }

    @Test
    void testUpdateEnabled() {
        var category = ProductCategory.builder()
                .name("Category 1")
                .enabled(false)
                .categoryId(1)
                .build();
        var request = new ProductCategoryRequest("Cat", false);
        when(repository.updateEnabled(1, false))
                .thenReturn(Mono.just(category));
        webClient.put()
                .uri("/category/1/enabled")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Category 1")
                .jsonPath("$.enabled").isEqualTo(false);
    }
}
