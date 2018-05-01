package com.rexijie.springframework.spring5webfluxrest.bootstrap;

import com.rexijie.springframework.spring5webfluxrest.domain.Category;
import com.rexijie.springframework.spring5webfluxrest.domain.Vendor;
import com.rexijie.springframework.spring5webfluxrest.repository.CategoryRepository;
import com.rexijie.springframework.spring5webfluxrest.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@Component
public class PopulateData implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PopulateData(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        setUpVendors();
        setUpCategories();
    }

    private void setUpVendors() throws Exception {
        Vendor ebun = new Vendor();
        ebun.setFirstName("Ebun");
        ebun.setLastName("Peters");
        Vendor ayo = new Vendor();
        ayo.setFirstName("Ayo");
        ayo.setLastName("Olami");

        Stream<Vendor> vendorStream = Stream.of(ebun, ayo);

        if (vendorRepository.count().block() < 2) {
            vendorRepository.saveAll(Flux.fromStream(vendorStream)).count().block();
        }
    }

    private void setUpCategories() throws Exception{
        if(categoryRepository.count().block() == 0) {
            categoryRepository.save(Category.builder()
            .description("Fruits").build()).block();
            categoryRepository.save(Category.builder()
            .description("Nuts").build()).block();
            categoryRepository.save(Category.builder()
            .description("Breads").build()).block();
            categoryRepository.save(Category.builder()
            .description("Meats").build()).block();
            categoryRepository.save(Category.builder()
            .description("Eggs").build()).block();
            System.out.println("Loaded Categories: "+ categoryRepository.count().block());
        }
    }
}
