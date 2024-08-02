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

import com.storeonline.product.application.repositories.ProductCategoryCrudRepository;
import com.storeonline.product.domain.models.ProductCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@ExtendWith(MockitoExtension.class)
class ProductCategoryReactiveCrudServiceTest {

    @Mock
    ProductCategoryCrudRepository categoryRepository;

    @InjectMocks
    ProductCategoryReactiveCrudService categoryService;

    @Test
    void findById() {
        var model = ProductCategory.builder()
                .enabled(true)
                .name("Category 1")
                .categoryId(1)
                .build();
        when(categoryRepository.findById(1))
                .thenReturn(Mono.just(model));

        var category = categoryService.findById(1);

        category.hasElement().subscribe(Assertions::assertTrue);
        category.subscribe(cat -> assertEquals("Category 1", cat.getName()));
    }

    @Test
    void create() {
        var category = ProductCategory.builder()
                .enabled(true)
                .name("Category 2")
                .categoryId(2)
                .build();
        var request = ProductCategory.builder()
                .enabled(true)
                .name("Category 2")
                .build();
        when(categoryRepository.create(request))
                .thenReturn(Mono.just(category));

        categoryService.create(request).subscribe(aCategory -> {
            assertEquals(2, aCategory.getCategoryId());
        });
    }

    @Test
    void updateTest() {
        var category = ProductCategory.builder()
                .enabled(true)
                .name("Category 1")
                .categoryId(2)
                .build();
        var request = ProductCategory.builder()
                .enabled(true)
                .name("Category 2")
                .build();
        when(categoryRepository.update(1, request))
                .thenReturn(Mono.just(category));

        categoryService.update(1, request).subscribe(aCategory -> {
            assertEquals(category.getName(), aCategory.getName());
        });
    }

    @Test
    void testDeleteById(){
        when(categoryRepository.delete(1))
                .thenReturn(Mono.empty());
        categoryService.delete(1).subscribe(result->{
           assertTrue(TRUE);
        });
    }
    
}
