package com.rexijie.springframework.spring5webfluxrest.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Category {
    @Id
    private String id;
    private String description;
}
