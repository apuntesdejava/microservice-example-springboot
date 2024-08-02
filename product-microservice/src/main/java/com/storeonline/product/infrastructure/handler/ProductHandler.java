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

import com.storeonline.product.application.services.ProductReactiveCrudService;
import com.storeonline.product.domain.models.Product;
import com.storeonline.product.domain.models.ProductCategory;
import com.storeonline.product.infrastructure.handler.request.ProductRequest;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Component
public class ProductHandler {

    private final ProductReactiveCrudService service;

    public ProductHandler(ProductReactiveCrudService service) {
        this.service = service;
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(productRequest -> {
                    var product = Product.builder()
                            .name(productRequest.name())
                            .salePrice(productRequest.salePrice())
                            .category(ProductCategory.builder().categoryId(productRequest.categoryId()).build())
                            .build();
                    return service.create(product)
                            .flatMap(model -> {
                                try {
                                    var uri = new URI("/product/" + model.getProductId());
                                    return ServerResponse.created(uri)
                                            .body(BodyInserters.fromValue(model));
                                } catch (URISyntaxException e) {
                                    return ServerResponse.status(500).build();
                                }
                            });
                });
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        var id = NumberUtils.parseNumber(request.pathVariable("productId"), Integer.class);
        return service.findById(id)
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product))
                        .switchIfEmpty(ServerResponse.notFound().build()));

    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        var id = NumberUtils.parseNumber(request.pathVariable("productId"), Integer.class);
        return service.delete(id)
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }
}
