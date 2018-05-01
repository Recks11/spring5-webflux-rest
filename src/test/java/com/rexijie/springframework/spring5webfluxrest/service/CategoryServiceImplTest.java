package com.rexijie.springframework.spring5webfluxrest.service;

import com.rexijie.springframework.spring5webfluxrest.domain.Category;
import com.rexijie.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    @Mock CategoryRepository categoryRepository;
    @InjectMocks CategoryServiceImpl categoryService;
    Mono<Category> savedCategoryMono;
    Flux<Category> categoryFlux;

    @Before
    public void setUp() throws Exception {
        this.savedCategoryMono = Mono.just(Category.builder().description("random").build());
        this.categoryFlux = Flux.just(new Category(), new Category());
    }

    @Test
    public void getAll() {
        given(categoryRepository.findAll()).willReturn(categoryFlux);

        Flux<Category> getAll = categoryService.getAll();
        assertEquals(getAll.count().block().longValue(), 2L);
    }

    @Test
    public void findById() {

        given(categoryRepository.findById(anyString())).willReturn(savedCategoryMono);

        Mono<Category> retrievedCategory = categoryRepository.findById(anyString());
        assertEquals(retrievedCategory, savedCategoryMono);
    }

    @Test
    public void createAndSave() {
        given(categoryRepository.save(any(Category.class))).willReturn(savedCategoryMono);

        Mono<Category> returnedMonoAfterSave = categoryService.createAndSave(new Category());
        assertEquals(savedCategoryMono, returnedMonoAfterSave);
    }

    @Test
    public void createAndSaveAll() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(categoryFlux);

        Flux<Category> returnedFLux = categoryService.createAndSaveAll(Flux.just(new Category()));

        assertEquals(categoryFlux, returnedFLux);
    }

    @Test
    public void update() {
        Mono<Category> updatedMono = Mono.just(Category.builder().description("Updated").build());
        given(categoryRepository.findById(anyString())).willReturn(savedCategoryMono);
        given(categoryRepository.save(updatedMono.block())).willReturn(updatedMono);

        Mono<Category> updatedMonoResponse = categoryService.update("any", updatedMono.block());

        assertEquals(updatedMono.block(), updatedMonoResponse.block());
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    public void updateSameCategory() {
        given(categoryRepository.findById(anyString())).willReturn(savedCategoryMono);

        Mono<Category> updatedMonoResponse = categoryService.update("any", savedCategoryMono.block());

        assertEquals(savedCategoryMono.block(), updatedMonoResponse.block());
        verify(categoryRepository, times(0)).save(any());
    }

    @Test
    public void patch() {
        Mono<Category> patchedMono = Mono.just(Category.builder().description("Patched").build());

        given(categoryRepository.findById(anyString())).willReturn(savedCategoryMono);
        given(categoryRepository.save(patchedMono.block())).willReturn(patchedMono);

        Mono<Category> updatedMonoResponse = categoryService.patch("any", patchedMono.block());

        assertEquals(patchedMono.block(), updatedMonoResponse.block());
        verify(categoryRepository, times(1)).save(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void badPatch() throws Exception {
        Mono<Category> nullCategoryMono = Mono.just(Category.builder().description(null).build());

        Mono<Category> nullHandlerResponse = categoryService.patch("any", nullCategoryMono.block());
        nullHandlerResponse.block();
    }

    @Test
    public void delete() {
        Mono<Void> voidMono = Mono.empty();
        given(categoryRepository.deleteById(anyString())).willReturn(voidMono);

        Mono<Void> identity = categoryService.delete("identity").then();
        verify(categoryRepository, times(1)).deleteById(anyString());
    }
}