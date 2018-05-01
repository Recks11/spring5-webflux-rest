package com.rexijie.springframework.spring5webfluxrest.controllers;

import com.rexijie.springframework.spring5webfluxrest.domain.Category;
import com.rexijie.springframework.spring5webfluxrest.service.CategoryService;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.API_URL)
public class CategoryController {

    public static final String API_URL = "/api/v1/categories";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Category> listCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> getCategoryById(@PathVariable("id") String id) {
        return categoryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createCategory(@RequestBody Publisher<Category> categoryPublisher) {
        return categoryService.createAndSaveAll(categoryPublisher).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> updateCategory(@PathVariable("id") String id, @RequestBody Category category) {
        category.setId(id);
        return categoryService.createAndSave(category);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Category> patchCategory(@PathVariable("id") String id, @RequestBody Category category) {
        return categoryService.patch(id, category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteCategory(@PathVariable("id") String id){
        return categoryService.delete(id);
    }

}
