package com.rexijie.springframework.spring5webfluxrest.service;

import com.rexijie.springframework.spring5webfluxrest.domain.Category;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CategoryService {

    Flux<Category> getAll();
    Mono<Category> findById(String id);
    Mono<Category> createAndSave(Category category);
    Flux<Category> createAndSaveAll(Publisher<Category> categoryPublisher);
    Mono<Category> update(String id, Category category);
    Mono<Category> patch(String id, Category category);
    Mono<Void> delete(String id);

}
