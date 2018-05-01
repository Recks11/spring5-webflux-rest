package com.rexijie.springframework.spring5webfluxrest.service;

import com.rexijie.springframework.spring5webfluxrest.domain.Category;
import com.rexijie.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Flux<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Mono<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Mono<Category> createAndSave(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Flux<Category> createAndSaveAll(Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher);
    }

    @Override
    public Mono<Category> update(String id, Category category) {
        Mono<Category> savedCategoryMono = categoryRepository.findById(id);

        return savedCategoryMono.flatMap(
                savedCategory -> {
                    if (!Objects.equals(savedCategory.getDescription(), category.getDescription())) {
                        savedCategory.setDescription(category.getDescription());
                        return categoryRepository.save(savedCategory);
                    } else {
                        return Mono.just(savedCategory);
                    }
                }
        );
    }

    @Override
    public Mono<Category> patch(String id, Category category) {
        if (category.getDescription() != null) {
            return this.update(id, category);
        }else {
            return Mono.error(new IllegalArgumentException());
        }
//        return category.getDescription() != null ? this.update(id, category) : Mono.error(new IllegalArgumentException());
    }

    @Override
    public Mono<Void> delete(String id) {
        return categoryRepository.deleteById(id);
    }
}
