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
package com.storeonline.product.application.services;

import com.storeonline.product.application.repositories.ProductCrudRepository;
import com.storeonline.product.domain.models.Product;
import com.storeonline.product.domain.models.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class ProductReactiveCrudServiceTest {

    @Mock
    ProductCrudRepository productRepository;

    @InjectMocks
    ProductReactiveCrudService productService;

    private void initData() {
        var category = ProductCategory.builder()
                .enabled(true)
                .name("Category 1")
                .categoryId(1)
                .build();

        var product = Product.builder()
                .productId(100)
                .category(category)
                .name("Product 1")
                .salePrice(100.0)
                .build();

        var products = Flux.just(product);

        Mockito.when(productRepository.listByCategoryId(1))
                .thenReturn(products);
    }

    @Test
    void findByCategoryId() {
        initData();
        var products = productService.listByCategoryId(1);

        products.hasElements().subscribe(Assertions::assertTrue);

        products.any(prod -> prod.getName().equals("Product 1"))
                .subscribe(Assertions::assertTrue);

    }
}
