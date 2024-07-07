package com.projet3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRentalDto {
    @NotBlank
    private String name;
    @NotNull
    private Double surface;
    @NotNull
    private Double price;
    @NotBlank
    private String description;
}

