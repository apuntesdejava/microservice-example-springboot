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
package com.storeonline.product.domain.models;

import java.util.Objects;

/**
 *
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
public class Product {

    private int productId;
    private ProductCategory category;
    private String name;
    private double salePrice;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public static class ProductBuilder {

        private int productId;
        private ProductCategory category;
        private String name;
        private double salePrice;

        public ProductBuilder productId(int productId) {
            this.productId = productId;
            return this;
        }

        public ProductBuilder category(ProductCategory category) {
            this.category = category;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder salePrice(double salePrice) {
            this.salePrice = salePrice;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setProductId(productId);
            product.setCategory(category);
            product.setName(name);
            product.setSalePrice(salePrice);
            return product;
        }
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}
