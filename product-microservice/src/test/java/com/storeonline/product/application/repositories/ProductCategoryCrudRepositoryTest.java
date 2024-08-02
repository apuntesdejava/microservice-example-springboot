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

import com.storeonline.product.domain.models.ProductCategory;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@SpringBootTest
@ActiveProfiles(value = "h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductCategoryCrudRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryCrudRepositoryTest.class);

    @Autowired
    ProductCategoryCrudRepository productCategoryCrudRepository;

    @Test
    @Order(1)
    void createCategory() {
        var model = ProductCategory.builder()
                .name("Category 1")
                .enabled(true)
                .build();
        productCategoryCrudRepository.create(model).subscribe(category -> assertTrue(category.getCategoryId() > 0));
    }

    @Test
    @Order(2)
    void updateEnabled() {
        var categories = productCategoryCrudRepository.listAll();
        categories.filter(ProductCategory::isEnabled).next().subscribe(category -> {
            LOGGER.debug("category found:{}", category);

            productCategoryCrudRepository.updateEnabled(category.getCategoryId(), false)
                    .subscribe(updated -> assertFalse(updated.isEnabled()));

        });
    }

    @Test
    @Order(3)
    void update() {
        var categories = productCategoryCrudRepository.listAll();
        categories.next().subscribe(category -> {
            LOGGER.debug("category found to update:{}", category);
            category.setName("NEW CATEGORY NAME");

            productCategoryCrudRepository.update(category.getCategoryId(), category)
                    .subscribe(updated -> assertEquals("NEW CATEGORY NAME", updated.getName()));

        });
    }

    @Test
    @Order(10)
    void deleteCategories() {
        var categories = productCategoryCrudRepository.listAll();
        categories.subscribe(category -> {
            LOGGER.debug("delete category:{}", category);
            productCategoryCrudRepository.delete(category.getCategoryId())
                    .subscribe(v -> productCategoryCrudRepository.findById(category.getCategoryId())
                            .hasElement()
                            .subscribe(Assertions::assertFalse));

        });
    }
}
