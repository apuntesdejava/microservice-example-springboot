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

import com.storeonline.commons.reactive.service.ReactiveCrudService;
import com.storeonline.product.application.repositories.ProductCategoryCrudRepository;
import com.storeonline.product.domain.models.ProductCategory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Service
public class ProductCategoryReactiveCrudService extends ReactiveCrudService<ProductCategory, Integer> {

    public ProductCategoryReactiveCrudService(ProductCategoryCrudRepository repository) {
        super(repository);
    }

    public Mono<ProductCategory> updateEnabled(int categoryId, boolean enabled) {
        return getRepository().updateEnabled(categoryId, enabled);
    }

    @Override
    protected ProductCategoryCrudRepository getRepository() {
        return (ProductCategoryCrudRepository) super.getRepository();
    }

}
