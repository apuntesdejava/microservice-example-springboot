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
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
public class ProductCategory {

    private int categoryId;
    private String name;
    private boolean enabled;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class ProductCategoryBuilder {

        private int categoryId;
        private String name;
        private boolean enabled;

        private ProductCategoryBuilder() {
        }

        public ProductCategoryBuilder categoryId(int categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public ProductCategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductCategoryBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public ProductCategory build() {
            ProductCategory productCategory = new ProductCategory();
            productCategory.categoryId = categoryId;
            productCategory.enabled = enabled;
            productCategory.name = name;
            return productCategory;
        }
    }

    public static ProductCategoryBuilder builder() {
        return new ProductCategoryBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(categoryId);
    }
}
