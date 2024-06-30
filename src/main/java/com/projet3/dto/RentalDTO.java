package com.projet3.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class RentalDTO {

    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private Long userId;
}
