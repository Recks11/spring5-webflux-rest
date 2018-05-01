package com.rexijie.springframework.spring5webfluxrest.controllers;

import com.rexijie.springframework.spring5webfluxrest.domain.Category;
import com.rexijie.springframework.spring5webfluxrest.repository.CategoryRepository;
import com.rexijie.springframework.spring5webfluxrest.service.CategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryService categoryService;
    CategoryController categoryController;

    @Before
    public void setUp() throws Exception {

        categoryService = Mockito.mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void listCategories() {
        given(categoryService.getAll()).willReturn(
                Flux.just(
                        Category.builder().description("category1").build(),
                        Category.builder().description("category1").build()
                )
        );
        webTestClient.get().uri(CategoryController.API_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    public void getCategoryById() {
        given(categoryService.findById(anyString())).willReturn(
                Mono.just(Category.builder().description("category1").build())
        );

        webTestClient.get()
                .uri(CategoryController.API_URL + "/any")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);
    }

    @Test
    public void createCategory() {
        given(categoryService.createAndSaveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().build());

        webTestClient.post()
                .uri(CategoryController.API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void updateCategory() {
        given(categoryService.createAndSave(any(Category.class))).willReturn(Mono.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Updated").build());

        webTestClient.put()
                .uri(CategoryController.API_URL + "/any")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patchCategory() {
        Mono<Category> categoryMono = Mono.just(Category.builder().description("Updated").build());

        given(categoryService.patch(anyString(), any(Category.class))).willReturn(categoryMono);


        webTestClient.patch()
                .uri(CategoryController.API_URL + "/any")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);

        verify(categoryService, times(1)).patch(anyString(), any(Category.class));
    }

    @Test
    public void deleteCategory() {

        given(categoryService.delete(anyString())).willReturn(Mono.empty());
        webTestClient.delete()
                .uri(CategoryController.API_URL+"/id")
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryService, times(1))
                .delete("id");

    }
}