package com.rexijie.springframework.spring5webfluxrest.controllers;

import com.rexijie.springframework.spring5webfluxrest.domain.Vendor;
import com.rexijie.springframework.spring5webfluxrest.service.VendorService;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.API_URL)
public class VendorController {
    public static final String API_URL = "/api/v1/vendors";

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Vendor> listVendors() {
        return vendorService.getAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createVendors(@RequestBody Publisher<Vendor> vendorPublisher) {
        return vendorService.createAndSaveAll(vendorPublisher).then();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> updateVendor(@PathVariable("id") String id, @RequestBody Vendor vendor) {
        return vendorService.update(id, vendor);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Vendor> patchVendor(@PathVariable("id") String id, @RequestBody Vendor vendor) {
        return vendorService.patch(id, vendor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteVendor(@PathVariable("id") String id) {
        return vendorService.delete(id);
    }
}
