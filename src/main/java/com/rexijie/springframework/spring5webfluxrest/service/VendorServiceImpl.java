package com.rexijie.springframework.spring5webfluxrest.service;

import com.rexijie.springframework.spring5webfluxrest.domain.Vendor;
import com.rexijie.springframework.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Flux<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Mono<Vendor> findById(String id) {
        return vendorRepository.findById(id);
    }

    @Override
    public Mono<Vendor> createAndSave(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public Flux<Vendor> createAndSaveAll(Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher);
    }

    @Override
    public Mono<Vendor> update(String id, Vendor vendor) {
        if (vendor.getFirstName() == null || vendor.getLastName() == null)
            return Mono.error(new IllegalArgumentException());
        else
            return vendorRepository.findById(id)
                    .flatMap(savedVendor -> {
                        if (!savedVendor.getFirstName().equals(vendor.getFirstName()))
                            savedVendor.setFirstName(vendor.getFirstName());

                        if (!savedVendor.getLastName().equals(vendor.getLastName()))
                            savedVendor.setLastName(vendor.getLastName());

                        return vendorRepository.save(savedVendor);
                    });
    }

    @Override
    public Mono<Vendor> patch(String id, Vendor vendor) {
        return vendorRepository.findById(id)
                .flatMap(savedVendor -> {
                    if (vendor != null && vendor.getFirstName() != null && !vendor.getFirstName().equals(savedVendor.getFirstName()))
                        savedVendor.setFirstName(vendor.getFirstName());

                    if (vendor != null && vendor.getLastName() != null && !vendor.getLastName().equals(savedVendor.getLastName()))
                        savedVendor.setLastName(vendor.getLastName());

                    return vendorRepository.save(savedVendor);
                });
    }

    @Override
    public Mono<Void> delete(String id) {
        return vendorRepository.deleteById(id);
    }
}
