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
package com.storeonline.product.application.repositories;

import com.storeonline.product.domain.models.Product;
import com.storeonline.product.domain.models.ProductCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ProductCrudRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCrudRepositoryTest.class);

    @Autowired
    ProductCrudRepository productCrudRepository;

    @Autowired
    ProductCategoryCrudRepository productCategoryCrudRepository;

    @Test
    @Order(1)
    void insertProduct() {
        var newCategory = ProductCategory.builder()
                .name("Category insert")
                .build();

        productCategoryCrudRepository.create(newCategory).subscribe(category -> {
            LOGGER.debug("Product category created: {}", category);
            var model = Product.builder()
                    .salePrice(100)
                    .category(category)
                    .name("Product 1")
                    .build();

            var created = productCrudRepository.create(model);
            created.subscribe(product -> {
                LOGGER.info("Product created: {}", product);
                assertNotNull(product.getCategory());
                assertTrue(product.getProductId() > 0);
            });
        });
    }

    @Test
    @Order(2)
    void listByCategory() {
        productCategoryCrudRepository.listAll().next().subscribe(category -> {
            var products = productCrudRepository.listByCategoryId(category.getCategoryId());
            products.count().subscribe(count -> assertTrue(count > 0));
        });
    }

    @Test
    @Order(3)
    void testListAll() {
        productCrudRepository.listAll()
                .subscribe(product -> {
                    assertNotNull(product.getName());
                });
    }

    @Test
    @Order(4)
    void testUpdate() {
        productCrudRepository.listAll()
                .next()
                .flatMap(product -> {
                    var newData = Product.builder()
                            .name("NEW PRODUCT")
                            .salePrice(200)
                            .build();
                    return productCrudRepository.update(product.getProductId(), newData);

                })
                .subscribe(productUpdated -> {
                    assertEquals("NEW PRODUCT", productUpdated.getName());
                });
    }

}
