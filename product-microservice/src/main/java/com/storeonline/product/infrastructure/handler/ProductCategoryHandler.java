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
package com.storeonline.product.infrastructure.handler;

import com.storeonline.product.application.services.ProductCategoryReactiveCrudService;
import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.handler.request.ProductCategoryRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Component
public class ProductCategoryHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryHandler.class);

    private final ProductCategoryReactiveCrudService service;

    public ProductCategoryHandler(ProductCategoryReactiveCrudService service) {
        this.service = service;
    }

    public Mono<ServerResponse> listAll() {
        var categories = service.listAll();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(categories, ProductCategory.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ProductCategoryRequest.class)
                .map(productCategoryRequest -> {
                    LOGGER.debug("productCategoryRequest:{}", productCategoryRequest);
                    return ProductCategory.builder()
                            .enabled(productCategoryRequest.enabled())
                            .name(productCategoryRequest.name())
                            .build();

                }).flatMap(service::create)
                .flatMap(model -> {
                    try {
                        var uri = new URI("/category/" + model.getCategoryId());
                        return ServerResponse.created(uri)
                                .body(BodyInserters.fromValue(model));
                    } catch (URISyntaxException e) {
                        return ServerResponse.status(501).build();
                    }
                });

    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        var id = NumberUtils.parseNumber(request.pathVariable("id"), Integer.class);
        LOGGER.debug("findById:{}", id);
        var category = service.findById(id);

        return category
                .flatMap(cat -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(cat)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> updateEnabled(ServerRequest request) {
        var id = NumberUtils.parseNumber(request.pathVariable("id"), Integer.class);
        return request.bodyToMono(ProductCategoryRequest.class)
                .flatMap(categoryRequest -> service.updateEnabled(id, categoryRequest.enabled())
                        .flatMap(ServerResponse.ok()::bodyValue));
    }
}
