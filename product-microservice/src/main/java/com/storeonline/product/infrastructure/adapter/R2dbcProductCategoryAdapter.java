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
package com.storeonline.product.infrastructure.adapter;

import com.storeonline.product.application.repositories.ProductCategoryCrudRepository;
import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.entities.ProductCategoryEntity;
import com.storeonline.product.infrastructure.mapper.ProductCategoryMapper;
import com.storeonline.product.infrastructure.repositories.R2dbcProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Component
public class R2dbcProductCategoryAdapter implements ProductCategoryCrudRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(R2dbcProductCategoryAdapter.class);

    private final R2dbcProductCategoryRepository repository;
    private final ProductCategoryMapper mapper;

    public R2dbcProductCategoryAdapter(R2dbcProductCategoryRepository repository, ProductCategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<ProductCategory> updateEnabled(int categoryId, boolean enabled) {
        return repository.findById(categoryId)
                .flatMap(found -> {
                    var newData = new ProductCategoryEntity(found.categoryId(), found.name(), enabled);
                    return repository.save(newData).map(mapper::entityToModel);
                });
    }

    @Override
    public Mono<ProductCategory> findById(Integer id) {
        LOGGER.debug("get category with id {}", id);
        var entity = repository.findById(id);
        return entity.map(mapper::entityToModel);

    }

    @Override
    public Flux<ProductCategory> listAll() {
        return repository.findAll().map(mapper::entityToModel);
    }

    @Override
    public Mono<ProductCategory> create(ProductCategory model) {
        var entity = mapper.modelToEntity(model);
        var created = repository.save(entity);
        return created.map(mapper::entityToModel);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<ProductCategory> update(Integer id, ProductCategory newModel) {
        return repository.findById(id)
                .flatMap(found -> {
                    var newData = new ProductCategoryEntity(id, newModel.getName(), newModel.isEnabled());
                    return repository.save(newData).map(mapper::entityToModel);
                }).switchIfEmpty(Mono.empty());
    }

}
