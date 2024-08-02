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
package com.storeonline.product.infrastructure.repositories;

import com.storeonline.product.domain.models.Product;
import com.storeonline.product.infrastructure.entities.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
public interface R2dbcProductRepository extends R2dbcRepository<ProductEntity, Integer> {

    @Query("SELECT * FROM product WHERE category_id=:categoryId")
    Flux<Product> listByCategoryId(int categoryId);
}
