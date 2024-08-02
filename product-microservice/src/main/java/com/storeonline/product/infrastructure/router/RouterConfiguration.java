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
package com.storeonline.product.infrastructure.router;

import com.storeonline.product.infrastructure.handler.ProductCategoryHandler;
import com.storeonline.product.infrastructure.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> productCategoryRoutes(ProductCategoryHandler handler) {
        return RouterFunctions
                .route(
                        GET("/category").and(accept(APPLICATION_JSON)),
                        request -> handler.listAll()
                )
                .andRoute(
                        POST("/category").and(accept(APPLICATION_JSON)),
                        handler::create
                )
                .andRoute(GET("/category/{id}").and(accept(APPLICATION_JSON)),
                        handler::findById
                )
                .andRoute(PUT("/category/{id}/enabled").and(accept(APPLICATION_JSON)),
                        handler::updateEnabled
                );
    }

    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        return RouterFunctions
                .route(
                        POST("/product").and(accept(APPLICATION_JSON)),
                        productHandler::create
                )
                .andRoute(GET("/product/{productId}").and(accept(APPLICATION_JSON)),
                        productHandler::findById
                )
                .andRoute(DELETE("/product/{productId}").and(accept(APPLICATION_JSON)),
                        productHandler::deleteById);

    }
}
