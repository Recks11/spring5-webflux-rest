package com.rexijie.springframework.spring5webfluxrest.service;

import com.rexijie.springframework.spring5webfluxrest.domain.Vendor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VendorService {

    Flux<Vendor> getAll();
    Mono<Vendor> findById(String id);
    Mono<Vendor> createAndSave(Vendor vendor);
    Flux<Vendor> createAndSaveAll(Publisher<Vendor> vendorPublisher);
    Mono<Vendor> update(String id, Vendor vendor);
    Mono<Vendor> patch(String id, Vendor vendor);
    Mono<Void> delete(String id);
}
