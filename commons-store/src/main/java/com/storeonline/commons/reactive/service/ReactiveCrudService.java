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
package com.storeonline.commons.reactive.service;

import com.storeonline.commons.reactive.repository.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @param <M>
 * @param <ID>
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
public abstract class ReactiveCrudService<M, ID> {

    private final ReactiveCrudRepository<M, ID> repository;

    public ReactiveCrudService(ReactiveCrudRepository<M, ID> repository) {
        this.repository = repository;

    }

    public Mono<M> create(M model) {
        return repository.create(model);
    }

    public Mono<M> findById(ID id) {
        return repository.findById(id);
    }

    public Flux<M> listAll() {
        return repository.listAll();
    }

    public Mono<Void> delete(ID id) {
        return repository.delete(id);
    }

    public Mono<M> update(ID id, M newModel) {
        return repository.update(id, newModel);
    }

    protected ReactiveCrudRepository<M, ID> getRepository() {
        return repository;
    }


}
