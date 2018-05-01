package com.rexijie.springframework.spring5webfluxrest.service;

import com.rexijie.springframework.spring5webfluxrest.domain.Vendor;
import com.rexijie.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendorServiceImplTest {
    @Mock
    VendorRepository vendorRepository;
    @InjectMocks
    VendorServiceImpl vendorService;

    Mono<Vendor> savedVendorMono;

    @Before
    public void setUp() throws Exception {
        savedVendorMono = Mono.just(Vendor.builder().firstName("John").lastName("Snow").build());
    }

    @Test
    public void getAll() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(new Vendor(), new Vendor()));

        assertEquals(vendorService.getAll().count().block(), Long.valueOf(2L));
    }

    @Test
    public void findById() {
        given(vendorRepository.findById(anyString())).willReturn(savedVendorMono);

        Mono<Vendor> returnedMono = vendorService.findById("johnSnowId");
        assertEquals(savedVendorMono, returnedMono);
    }

    @Test
    public void createAndSave() {
        given(vendorRepository.save(any(Vendor.class))).willReturn(savedVendorMono);
        Mono<Vendor> returnedMono = vendorService.createAndSave(savedVendorMono.block());

        assertEquals(returnedMono, returnedMono);
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }

    @Test
    public void createAndSaveAll() {
        Flux<Vendor> vendorFlux = Flux.just(new Vendor(), new Vendor());
        when(vendorRepository.saveAll(any(Publisher.class))).thenReturn(vendorFlux);

        Flux<Vendor> returnedFlux = vendorService.createAndSaveAll(vendorFlux);
        assertEquals(returnedFlux.count().block(), Long.valueOf(2L));
        verify(vendorRepository, times(1)).saveAll(any(Publisher.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUpdate() {
        Mono<Vendor> updatedMono = Mono.just(Vendor.builder().firstName(null).lastName(null).build());

        vendorService.update("id", updatedMono.block()).block();
    }

    @Test
    public void update() {
        Mono<Vendor> updatedMono = Mono.just(Vendor.builder().firstName("Arya").lastName("Stark").build());

        given(vendorRepository.findById(anyString())).willReturn(savedVendorMono);
        given(vendorRepository.save(any(Vendor.class))).willReturn(updatedMono);

        Mono<Vendor> returnedMonoAfterUpdate = vendorService.update("id", updatedMono.block());

        assertEquals(updatedMono.block(), returnedMonoAfterUpdate.block());

    }

    @Test
    public void patch() {
        Mono<Vendor> johnSnowMono = Mono.just(Vendor.builder().firstName("John").lastName("Snow").build());
        Mono<Vendor> sansaMono = Mono.just(Vendor.builder().firstName("Sansa").lastName(null).build());

        given(vendorRepository.findById(anyString())).willReturn(johnSnowMono);
        given(vendorRepository.findById(anyString())).willReturn(johnSnowMono);

        //given that vendorRepo.save any vendor will return a mono of that vendor
        given(vendorRepository.save(any(Vendor.class))).will(invocation -> Mono.just(invocation.getArguments()[0]));

        Mono<Vendor> firstNamePatchedVendorMono = vendorService.patch("id", sansaMono.block());

        System.out.println(firstNamePatchedVendorMono.block().getFirstName());

        assertEquals("Sansa", firstNamePatchedVendorMono.block().getFirstName());
        assertEquals("Snow", firstNamePatchedVendorMono.block().getLastName());

        Mono<Vendor> starkMono = Mono.just(Vendor.builder().firstName(null).lastName("Stark").build());

        Mono<Vendor> lastNamePatchedVendorMono = vendorService.patch("id1", starkMono.block());

        assertEquals("Stark", lastNamePatchedVendorMono.block().getLastName());
    }

    @Test
    public void delete() {
        given(vendorRepository.deleteById(anyString())).willReturn(Mono.empty());

        vendorService.delete("id").block();
        verify(vendorRepository, times(1)).deleteById(anyString());
    }
}