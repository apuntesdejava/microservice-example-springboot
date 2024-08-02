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

import com.storeonline.product.application.repositories.ProductCrudRepository;
import com.storeonline.product.domain.models.Product;
import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.entities.ProductEntity;
import com.storeonline.product.infrastructure.mapper.ProductCategoryMapper;
import com.storeonline.product.infrastructure.mapper.ProductMapper;
import com.storeonline.product.infrastructure.repositories.R2dbcProductCategoryRepository;
import com.storeonline.product.infrastructure.repositories.R2dbcProductRepository;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class R2dbcProductAdapter implements ProductCrudRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(R2dbcProductAdapter.class);

    private final R2dbcProductRepository repository;
    private final R2dbcProductCategoryRepository categoryRepository;

    private final ProductMapper mapper;
    private final ProductCategoryMapper categoryMapper;

    public R2dbcProductAdapter(R2dbcProductRepository repository, R2dbcProductCategoryRepository categoryRepository,
            ProductMapper mapper, ProductCategoryMapper categoryMapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Flux<Product> listByCategoryId(int categoryId) {
        return repository.listByCategoryId(categoryId);
    }

    @Override
    public Mono<Product> create(Product model) {
        var entity = mapper.modelToEntity(model);
        return repository.save(entity)
                .flatMap(this::mapping);
    }

    private Mono<Product> mapping(ProductEntity productEntity) {
        var product = mapper.entityToModel(productEntity);
        if (productEntity.categoryId() == 0) {
            return Mono.just(product);
        }
        return categoryRepository.findById(productEntity.categoryId())
                .map(category -> {
                    var categoryEntity = categoryMapper.entityToModel(category);
                    LOGGER.debug("Category found: {}", categoryEntity);
                    product.setCategory(categoryEntity);
                    return product;
                });
    }

    @Override
    public Mono<Product> findById(Integer integer) {
        var entity = repository.findById(integer);
        return entity.map(mapper::entityToModel);
    }

    @Override
    public Flux<Product> listAll() {
        return repository.findAll().map(mapper::entityToModel);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Product> update(Integer id, Product newModel) {
        return repository.findById(id)
                .flatMap(found -> {
                    var categoryId = Optional.ofNullable(newModel.getCategory())
                            .map(ProductCategory::getCategoryId)
                            .orElse(0);
                    var newDate = new ProductEntity(id, categoryId, newModel.getName(),
                            newModel.getSalePrice());
                    return repository.save(newDate).map(mapper::entityToModel);
                }).switchIfEmpty(Mono.empty());
    }
}
