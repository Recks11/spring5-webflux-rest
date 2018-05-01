package com.rexijie.springframework.spring5webfluxrest.controllers;

import com.rexijie.springframework.spring5webfluxrest.domain.Vendor;
import com.rexijie.springframework.spring5webfluxrest.service.VendorService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class VendorControllerTest {
    VendorController vendorController;
    VendorService vendorService;
    WebTestClient webTestClient;

    Mono<Vendor> savedVendorMono;

    @Before
    public void setUp() throws Exception {
        vendorService = Mockito.mock(VendorService.class);
        vendorController = new VendorController(vendorService);

        webTestClient = WebTestClient.bindToController(vendorController).build();

        savedVendorMono = Mono.just(Vendor.builder().firstName("Dave").lastName("Shappel").build());
    }

    @Test
    public void listVendors() {
        given(vendorService.getAll()).willReturn(
                Flux.just(
                        Vendor.builder().firstName("Jane").lastName("doe").build(),
                        Vendor.builder().firstName("Jack").lastName("Daniels").build()
                )
        );

        webTestClient.get()
                .uri(VendorController.API_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        given(vendorService.findById(anyString()))
                .willReturn(
                        Mono.just(Vendor.builder().firstName("Jane").lastName("Doe").build())
                );
        webTestClient.get()
                .uri(VendorController.API_URL+"/any")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);
    }

    @Test
    public void createVendors() {
        given(vendorService.createAndSaveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));
        Mono<Vendor> vendor = Mono.just(Vendor.builder().firstName("Jane").lastName("Doe").build());

        webTestClient.post()
                .uri(VendorController.API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(vendor, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void updateVendor() {
        Mono<Vendor> vendorUpdated = Mono.just(Vendor.builder().firstName("Dave").lastName("Shappel").build());

        given(vendorService.createAndSave(any(Vendor.class)))
                .willReturn(vendorUpdated);

        webTestClient.put()
                .uri(VendorController.API_URL+"/id")
                .contentType(MediaType.APPLICATION_JSON)
                .body(vendorUpdated, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);
    }

    @Test
    public void patchVendor() {
        given(vendorService.patch(anyString(), any(Vendor.class))).willReturn(savedVendorMono);

        webTestClient.patch()
                .uri(VendorController.API_URL+"/id")
                .contentType(MediaType.APPLICATION_JSON)
                .body(savedVendorMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Vendor.class);
    }

    @Test
    public void deleteVendor() {
        given(vendorService.delete("id")).willReturn(Mono.empty());
        webTestClient.delete()
                .uri(VendorController.API_URL+"/id")
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorService, times(1)).delete("id");
    }
}